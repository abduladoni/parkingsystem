package dev.abduladoni.parkingsystem.mapper;

import dev.abduladoni.parkingsystem.dto.ParkingVehicleRequestDTO;
import dev.abduladoni.parkingsystem.model.ParkingVehicle;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ParkingVehicleMapper {

    @Mapping(source = "vehicleNumber", target = "licensePlateNumber")
    @Mapping(source = "streetName", target = "streetName")
    @Mapping(target = "startTime", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "status", expression = "java(dev.abduladoni.parkingsystem.model.ParkingStatus.ACTIVE.getStatus())")
    @Mapping(target = "lastUpdatedTs", expression = "java(java.time.LocalDateTime.now())")
    ParkingVehicle dtoToModel(ParkingVehicleRequestDTO dto);
}