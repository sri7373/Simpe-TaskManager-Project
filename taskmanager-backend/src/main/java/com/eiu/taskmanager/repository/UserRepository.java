package com.eiu.taskmanager.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eiu.taskmanager.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    List<User> findByRole(String role);
    Optional<User> findByUsername(String username);

}

// camel Case for code
// snake Case for db

// Use solid principles and segregate code with interfaces more