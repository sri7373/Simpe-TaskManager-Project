package com.eiu.taskmanager.controller;

import com.eiu.taskmanager.model.Task;
import com.eiu.taskmanager.service.TaskService;

import com.eiu.taskmanager.dto.TaskRequestDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks") // Base URL for all task endpoints
public class TaskController {

    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    } // dependency injection to prevent tightly coupled code

    // ==============================
    // CREATE TASK
    // ==============================

    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody TaskRequestDTO taskDTO) {
        Task createdTask = taskService.createTask(taskDTO);
        return ResponseEntity.ok(createdTask);
    } 

    // ==============================
    // GET ALL TASKS
    // ==============================
    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks() {
        List<Task> tasks = taskService.getAllTasks();
        return ResponseEntity.ok(tasks);
    }

    // ==============================
    // GET TASK BY ID
    // ==============================
    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        return taskService.getTaskById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ==============================
    // UPDATE TASK
    // ==============================
    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody Task task) {
        try {
            // Optional: keep default if not provided
            if (task.getPriority() == null) task.setPriority("Medium");

            Task updatedTask = taskService.updateTask(id, task);
            return ResponseEntity.ok(updatedTask);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ==============================
    // DELETE TASK
    // ==============================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        try {
            taskService.deleteTask(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ==============================
    // GET TASKS BY STATUS
    // ==============================
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Task>> getTasksByStatus(@PathVariable String status) {
        List<Task> tasks = taskService.getTasksByStatus(status);
        return ResponseEntity.ok(tasks);
    }

    // GET tasks by priority
    @GetMapping("/priority/{priority}")
    public ResponseEntity<List<Task>> getTasksByPriority(@PathVariable String priority){
        List<Task> tasks = taskService.getTasksByPriority(priority);
        return ResponseEntity.ok(tasks);
    }

    // New endpoint: Get tasks by owner ID
    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<List<Task>> getTasksByOwner(@PathVariable Long ownerId) {
        List<Task> tasks = taskService.getTasksByOwnerId(ownerId);
        return ResponseEntity.ok(tasks);
    }
}
