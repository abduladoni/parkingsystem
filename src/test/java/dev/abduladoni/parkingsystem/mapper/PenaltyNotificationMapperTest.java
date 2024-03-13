package dev.abduladoni.parkingsystem.mapper;

import dev.abduladoni.parkingsystem.dto.MonitorParkingVehicleRequestDTO;
import dev.abduladoni.parkingsystem.model.PenaltyNotification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PenaltyNotificationMapperTest {

    private PenaltyNotificationMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(PenaltyNotificationMapper.class);
    }

    @Test
    @DisplayName("Should map DTO to Model correctly")
    void shouldMapDtoToModelCorrectly() {
        MonitorParkingVehicleRequestDTO dto = MonitorParkingVehicleRequestDTO.  builder()
                .vehicleNumber("123ABC").streetName("Test Street").observationTime("2024-03-09 09:00:00").build();

        PenaltyNotification model = mapper.dtoToModel(dto);

        assertEquals("123ABC", model.getLicensePlateNumber());
        assertEquals("Test Street", model.getStreetName());
        assertEquals("2024-03-09T09:00", model.getDateTime().toString());

    }
}