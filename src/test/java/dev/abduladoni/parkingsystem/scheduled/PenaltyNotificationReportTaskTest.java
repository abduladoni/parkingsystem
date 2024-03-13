package dev.abduladoni.parkingsystem.scheduled;

import dev.abduladoni.parkingsystem.daos.PenaltyNotificationRepository;
import dev.abduladoni.parkingsystem.model.PenaltyNotification;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.File;
import java.time.LocalDateTime;
import java.util.Collections;

import static org.mockito.Mockito.*;

class PenaltyNotificationReportTaskTest {

    @Mock
    private PenaltyNotificationRepository penaltyNotificationRepository;

    @InjectMocks
    private PenaltyNotificationReportTask penaltyNotificationReportTask;

    private String reportLocation = new File(getClass().getClassLoader()
            .getResource("").getFile()).getAbsolutePath();

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(penaltyNotificationReportTask, "reportLocation", reportLocation);
    }

    @AfterEach
    public void tearDown() {
        File reportDirectory = new File(reportLocation);
        for (File file : reportDirectory.listFiles()) {
            if (file.getName().startsWith("report_")) {
                file.delete();
            }
        }
    }

    @Test
    @DisplayName("Should generate report when there are penalty notifications")
    public void shouldGenerateReportWhenThereArePenaltyNotifications() {
        // Given
        PenaltyNotification penaltyNotification = new PenaltyNotification();
        penaltyNotification.setLicensePlateNumber("123ABC");
        penaltyNotification.setStreetName("Test Street");
        penaltyNotification.setPenaltySent(false);
        penaltyNotification.setDateTime(LocalDateTime.of(2024, 3, 9, 9, 0));

        when(penaltyNotificationRepository.findByPenaltySentFalse()).thenReturn(Collections.singletonList(penaltyNotification));

        // When
        penaltyNotificationReportTask.generateReport();
        // Then
        verify(penaltyNotificationRepository, times(1)).saveAll(anyList());
    }

    @Test
    @DisplayName("Should not generate report when there are no penalty notifications")
    public void shouldNotGenerateReportWhenThereAreNoPenaltyNotifications() {
        // Given
        when(penaltyNotificationRepository.findByPenaltySentFalse()).thenReturn(Collections.emptyList());

        // When
        penaltyNotificationReportTask.generateReport();

        // Then
        verify(penaltyNotificationRepository, times(0)).saveAll(anyList());
    }
}