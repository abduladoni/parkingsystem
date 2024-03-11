package dev.abduladoni.parkingsystem.service.parkingfee;

import dev.abduladoni.parkingsystem.exception.InvalidParkingRequestException;
import dev.abduladoni.parkingsystem.model.ParkingVehicle;
import dev.abduladoni.parkingsystem.service.CacheService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

class DailyParkingFeeStrategyTest {

    @Mock
    private CacheService cacheService;

    @InjectMocks
    private DailyParkingFeeStrategy dailyParkingFeeStrategy;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    public void afterEach(){
        reset(cacheService);
    }


    @Test
    @DisplayName("Calculate parking fee during tariff hours")
    public void testCalculateParkingFeeDuringTariffHours() {
        //Given
        ParkingVehicle parkingVehicle = new ParkingVehicle();
        parkingVehicle.setStreetName("Test Street");

        when(cacheService.loadParkingTariffMetaData()).thenReturn(Map.of("Test Street", 10));


        BigDecimal result = dailyParkingFeeStrategy.calculateParkingFee(parkingVehicle,
                LocalDateTime.of(2024, 3, 9, 9, 0),
                LocalDateTime.of(2024, 3, 9, 10, 0));


        assertEquals(BigDecimal.valueOf(6), result);
    }

    @Test
    @DisplayName("Calculate parking fee outside tariff hours")
    public void testCalculateParkingFeeOutsideTariffHours() {
        //Given
        ParkingVehicle parkingVehicle = new ParkingVehicle();
        parkingVehicle.setStreetName("Test Street");

        when(cacheService.loadParkingTariffMetaData()).thenReturn(Map.of("Test Street", 10));

        //When
        BigDecimal result = dailyParkingFeeStrategy.calculateParkingFee(parkingVehicle,
                LocalDateTime.of(2024, 3, 9, 22, 0),
                LocalDateTime.of(2024, 3, 9, 23, 0));

        //Then
        assertEquals(BigDecimal.ZERO, result);
    }

    @Test
    @DisplayName("Calculate parking fee with invalid street name")
    public void testCalculateParkingFeeWithInvalidStreetName() {
        //
        ParkingVehicle parkingVehicle = new ParkingVehicle();
        parkingVehicle.setStreetName("Invalid Street");

        when(cacheService.loadParkingTariffMetaData()).thenReturn(Map.of("Test Street", 10));
        //When
        //Then
        assertThrows(InvalidParkingRequestException.class,
                () -> dailyParkingFeeStrategy.calculateParkingFee(
                        parkingVehicle, LocalDateTime.of(2024, 3, 9, 8, 0),
                        LocalDateTime.of(2024, 3, 9, 9, 0)));
    }
}