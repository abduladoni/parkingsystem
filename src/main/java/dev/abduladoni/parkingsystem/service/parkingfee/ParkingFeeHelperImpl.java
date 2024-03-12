package dev.abduladoni.parkingsystem.service.parkingfee;

import dev.abduladoni.parkingsystem.model.ParkingVehicle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Component
public class ParkingFeeHelperImpl implements ParkingFeeHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(ParkingFeeHelperImpl.class);
    @Qualifier("dailyParkingFeeStrategy")
    private final ParkingFeeStrategy dailyParkingFeeStrategy;
    @Qualifier("sundayParkingFeeStrategy")
    private final ParkingFeeStrategy sundayParkingFeeStrategy;

    @Autowired
    public ParkingFeeHelperImpl(final ParkingFeeStrategy dailyParkingFeeStrategy,
                                final ParkingFeeStrategy sundayParkingFeeStrategy) {
        this.dailyParkingFeeStrategy = dailyParkingFeeStrategy;
        this.sundayParkingFeeStrategy = sundayParkingFeeStrategy;
    }

    @Override
    public BigDecimal calculateParkingFee(ParkingVehicle parkingVehicle) {
        LocalDateTime startTime = parkingVehicle.getStartTime();
        LocalDateTime endTime = parkingVehicle.getEndTime();

        BigDecimal totalFee = BigDecimal.ZERO;
        while (startTime.isBefore(endTime)) {
            LocalDateTime nextDay = startTime.plusDays(1).truncatedTo(ChronoUnit.DAYS);
            if (nextDay.isAfter(endTime)) {
                nextDay = endTime;
            }

            ParkingFeeStrategy parkingFeeStrategy;
            if (startTime.getDayOfWeek() == DayOfWeek.SUNDAY) {
                parkingFeeStrategy = sundayParkingFeeStrategy;
            } else {
                parkingFeeStrategy = dailyParkingFeeStrategy;
            }

            totalFee = totalFee.add(parkingFeeStrategy.calculateParkingFee(parkingVehicle, startTime, nextDay));

            if (nextDay.isAfter(endTime)) {
                startTime = endTime;
            } else {
                startTime = nextDay;
            }
        }

        return totalFee;
    }

}
