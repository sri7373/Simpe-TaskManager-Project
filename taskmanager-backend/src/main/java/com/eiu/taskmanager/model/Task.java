package com.eiu.taskmanager.model;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDate;

import jakarta.persistence.*;

@Entity                       // Marks this class as a JPA entity
@Table(name = "Tasks")          // Maps to the "Tasks" table in TaskManagerDB
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increment primary key
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(length = 500)
    private String description;

    @Column(nullable = false)
    private String status; // e.g., "Pending", "In Progress", "Done"

    // New fields
    // ========================

    @Column(name = "Priority", nullable = false)
    private String priority = "Medium"; // default value given

    @Column(name = "DueDate")
    private LocalDate dueDate; // can be null initially

    // Link to User
    @ManyToOne
    @JoinColumn(name = "user_id") // column in tasks table
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) // prevent infinite loop in JSON
    private User owner;

    // ==========================
    // Constructors
    // ==========================
    public Task() {} // Default constructor required by JPA

    public Task(String title, String description, String status, String priority, LocalDate dueDate, User owner) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.priority = priority;
        this.dueDate = dueDate;
        this.owner = owner;
    }

    // ==========================
    // Getters and Setters
    // ==========================
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }

    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    // Optional: toString() for debugging
    @Override

    public String toString() {
        return "Task{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", priority='" + priority + '\'' +
                ", dueDate=" + dueDate +
                ", owner=" + (owner != null ? owner.getUsername() : null) +
                '}';
    }

}
