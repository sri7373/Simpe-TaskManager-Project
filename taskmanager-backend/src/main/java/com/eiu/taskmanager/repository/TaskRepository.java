package com.eiu.taskmanager.repository;

import com.eiu.taskmanager.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository  // Marks this interface as a Spring Data repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    // jpa repo automatically has all the required CRUD stuff

    // Example of a custom query method:
    // Find all tasks by their status (Pending, In Progress, Done)
    @Query
    List<Task> findByStatus(String status);

    @Query
    List<Task> findByPriority(String priority);

    // You can add more query methods as needed, Spring Data generates the SQL automatically
}

