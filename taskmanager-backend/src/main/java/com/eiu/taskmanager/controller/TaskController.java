package com.eiu.taskmanager.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eiu.taskmanager.dto.TaskRequestDTO;
import com.eiu.taskmanager.model.Task;
import com.eiu.taskmanager.service.TaskService;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    // ==============================
    // CREATE TASK -> Admin only
    // ==============================
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Task> createTask(@RequestBody TaskRequestDTO taskDTO) {
        Task createdTask = taskService.createTask(taskDTO);
        return ResponseEntity.ok(createdTask);
    }

    // ==============================
    // GET ALL TASKS -> Admin/Analyst see all, User sees only their own
    // ==============================
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','ANALYST','USER')")
    public ResponseEntity<List<Task>> getAllTasks(Authentication authentication) {
        
        String username = authentication.getName();
        boolean isAdminOrAnalyst = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN") || 
                               a.getAuthority().equals("ROLE_ANALYST"));

        List<Task> tasks;
        if (isAdminOrAnalyst) {
            tasks = taskService.getAllTasks();
        } else {
            tasks = taskService.getAllTasks().stream()
                    .filter(task -> task.getOwner().getUsername().equals(username))
                    .collect(Collectors.toList());
        }
        
        return ResponseEntity.ok(tasks);
    }

    // ==============================
    // GET TASK BY ID -> Admin, Analyst, or owner (USER)
    // ==============================
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','ANALYST') or @taskSecurity.isTaskOwner(#id, authentication.name)")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        return taskService.getTaskById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ==============================
    // UPDATE TASK -> Admin only
    // ==============================
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateTask(@PathVariable Long id, @RequestBody Task task) {
        try {
            if (task.getPriority() == null) task.setPriority("Medium");
            Task updatedTask = taskService.updateTask(id, task);
            return ResponseEntity.ok(updatedTask);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ==============================
    // DELETE TASK -> Admin only
    // ==============================
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteTask(@PathVariable Long id) {
        try {
            taskService.deleteTask(id);
            return ResponseEntity.ok(Map.of("message", "Task deleted successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ==============================
    // GET TASKS BY STATUS -> Admin/Analyst see all, User sees only their own
    // ==============================
    @GetMapping("/status/{status}")
    @PreAuthorize("hasAnyRole('ADMIN','ANALYST','USER')")
    public ResponseEntity<List<Task>> getTasksByStatus(@PathVariable String status, 
                                                       Authentication authentication) {

        String username = authentication.getName();
        boolean isAdminOrAnalyst = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN") || 
                               a.getAuthority().equals("ROLE_ANALYST"));

        List<Task> tasks;
        if (isAdminOrAnalyst) {
            tasks = taskService.getTasksByStatus(status);
        } else {
            tasks = taskService.getTasksByStatus(status).stream()
                    .filter(task -> task.getOwner().getUsername().equals(username))
                    .collect(Collectors.toList());
        }
        
        return ResponseEntity.ok(tasks);
    }

    // ==============================
    // GET TASKS BY PRIORITY -> Admin/Analyst see all, User sees only their own
    // ==============================
    @GetMapping("/priority/{priority}")
    @PreAuthorize("hasAnyRole('ADMIN','ANALYST','USER')")
    public ResponseEntity<List<Task>> getTasksByPriority(@PathVariable String priority,
                                                         Authentication authentication) {

        String username = authentication.getName();
        boolean isAdminOrAnalyst = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN") || 
                               a.getAuthority().equals("ROLE_ANALYST"));

        List<Task> tasks;
        if (isAdminOrAnalyst) {
            tasks = taskService.getTasksByPriority(priority);
        } else {
            tasks = taskService.getTasksByPriority(priority).stream()
                    .filter(task -> task.getOwner().getUsername().equals(username))
                    .collect(Collectors.toList());
        }
        
        return ResponseEntity.ok(tasks);
    }

    // ==============================
    // GET TASKS BY OWNER -> Admin and Analysts only
    // ==============================
    @GetMapping("/owner/{ownerId}")
    @PreAuthorize("hasAnyRole('ADMIN','ANALYST')")
    public ResponseEntity<List<Task>> getTasksByOwner(@PathVariable Long ownerId) {
        List<Task> tasks = taskService.getTasksByOwnerId(ownerId);
        return ResponseEntity.ok(tasks);
    }
}
