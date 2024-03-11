package dev.abduladoni.parkingsystem.daos;

import dev.abduladoni.parkingsystem.model.ParkingTariffMetaData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParkingTariffMetaDataRepository extends JpaRepository<ParkingTariffMetaData, Long> {
    List<ParkingTariffMetaData> findAll();
}
