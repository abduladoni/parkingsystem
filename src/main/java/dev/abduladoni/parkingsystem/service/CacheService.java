package dev.abduladoni.parkingsystem.service;

import dev.abduladoni.parkingsystem.model.ParkingTariffMetaData;
import java.util.Map;

public interface CacheService {
    Map<String, Integer> loadParkingTariffMetaData();
}
