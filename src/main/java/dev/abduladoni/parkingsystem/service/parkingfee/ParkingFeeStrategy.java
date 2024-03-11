package dev.abduladoni.parkingsystem.service.parkingfee;

import dev.abduladoni.parkingsystem.model.ParkingVehicle;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface ParkingFeeStrategy {
    BigDecimal calculateParkingFee(ParkingVehicle parkingVehicle, LocalDateTime startTime, LocalDateTime endTime);
}
