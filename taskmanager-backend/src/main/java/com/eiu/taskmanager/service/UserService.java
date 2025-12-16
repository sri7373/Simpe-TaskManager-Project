package com.eiu.taskmanager.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.eiu.taskmanager.model.User;
import com.eiu.taskmanager.repository.UserRepository;


@Service
public class UserService {

    private final UserRepository repo;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository repo, PasswordEncoder passwordEncoder) {
        this.repo = repo;
        this.passwordEncoder = passwordEncoder;
    }

    public User createUser(User u) {
        u.setPassword(passwordEncoder.encode(u.getPassword()));
        
        // Basic checks (could add email uniqueness check)
        return repo.save(u);
    }

    public User getUserByUsername(String username) {
        return repo.findByUsername(username)
               .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
    }
 
    public List<User> getAllUsers() {
        return repo.findAll();
    }

    public User getUser(Long id) {
        return repo.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public User updateUser(Long id, User updatedUser) {
        User existing = repo.findById(id)
            .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        
        // Update fields
        if (updatedUser.getUsername() != null) {
            existing.setUsername(updatedUser.getUsername());
        }
        if (updatedUser.getEmail() != null) {
            existing.setEmail(updatedUser.getEmail());
        }
        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
            existing.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }
        if (updatedUser.getRole() != null) {
            existing.setRole(updatedUser.getRole());
        }
        
        return repo.save(existing);
    }
    
    public void deleteUser(Long id) {
        if (!repo.existsById(id)) {
            throw new RuntimeException("User not found with id: " + id);
        }
        repo.deleteById(id);
    }
}
