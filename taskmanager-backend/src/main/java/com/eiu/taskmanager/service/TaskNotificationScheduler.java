package com.eiu.taskmanager.service;

import com.eiu.taskmanager.model.Task;
import com.eiu.taskmanager.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class TaskNotificationScheduler {

    private final TaskService taskService;
    private final NotificationService notificationService;

    @Autowired
    public TaskNotificationScheduler(TaskService taskService, NotificationService notificationService) {
        this.taskService = taskService;
        this.notificationService = notificationService;
    }

    // Run every 5 minutes
    @Scheduled(cron = "0 */5 * * * ?")
    public void sendDueDateNotifications() {
        List<Task> tasks = taskService.getAllTasks();
        LocalDate tomorrow = LocalDate.now().plusDays(1);

    for (Task task : tasks) {
        if (task.getDueDate() != null
                && task.getDueDate().equals(tomorrow)
                && !task.isNotificationSent()
                && task.getOwner() != null) {

            String to = task.getOwner().getEmail();
            String subject = "Task Due Tomorrow: " + task.getTitle();
            String body = "Hello " + task.getOwner().getUsername() + ",\n\n" +
                    "This is a reminder that your task is due tomorrow (" + task.getDueDate() + ").\n\n" +
                    "Task Details:\n" +
                    "Title: " + task.getTitle() + "\n" +
                    "Description: " + task.getDescription() + "\n" +
                    "Priority: " + task.getPriority() + "\n\n" +
                    "Please make sure to complete it on time.\n\n" +
                    "Best regards,\nTask Manager";

            try {
                // Try sending email
                notificationService.sendEmail(to, subject, body);

                // Only mark as sent if email succeeded
                task.setNotificationSent(true);
                taskService.updateTask(task.getId(), task);

                System.out.println("Notification sent to " + to + " for task " + task.getTitle());

            } catch (Exception e) {
                // Log error, do NOT mark as sent
                System.err.println("Failed to send notification to " + to + " for task " + task.getTitle());
                e.printStackTrace();
            }
        }
    }
}

}
