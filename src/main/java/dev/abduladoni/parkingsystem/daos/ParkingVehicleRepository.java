package dev.abduladoni.parkingsystem.daos;

import dev.abduladoni.parkingsystem.model.ParkingVehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParkingVehicleRepository extends JpaRepository<ParkingVehicle, Long> {
    Optional<ParkingVehicle> findByLicensePlateNumberAndStatus(String licensePlateNumber, String status);

    @Query("SELECT p.licensePlateNumber FROM ParkingVehicle p WHERE p.status = :status")
    List<String> findVehicleNumbersByStatus(final String status);
}
