package com.eiu.taskmanager.service;

import com.eiu.taskmanager.model.Task;
import com.eiu.taskmanager.repository.TaskRepository;

import com.eiu.taskmanager.dto.TaskRequestDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eiu.taskmanager.model.User;
import com.eiu.taskmanager.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service  // Marks this as a service component for Spring
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @Autowired  // Spring injects the TaskRepository automatically
    public TaskService(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    // ==============================
    // CREATE / SAVE TASK
    // ==============================

    public Task createTask(TaskRequestDTO taskDTO) {
    User owner = userRepository.findById(taskDTO.getOwnerId())
            .orElseThrow(() -> new RuntimeException("User not found with id " + taskDTO.getOwnerId()));

    Task task = new Task(
        taskDTO.getTitle(),
        taskDTO.getDescription(),
        taskDTO.getStatus(),
        taskDTO.getPriority(),
        taskDTO.getDueDate(),
        owner
    );

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
                task.setPriority(updatedTask.getPriority());
                task.setDueDate(updatedTask.getDueDate());
                task.setNotificationSent(updatedTask.isNotificationSent()); // <- important
                task.setOwner(updatedTask.getOwner()); // optional if owner can change
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

    // Optional: get tasks by priority

    public List<Task> getTasksByPriority(String priority){
    return taskRepository.findByPriority(priority);
    }

    // Optional: To get tasks based on who owns it 
    // (i.e tasks each user is in charge of)

    public List<Task> getTasksByOwnerId(Long ownerId) {
        return taskRepository.findByOwnerId(ownerId);
    }

}
