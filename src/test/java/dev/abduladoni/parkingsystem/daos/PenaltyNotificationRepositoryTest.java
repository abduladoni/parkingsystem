package dev.abduladoni.parkingsystem.daos;

import dev.abduladoni.parkingsystem.model.PenaltyNotification;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class PenaltyNotificationRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private PenaltyNotificationRepository penaltyNotificationRepository;


    @Test
    @DisplayName("Should find all PenaltyNotifications not sent")
    public void shouldFindAllPenaltyNotificationsNotSent() {
        // Given
        PenaltyNotification penaltyNotification = new PenaltyNotification();
        penaltyNotification.setLicensePlateNumber("123ABC");
        penaltyNotification.setStreetName("Test Street");
        penaltyNotification.setPenaltySent(false);
        entityManager.persistAndFlush(penaltyNotification);

        // When
        List<PenaltyNotification> penaltyNotifications = penaltyNotificationRepository.findByPenaltySentFalse();

        // Then
        assertThat(penaltyNotifications).isNotEmpty();
        assertThat(penaltyNotifications.get(0).getLicensePlateNumber()).isEqualTo(penaltyNotification.getLicensePlateNumber());
        entityManager.clear();
    }

    @Test
    @DisplayName("Should return empty list when no PenaltyNotifications not sent")
    public void shouldReturnEmptyListWhenNoPenaltyNotificationsNotSent() {
        entityManager.clear();
        // When
        List<PenaltyNotification> penaltyNotifications = penaltyNotificationRepository.findByPenaltySentFalse();

        // Then
        assertThat(penaltyNotifications).isEmpty();
    }
}