package dev.abduladoni.parkingsystem.service;

import dev.abduladoni.parkingsystem.dto.MonitorParkingVehicleRequestDTO;
import dev.abduladoni.parkingsystem.dto.ParkingSessionDTO;
import dev.abduladoni.parkingsystem.dto.ParkingVehicleRequestDTO;
import dev.abduladoni.parkingsystem.mapper.ParkingVehicleMapper;
import dev.abduladoni.parkingsystem.mapper.PenaltyNotificationMapper;
import dev.abduladoni.parkingsystem.model.ParkingStatus;
import dev.abduladoni.parkingsystem.model.ParkingVehicle;
import dev.abduladoni.parkingsystem.exception.InvalidParkingRequestException;
import dev.abduladoni.parkingsystem.daos.ParkingVehicleRepository;
import dev.abduladoni.parkingsystem.daos.PenaltyNotificationRepository;
import dev.abduladoni.parkingsystem.service.parkingfee.ParkingFeeHelper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class ParkingServiceImplTest {

    @Mock
    private  ParkingVehicleMapper parkingVehicleMapper;
    @Mock
    private  PenaltyNotificationMapper penaltyNotificationMapper;
    @Mock
    private ParkingVehicleRepository parkingVehicleRepository;
    @Mock
    private  CacheService cacheService;

    @Mock
    private PenaltyNotificationRepository penaltyNotificationRepository;

    @Mock
    private ParkingFeeHelper parkingFeeHelper;

    @InjectMocks
    private ParkingServiceImpl parkingService;

    @AfterEach
    public void afterEach(){
        reset(parkingVehicleRepository);
        reset(cacheService);
        reset(parkingFeeHelper);
        reset(penaltyNotificationRepository);
        reset(parkingVehicleMapper);
        reset(penaltyNotificationMapper);
    }

    @Test
    @DisplayName("Should park a vehicle successfully and save request")
    public void testParkVehicleSavesRequestSuccessfully() {
        // Given
        ParkingVehicleRequestDTO requestDTO = ParkingVehicleRequestDTO.builder().vehicleNumber("AA-691-A").streetName("Street1").build();

        when(parkingVehicleRepository.findByLicensePlateNumberAndStatus(requestDTO.getVehicleNumber(), ParkingStatus.ACTIVE.getStatus()))
                .thenReturn(Optional.empty());
        when(cacheService.loadParkingTariffMetaData()).thenReturn(Collections.singletonMap("Street1", 1));

        // When
        parkingService.registerVehicle(requestDTO);

        // Then
        verify(parkingVehicleRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("Should throw InvalidParkingRequestException when there is already active parking session")
    public void testParkVehicleThrowsExceptionWhenThereIsAlreadyActiveParkingSession() {
        // Given
        ParkingVehicleRequestDTO requestDTO = ParkingVehicleRequestDTO.builder().vehicleNumber("AA-691-A").streetName("Street1").build();
        ParkingVehicle parkingVehicle = new ParkingVehicle();
        parkingVehicle.setStatus(ParkingStatus.ACTIVE.getStatus());
        when(parkingVehicleRepository.findByLicensePlateNumberAndStatus(requestDTO.getVehicleNumber(), ParkingStatus.ACTIVE.getStatus()))
                .thenReturn(Optional.of(parkingVehicle));
        when(cacheService.loadParkingTariffMetaData()).thenReturn(Collections.singletonMap("Street1", 1));


        // When
        // Then
        assertThrows(InvalidParkingRequestException.class,
                ()->parkingService.registerVehicle(requestDTO));
    }


    @Test
    @DisplayName("Should unpark a vehicle successfully and return parking fee")
    public void testUnParkVehicleAndReturnParkingFeeSuccessfully() {
        // Given
        String vehicleNumber = "AA-781-B";
        ParkingVehicle parkingVehicle = new ParkingVehicle();
        parkingVehicle.setStatus(ParkingStatus.ACTIVE.getStatus());
        when(parkingVehicleRepository.findByLicensePlateNumberAndStatus(vehicleNumber, ParkingStatus.ACTIVE.getStatus()))
                .thenReturn(Optional.of(parkingVehicle));
        when(parkingFeeHelper.calculateParkingFee(parkingVehicle)).thenReturn(BigDecimal.ONE);

        // When
        ParkingSessionDTO result = parkingService.unRegisterVehicle(vehicleNumber);

        // Then
        assertNotNull(result);
        assertEquals("1", result.getParkingFee());
    }

    @Test
    @DisplayName("Should throw InvalidParkingRequestException when there is no Active Parking Session for the vehicle")
    public void testUnParkVehicleThrowsExceptionWhenThereIsNoActiveParkingSession() {
        // Given
        String vehicleNumber = "AA-781-B";
        when(parkingVehicleRepository.findByLicensePlateNumberAndStatus(vehicleNumber, ParkingStatus.ACTIVE.getStatus()))
                .thenReturn(Optional.empty());

        // When
        // Then
        assertThrows(InvalidParkingRequestException.class,
                ()->parkingService.unRegisterVehicle(vehicleNumber));
    }

    @Test
    @DisplayName("Should penalize unregistered vehicles successfully")
    void testPenalizeUnregisteredVehiclesToSaveRecordsSuccessfully() {
        // Given
        MonitorParkingVehicleRequestDTO requestDTO = MonitorParkingVehicleRequestDTO.builder()
                .vehicleNumber("AA-999-B")
                .streetName("Street1")
                .observationTime(LocalDateTime.now().toString())
                .build();

        when(cacheService.loadParkingTariffMetaData()).thenReturn(Collections.singletonMap("Street1", 1));
        when(parkingVehicleRepository.findVehicleNumbersByStatus(ParkingStatus.ACTIVE.getStatus()))
                .thenReturn(Collections.emptyList());

        // When
        parkingService.penalizeUnregisteredVehicles(Collections.singletonList(requestDTO));

        // Then
        verify(penaltyNotificationRepository, times(1)).saveAll(any());
    }

    @Test
    @DisplayName("Should throw InvalidParkingRequestException when invalid vehicle details are provided for parking")
    void testPenalizeUnregisteredVehiclesToThrowInvalidParkingRequestExceptionWhenIncorrectStreetNameIsGiven() {
        // Given
        MonitorParkingVehicleRequestDTO requestDTO = MonitorParkingVehicleRequestDTO.builder()
                .vehicleNumber("AA-999-B")
                .streetName("Street1")
                .observationTime(LocalDateTime.now().toString())
                .build();

        when(cacheService.loadParkingTariffMetaData()).thenReturn(Collections.singletonMap("invalidStreetName", 1));
        when(parkingVehicleRepository.findVehicleNumbersByStatus(ParkingStatus.ACTIVE.getStatus()))
                .thenReturn(Collections.emptyList());

        // When
        // Then
        assertThrows(InvalidParkingRequestException.class,
                ()->parkingService.penalizeUnregisteredVehicles(Collections.singletonList(requestDTO)));
    }

}