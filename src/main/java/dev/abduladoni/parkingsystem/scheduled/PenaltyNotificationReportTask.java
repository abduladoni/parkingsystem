package dev.abduladoni.parkingsystem.scheduled;

import dev.abduladoni.parkingsystem.daos.PenaltyNotificationRepository;
import dev.abduladoni.parkingsystem.model.PenaltyNotification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class PenaltyNotificationReportTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(PenaltyNotificationReportTask.class);
    private final PenaltyNotificationRepository penaltyNotificationRepository;

    @Autowired
    public PenaltyNotificationReportTask(PenaltyNotificationRepository penaltyNotificationRepository) {
        this.penaltyNotificationRepository = penaltyNotificationRepository;
    }

    @Value("${report.location}")
    private String reportLocation;

    @Scheduled(cron = "${report.cron}")
    public void generateReport() {
        List<PenaltyNotification> penaltyNotifications = penaltyNotificationRepository.findByPenaltySentFalse();

        if (!penaltyNotifications.isEmpty()) {
            String fileName = reportLocation + "/report_" + LocalDateTime.now().format(
                    DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + ".csv";

            LOGGER.info("Generating report: {} for records: {}", fileName, penaltyNotifications.size());

            try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
                writer.println("LicensePlateNumber,StreetName,DateTime");

                for (PenaltyNotification penaltyNotification : penaltyNotifications) {
                    writer.println(String.format("%s,%s,%s",
                            penaltyNotification.getLicensePlateNumber(),
                            penaltyNotification.getStreetName(),
                            penaltyNotification.getDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));

                    penaltyNotification.setPenaltySent(true);
                }

                penaltyNotificationRepository.saveAll(penaltyNotifications);
            } catch (IOException e) {
                LOGGER.error("Error occurred while generating report", e);
            }
        }else{
            LOGGER.info("No penalty notifications to report");
        }
    }
}