package com.eiu.taskmanager.service;

import com.eiu.taskmanager.model.Task;
import com.eiu.taskmanager.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service  // Marks this as a service component for Spring
public class TaskService {

    private final TaskRepository taskRepository;

    @Autowired  // Spring injects the TaskRepository automatically
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    // ==============================
    // CREATE / SAVE TASK
    // ==============================
    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    // ==============================
    // GET ALL TASKS
    // ==============================
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    // ==============================
    // GET TASK BY ID
    // ==============================
    public Optional<Task> getTaskById(Long id) {
        return taskRepository.findById(id);
    }

    // ==============================
    // UPDATE TASK
    // ==============================
    public Task updateTask(Long id, Task updatedTask) {
        return taskRepository.findById(id)
                .map(task -> {
                    task.setTitle(updatedTask.getTitle());
                    task.setDescription(updatedTask.getDescription());
                    task.setStatus(updatedTask.getStatus());
                    return taskRepository.save(task);
                })
                .orElseThrow(() -> new RuntimeException("Task not found with id " + id));
    }

    // ==============================
    // DELETE TASK
    // ==============================
    public void deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new RuntimeException("Task not found with id " + id);
        }
        taskRepository.deleteById(id);
    }

    // ==============================
    // OPTIONAL: CUSTOM BUSINESS LOGIC
    // ==============================
    // Example: get tasks by status
    public List<Task> getTasksByStatus(String status) {
        return taskRepository.findByStatus(status); // Only works if you add this method in TaskRepository
    }
}
