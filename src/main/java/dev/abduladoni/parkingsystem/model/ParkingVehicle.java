package dev.abduladoni.parkingsystem.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.Index;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "parking_vehicle", indexes = {
        @Index(name = "idx_license_plate_number", columnList = "license_plate_number"),
        @Index(name = "idx_status", columnList = "status")
})
public class ParkingVehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "license_plate_number")
    private String licensePlateNumber;

    @Column(name = "street_name")
    private String streetName;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Column(name = "status")
    private String status;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "last_updated_ts")
    private LocalDateTime lastUpdatedTs;
}