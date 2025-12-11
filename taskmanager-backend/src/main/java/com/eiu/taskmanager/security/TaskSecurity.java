package com.eiu.taskmanager.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.eiu.taskmanager.model.Task;
import com.eiu.taskmanager.repository.TaskRepository;

@Component("taskSecurity") // name referenced in SpEL
public class TaskSecurity {

    private final TaskRepository taskRepository;

    @Autowired
    public TaskSecurity(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    /**
     * Checks if the given username is the owner of the task.
     *
     * @param taskId ID of the task
     * @param username the username of the currently authenticated user
     * @return true if user owns the task, false otherwise
     */
    public boolean isTaskOwner(Long taskId, String username) {
        return taskRepository.findById(taskId)
                .map(Task::getOwner)          // get owner User object
                .map(owner -> owner.getUsername().equals(username))
                .orElse(false);               // task not found -> deny access
    }
}
