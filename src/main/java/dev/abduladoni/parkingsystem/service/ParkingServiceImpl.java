package dev.abduladoni.parkingsystem.service;

import dev.abduladoni.parkingsystem.daos.ParkingVehicleRepository;
import dev.abduladoni.parkingsystem.daos.PenaltyNotificationRepository;
import dev.abduladoni.parkingsystem.dto.MonitorParkingVehicleRequestDTO;
import dev.abduladoni.parkingsystem.dto.ParkingSessionDTO;
import dev.abduladoni.parkingsystem.dto.ParkingVehicleRequestDTO;
import dev.abduladoni.parkingsystem.exception.InvalidParkingRequestException;
import dev.abduladoni.parkingsystem.mapper.ParkingVehicleMapper;
import dev.abduladoni.parkingsystem.mapper.PenaltyNotificationMapper;
import dev.abduladoni.parkingsystem.model.ParkingStatus;
import dev.abduladoni.parkingsystem.model.ParkingVehicle;
import dev.abduladoni.parkingsystem.model.PenaltyNotification;
import dev.abduladoni.parkingsystem.service.parkingfee.ParkingFeeHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ParkingServiceImpl implements ParkingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ParkingServiceImpl.class);

    private final ParkingVehicleMapper parkingVehicleMapper;
    private final PenaltyNotificationMapper penaltyNotificationMapper;
    private final ParkingVehicleRepository parkingVehicleRepository;
    private final PenaltyNotificationRepository penaltyNotificationRepository;
    private final CacheService cacheService;
    private final ParkingFeeHelper parkingFeeHelper;
    @Autowired
    public ParkingServiceImpl(final ParkingVehicleMapper parkingVehicleMapper,
                              final PenaltyNotificationMapper penaltyNotificationMapper,
                              final ParkingVehicleRepository parkingVehicleRepository,
                              final PenaltyNotificationRepository penaltyNotificationRepository,
                              final CacheService cacheService,
                              final ParkingFeeHelper parkingFeeHelper) {
        this.parkingVehicleMapper = parkingVehicleMapper;
        this.penaltyNotificationMapper = penaltyNotificationMapper;
        this.parkingVehicleRepository = parkingVehicleRepository;
        this.penaltyNotificationRepository = penaltyNotificationRepository;
        this.cacheService = cacheService;
        this.parkingFeeHelper = parkingFeeHelper;
    }

    @Override
    public void parkVehicle(ParkingVehicleRequestDTO parkingVehicleRequestDTO) {
        LOGGER.info("Starting parkVehicle method for vehicle: {}", parkingVehicleRequestDTO.getVehicleNumber());
        try {
            validateStreetName(parkingVehicleRequestDTO.getStreetName());
            validateNoActiveParkingSessionExists(parkingVehicleRequestDTO.getVehicleNumber());

            parkingVehicleRepository.save(parkingVehicleMapper.dtoToModel(parkingVehicleRequestDTO));
            LOGGER.info("Vehicle parked successfully: {}", parkingVehicleRequestDTO.getVehicleNumber());
        } catch (Exception e) {
            LOGGER.error("Error occurred while parking the vehicle: {}", parkingVehicleRequestDTO.getVehicleNumber(), e);
            throw e;
        }
        LOGGER.info("Finished parkVehicle method for vehicle: {}", parkingVehicleRequestDTO.getVehicleNumber());
    }

    @Override
    public ParkingSessionDTO unParkVehicle(String vehicleNumber) {
        LOGGER.info("Starting unParkVehicle method for vehicle: {}", vehicleNumber);
        try {
            ParkingVehicle parkingVehicle = getActiveParkingSession(vehicleNumber);
            // Calculate the parking fee
            BigDecimal parkingFee = parkingFeeHelper.calculateParkingFee(parkingVehicle);


            parkingVehicle.setPrice(parkingFee);
            parkingVehicle.setStatus(ParkingStatus.ENDED.getStatus());
            parkingVehicle.setEndTime(LocalDateTime.now());
            parkingVehicle.setLastUpdatedTs(LocalDateTime.now());
            parkingVehicleRepository.save(parkingVehicle);
            return ParkingSessionDTO.builder().parkingFee(parkingFee.toString()).build();
        } catch (Exception e) {
            LOGGER.error("Error occurred while unparking the vehicle: {}", vehicleNumber, e);
            throw e;
        }
    }

    @Override
    public void penalizeUnregisteredVehicles(List<MonitorParkingVehicleRequestDTO> monitorParkingVehicleRequestDTOS) {
        LOGGER.info("Starting penalizeUnregisteredVehicles method for {} vehicles",
                monitorParkingVehicleRequestDTOS.size());
        try {
            validateStreetNames(monitorParkingVehicleRequestDTOS);

            List<PenaltyNotification> vehiclesToBePenalized =
                    convertToPenaltyNotification(monitorParkingVehicleRequestDTOS);

            LOGGER.info("Count of Vehicles to be penalized: {}", vehiclesToBePenalized.size());
            penaltyNotificationRepository.saveAll(vehiclesToBePenalized);

        } catch (Exception e) {
            LOGGER.error("Error occurred while parking the vehicles", e);
            throw e;
        }
        LOGGER.info("Finished penalizeUnregisteredVehicles method for {} vehicles",
                monitorParkingVehicleRequestDTOS.size());
    }

    private void validateNoActiveParkingSessionExists(String vehicleNumber) {
        Optional<ParkingVehicle> parkingVehicle = parkingVehicleRepository.findByLicensePlateNumberAndStatus(
                vehicleNumber, ParkingStatus.ACTIVE.getStatus());
        if (parkingVehicle.isPresent()) {
            LOGGER.error("Vehicle with number {} has an active parking session", vehicleNumber);
            throw new InvalidParkingRequestException("Vehicle with number " + vehicleNumber
                    + " has an active parking session");
        }
    }

    private void validateStreetName(String streetName) {
        Map<String, Integer> parkingTariffMetaData = cacheService.loadParkingTariffMetaData();

        if (!parkingTariffMetaData.containsKey(streetName)) {
            LOGGER.error("Invalid street name: {}", streetName);
            throw new InvalidParkingRequestException("Invalid street name: " + streetName);
        }
    }

    private ParkingVehicle getActiveParkingSession(String vehicleNumber) {
        Optional<ParkingVehicle> parkingVehicle = parkingVehicleRepository.findByLicensePlateNumberAndStatus(
                vehicleNumber, ParkingStatus.ACTIVE.getStatus());
        if (parkingVehicle.isEmpty()) {
            LOGGER.error("No active parking session found for vehicle: {}", vehicleNumber);
            throw new InvalidParkingRequestException("No active parking session found for vehicle: " + vehicleNumber);
        }
        return parkingVehicle.get();
    }

    private void validateStreetNames(final List<MonitorParkingVehicleRequestDTO> monitorParkingVehicleRequestDTOS) {
        Map<String, Integer> parkingTariffMetaData = cacheService.loadParkingTariffMetaData();

        List<String> invalidStreetNames = monitorParkingVehicleRequestDTOS.stream()
                .map(MonitorParkingVehicleRequestDTO::getStreetName)
                .filter(streetName -> !parkingTariffMetaData.containsKey(streetName))
                .toList();
        if (!invalidStreetNames.isEmpty()) {
            LOGGER.error("Invalid street names: {}", invalidStreetNames);
            throw new InvalidParkingRequestException("Invalid street names: "
                    + String.join(",", invalidStreetNames));
        }
    }

    private List<PenaltyNotification> convertToPenaltyNotification(
            final List<MonitorParkingVehicleRequestDTO> monitorParkingVehicleRequestDTOS) {

        List<MonitorParkingVehicleRequestDTO> vehiclesToBePenalized = getVehiclesToBePenalized(monitorParkingVehicleRequestDTOS);
        return vehiclesToBePenalized.stream()
                .map(penaltyNotificationMapper::dtoToModel)
                .toList();
    }

    private List<MonitorParkingVehicleRequestDTO> getVehiclesToBePenalized(
            final List<MonitorParkingVehicleRequestDTO> monitorParkingVehicleRequestDTOS) {
        List<String> paidParkedVehicles = parkingVehicleRepository
                .findVehicleNumbersByStatus(ParkingStatus.ACTIVE.getStatus());
        return monitorParkingVehicleRequestDTOS.stream()
                .filter(parkingVehicleRequestDTO ->
                        !paidParkedVehicles.contains(parkingVehicleRequestDTO.getVehicleNumber()))
                .toList();
    }
}