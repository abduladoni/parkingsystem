package dev.abduladoni.parkingsystem.service.parkingfee;

import dev.abduladoni.parkingsystem.model.ParkingVehicle;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
@Qualifier("sundayParkingFeeStrategy")
public class SundayParkingFeeStrategy implements ParkingFeeStrategy {


    @Override
    public BigDecimal calculateParkingFee(ParkingVehicle parkingVehicle, LocalDateTime startTime, LocalDateTime endTime) {
        return BigDecimal.ZERO; // On Sundays, the parking fee is zero
    }
}
