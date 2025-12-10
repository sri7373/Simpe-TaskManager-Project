package com.eiu.taskmanager;

import com.eiu.taskmanager.service.NotificationService; // <<— import added
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class EmailTestRunner implements CommandLineRunner {

    private final NotificationService notificationService;

    public EmailTestRunner(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Override
    public void run(String... args) throws Exception {
        try {
            notificationService.sendEmail(
                "srividya.rs.2024@computing.smu.edu.sg", // recipient
                "Test Email from Task Manager",
                "This is a test email to verify SMTP configuration."
            );
            System.out.println("Test email sent successfully!");
        } catch (Exception e) {
            System.err.println("Failed to send test email.");
            e.printStackTrace();
        }
    }
}
