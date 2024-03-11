package dev.abduladoni.parkingsystem.service.parkingfee;

import dev.abduladoni.parkingsystem.model.ParkingVehicle;

import java.math.BigDecimal;

public interface ParkingFeeHelper {

    BigDecimal calculateParkingFee(ParkingVehicle parkingVehicle);
}
