package com.eiu.taskmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling; // <-- THIS WAS MISSING

@SpringBootApplication  // Marks this as the main Spring Boot app
@EnableScheduling       // Enables scheduling for @Scheduled tasks
public class TaskManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaskManagerApplication.class, args); // Starts the app
    }
}
