package dev.abduladoni.parkingsystem.daos;

import dev.abduladoni.parkingsystem.model.ParkingStatus;
import dev.abduladoni.parkingsystem.model.ParkingVehicle;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ParkingVehicleRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ParkingVehicleRepository parkingVehicleRepository;

    private ParkingVehicle parkingVehicle;

    @BeforeEach
    public void setUp() {
        parkingVehicle = new ParkingVehicle();
        parkingVehicle.setLicensePlateNumber("123ABC");
        parkingVehicle.setStatus(ParkingStatus.ACTIVE.getStatus());
        entityManager.persistAndFlush(parkingVehicle);
    }

    @AfterEach
    public void afterEach() {
        entityManager.clear();
    }

    @Test
    @DisplayName("Should find ParkingVehicle by license plate")
    public void shouldFindParkingVehicleByLicensePlate() {
        // When
        Optional<ParkingVehicle> foundVehicle = parkingVehicleRepository
                .findByLicensePlateNumberAndStatus(parkingVehicle.getLicensePlateNumber(),
                        ParkingStatus.ACTIVE.getStatus());
        // Then
        assertThat(foundVehicle).isPresent();
        assertThat(foundVehicle.get().getLicensePlateNumber()).isEqualTo(parkingVehicle.getLicensePlateNumber());
    }

    @Test
    @DisplayName("Should return empty when no ParkingVehicle found by license plate")
    public void shouldReturnEmptyWhenNoParkingVehicleFoundByLicensePlate() {
        // When
        Optional<ParkingVehicle> foundVehicle = parkingVehicleRepository
                .findByLicensePlateNumberAndStatus("NON_EXISTING_PLATE", ParkingStatus.ACTIVE.getStatus());
        // Then
        assertThat(foundVehicle).isNotPresent();
    }

    @Test
    @DisplayName("Should find all ParkingVehicle by status")
    public void shouldFindAllParkingVehicleByStatus() {
        // When
        // Then
        assertThat(parkingVehicleRepository.findVehicleNumbersByStatus(ParkingStatus.ACTIVE.getStatus()))
                .contains(parkingVehicle.getLicensePlateNumber());
    }
}