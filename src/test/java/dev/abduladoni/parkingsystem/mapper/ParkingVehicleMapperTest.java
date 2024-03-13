package dev.abduladoni.parkingsystem.mapper;

import dev.abduladoni.parkingsystem.dto.ParkingVehicleRequestDTO;
import dev.abduladoni.parkingsystem.model.ParkingVehicle;
import dev.abduladoni.parkingsystem.model.ParkingStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ParkingVehicleMapperTest {

    private ParkingVehicleMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(ParkingVehicleMapper.class);
    }

    @Test
    @DisplayName("Should map DTO to Model correctly")
    void shouldMapDtoToModelCorrectly() {
        // Given
        ParkingVehicleRequestDTO dto = ParkingVehicleRequestDTO.builder()
                .vehicleNumber("123ABC").streetName("Test Street").build();

        // When
        ParkingVehicle model = mapper.dtoToModel(dto);

        // Then
        assertEquals("123ABC", model.getLicensePlateNumber());
        assertEquals("Test Street", model.getStreetName());
        assertEquals(ParkingStatus.ACTIVE.getStatus(), model.getStatus());
    }

    @Test
    @DisplayName("Should set current time as start time and last updated timestamp")
    void shouldSetCurrentTimeAsStartTimeAndLastUpdatedTimestamp() {
        // Given
        ParkingVehicleRequestDTO dto = ParkingVehicleRequestDTO.builder()
                .vehicleNumber("123ABC").streetName("Test Street").build();

        // When
        ParkingVehicle model = mapper.dtoToModel(dto);
        // Then
        LocalDateTime now = LocalDateTime.now();
        assertEquals(now.getYear(), model.getStartTime().getYear());
        assertEquals(now.getMonth(), model.getStartTime().getMonth());
        assertEquals(now.getDayOfMonth(), model.getStartTime().getDayOfMonth());
        assertEquals(now.getHour(), model.getStartTime().getHour());
        assertEquals(now.getMinute(), model.getStartTime().getMinute());

        assertEquals(now.getYear(), model.getLastUpdatedTs().getYear());
        assertEquals(now.getMonth(), model.getLastUpdatedTs().getMonth());
        assertEquals(now.getDayOfMonth(), model.getLastUpdatedTs().getDayOfMonth());
        assertEquals(now.getHour(), model.getLastUpdatedTs().getHour());
        assertEquals(now.getMinute(), model.getLastUpdatedTs().getMinute());
    }
}