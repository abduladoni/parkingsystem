package dev.abduladoni.parkingsystem.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "penalty_notification", indexes = {
        @Index(name = "idx_penalty_sent", columnList = "penalty_sent")
})
public class PenaltyNotification {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "license_plate_number")
    private String licensePlateNumber;

    @Column(name = "street_name")
    private String streetName;

    @Column(name = "date_time")
    private LocalDateTime dateTime;

    @Column(name = "penalty_sent")
    private Boolean penaltySent;

    @Column(name = "last_updated_ts")
    private LocalDateTime lastUpdatedTs;
}
