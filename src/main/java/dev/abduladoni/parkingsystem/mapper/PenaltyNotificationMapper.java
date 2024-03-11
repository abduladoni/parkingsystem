package dev.abduladoni.parkingsystem.mapper;

import dev.abduladoni.parkingsystem.dto.MonitorParkingVehicleRequestDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import dev.abduladoni.parkingsystem.model.PenaltyNotification;

@Mapper(componentModel = "spring")
public interface PenaltyNotificationMapper {

    @Mapping(target = "licensePlateNumber", source = "vehicleNumber")
    @Mapping(target = "streetName", source = "streetName")
    @Mapping(target = "dateTime", expression = "java(java.time.LocalDateTime.parse(dto.getObservationTime(), java.time.format.DateTimeFormatter.ofPattern(\"yyyy-MM-dd HH:mm:ss\")))")
    @Mapping(target = "penaltySent", constant = "false")
    @Mapping(target = "lastUpdatedTs", expression = "java(java.time.LocalDateTime.now())")
    PenaltyNotification dtoToModel(MonitorParkingVehicleRequestDTO dto);
}
