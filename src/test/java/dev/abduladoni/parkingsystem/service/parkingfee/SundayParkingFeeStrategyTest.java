package dev.abduladoni.parkingsystem.service.parkingfee;

import dev.abduladoni.parkingsystem.model.ParkingVehicle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SundayParkingFeeStrategyTest {

    @Mock
    private ParkingVehicle parkingVehicle;

    @InjectMocks
    private SundayParkingFeeStrategy sundayParkingFeeStrategy;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Calculate parking fee on Sunday")
    public void calculateParkingFeeOnSunday() {
        BigDecimal result = sundayParkingFeeStrategy.calculateParkingFee(parkingVehicle,
                LocalDateTime.of(2024, 3, 10, 9, 0),
                LocalDateTime.of(2024, 3, 10, 10, 0));

        assertEquals(BigDecimal.ZERO, result);
    }

    @Test
    @DisplayName("Calculate parking fee on Non Sunday")
    public void calculateParkingFeeOnNonSunday() {
        BigDecimal result = sundayParkingFeeStrategy.calculateParkingFee(parkingVehicle,
                LocalDateTime.of(2024, 3, 11, 9, 0),
                LocalDateTime.of(2024, 3, 11, 10, 0));

        assertEquals(BigDecimal.ZERO, result);
    }
}