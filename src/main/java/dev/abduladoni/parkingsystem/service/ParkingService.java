package dev.abduladoni.parkingsystem.service;

import dev.abduladoni.parkingsystem.dto.MonitorParkingVehicleRequestDTO;
import dev.abduladoni.parkingsystem.dto.ParkingSessionDTO;
import dev.abduladoni.parkingsystem.dto.ParkingVehicleRequestDTO;

import java.util.List;

public interface ParkingService {

    void parkVehicle(final ParkingVehicleRequestDTO parkingVehicleRequestDTO);

    ParkingSessionDTO unParkVehicle(final String vehicleNumber);

    void penalizeUnregisteredVehicles(final List<MonitorParkingVehicleRequestDTO> parkingVehicleRequestDTOs);

}
