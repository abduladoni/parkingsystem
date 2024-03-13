package dev.abduladoni.parkingsystem.service.parkingfee;

import dev.abduladoni.parkingsystem.exception.InvalidParkingRequestException;
import dev.abduladoni.parkingsystem.model.ParkingVehicle;
import dev.abduladoni.parkingsystem.service.CacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Map;

@Component
@Qualifier("dailyParkingFeeStrategy")
public class DailyParkingFeeStrategy implements ParkingFeeStrategy {

    private static final Logger LOGGER = LoggerFactory.getLogger(DailyParkingFeeStrategy.class);
    private final CacheService cacheService;

    @Autowired
    public DailyParkingFeeStrategy(CacheService cacheService) {
        this.cacheService = cacheService;
    }
    @Override
    public BigDecimal calculateParkingFee(ParkingVehicle parkingVehicle, LocalDateTime startTime, LocalDateTime endTime) {
        Integer tariff = getTariff(parkingVehicle.getStreetName());
        long totalMinutes = calculateTotalMinutes(startTime, endTime);
        LOGGER.info("Calculated total minutes DailyParkingFeeStrategy for vehicle: {} from: {} to: {}",
                parkingVehicle.getLicensePlateNumber(), startTime, endTime);

        return BigDecimal.valueOf(totalMinutes * tariff).divide(BigDecimal.valueOf(100));
    }

    private Integer getTariff(String streetName) {
        Map<String, Integer> parkingTariffMetaData = cacheService.loadParkingTariffMetaData();
        Integer tariff = parkingTariffMetaData.get(streetName);

        if (tariff == null) {
            throw new InvalidParkingRequestException("No tariff found for street: " + streetName);
        }

        return tariff;
    }

    private long calculateTotalMinutes(LocalDateTime startTime, LocalDateTime endTime) {
        long totalMinutes = 0;

        while (startTime.isBefore(endTime)) {
            if ((startTime.toLocalTime().isAfter(LocalTime.of(7, 59))
                    && startTime.toLocalTime().isBefore(LocalTime.of(21, 0)))) {
                totalMinutes++;
            }
            startTime = startTime.plusMinutes(1);
        }

        return totalMinutes;
    }
}
