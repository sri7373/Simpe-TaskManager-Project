package com.eiu.taskmanager.forms.model;

import com.eiu.taskmanager.model.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@Table(name = "survey_forms")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})  // CRITICAL: Ignore Hibernate proxy stuff
// as json doesnt understand the extra internal fields
public class SurveyForm {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    @JsonIgnoreProperties({"tasks", "password"})  // Don't serialize tasks and password, to not leak passwords and prevent infinite recursion
    private User owner;

    @OneToMany(mappedBy = "surveyForm", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("surveyForm")  // Prevent circular reference, spiralling between surveyForm and qn
    private List<Question> questions;

    @Column(name = "expiry_date")
    private LocalDateTime expiryDate;

    @Column(name = "is_active")
    private boolean active = true;

    @Column(name = "is_deleted")
    private boolean deleted = false;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Optional "creator" concept for convenience, mainly for the json
    // support diff naming conventions without changing db structure
    @Transient
    public User getCreator() {
        return owner;
    }

    @Transient
    public void setCreator(User creator) {
        this.owner = creator;
    }

    @Transient
    public LocalDateTime getExpiresAt() {
        return expiryDate;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiryDate = expiresAt;
    }
}