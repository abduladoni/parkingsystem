package dev.abduladoni.parkingsystem.service.parkingfee;

import dev.abduladoni.parkingsystem.model.ParkingVehicle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Qualifier;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class ParkingFeeHelperImplTest {

    @Mock
    @Qualifier("dailyParkingFeeStrategy")
    private ParkingFeeStrategy dailyParkingFeeStrategy;

    @Mock
    @Qualifier("sundayParkingFeeStrategy")
    private ParkingFeeStrategy sundayParkingFeeStrategy;

    @InjectMocks
    private ParkingFeeHelperImpl parkingFeeHelper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Calculate parking fee for Non Sunday daily parking")
    void calculateParkingFeeForDailyParking() {
        ParkingVehicle parkingVehicle = new ParkingVehicle();
        parkingVehicle.setStartTime(LocalDateTime.now().minusHours(2));
        LocalDateTime endTime = LocalDateTime.now();

        when(dailyParkingFeeStrategy.calculateParkingFee(any(ParkingVehicle.class), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(BigDecimal.valueOf(20));

        BigDecimal result = parkingFeeHelper.calculateParkingFee(parkingVehicle);

        assertEquals(BigDecimal.valueOf(20), result);
    }

    @Test
    @DisplayName("Calculate parking fee for Sunday parking")
    void testCalculateParkingFeeForSundayParking() {
        //Given
        ParkingVehicle parkingVehicle = new ParkingVehicle();
        parkingVehicle.setStartTime(LocalDateTime.now().with(DayOfWeek.SUNDAY));

        when(sundayParkingFeeStrategy.calculateParkingFee(parkingVehicle, parkingVehicle.getStartTime(), LocalDateTime.now()))
                .thenReturn(BigDecimal.valueOf(0));

        //When
        BigDecimal result = parkingFeeHelper.calculateParkingFee(parkingVehicle);

        //Then
        assertEquals(BigDecimal.ZERO, result);
    }

}