package dev.abduladoni.parkingsystem.daos;

import dev.abduladoni.parkingsystem.model.PenaltyNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PenaltyNotificationRepository extends JpaRepository<PenaltyNotification, Long> {
    List<PenaltyNotification> findByPenaltySentFalse();
}
