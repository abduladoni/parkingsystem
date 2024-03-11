package dev.abduladoni.parkingsystem.service;

import dev.abduladoni.parkingsystem.daos.ParkingTariffMetaDataRepository;
import dev.abduladoni.parkingsystem.model.ParkingTariffMetaData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CacheServiceImpl implements CacheService {

    @Autowired
    private ParkingTariffMetaDataRepository parkingTariffMetaDataRepository;

    @PostConstruct
    @Cacheable("parkingTariffMetaData")
    public Map<String, Integer> loadParkingTariffMetaData() {
        List<ParkingTariffMetaData> parkingTariffMetaDataList = parkingTariffMetaDataRepository.findAll();
        return parkingTariffMetaDataList.stream()
                .collect(Collectors.toMap(ParkingTariffMetaData::getStreetName, ParkingTariffMetaData::getTariffInCents));
    }
}
