package com.eiu.taskmanager.forms.controller;

import com.eiu.taskmanager.forms.model.SurveyForm;
import com.eiu.taskmanager.forms.service.SurveyFormService;
import com.eiu.taskmanager.model.User;
import com.eiu.taskmanager.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/forms")
public class SurveyFormController {

    private final SurveyFormService surveyFormService;
    private final UserService userService;

    @Autowired
    public SurveyFormController(SurveyFormService surveyFormService, UserService userService) {
        this.surveyFormService = surveyFormService;
        this.userService = userService;
    }

    @PostMapping("/create")
    public ResponseEntity<SurveyForm> createForm(@RequestBody SurveyForm form,
                                                 Authentication authentication) {
        String username = authentication.getName(); 
        User creator = userService.getUserByUsername(username);
        SurveyForm created = surveyFormService.createForm(form, creator);
        return ResponseEntity.ok(created);
    }

    @GetMapping
    public ResponseEntity<List<SurveyForm>> getAllActiveForms() {
        return ResponseEntity.ok(surveyFormService.getAllActiveForms());
    }

    @GetMapping("/creator")
    public ResponseEntity<List<SurveyForm>> getFormsByCreator(Authentication authentication) {
        String username = authentication.getName();
        User creator = userService.getUserByUsername(username);
        return ResponseEntity.ok(surveyFormService.getFormsByCreator(creator));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SurveyForm> getForm(@PathVariable UUID id) {
        return ResponseEntity.ok(surveyFormService.getFormById(id));
    }

    @PatchMapping("/{id}/active-switch")
    public ResponseEntity<Void> toggleActive(@PathVariable UUID id,
                                             Authentication authentication) {
        String username = authentication.getName();
        User requester = userService.getUserByUsername(username);
        surveyFormService.softDeleteForm(id, requester);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/expire")
    public ResponseEntity<Void> expireForm(@PathVariable UUID id) {
        surveyFormService.expireForm(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{formId}")
    public ResponseEntity<Void> deleteSurveyForm(@PathVariable UUID formId) {

        // ✅ Use the service to get current user safely
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userService.getUserByUsername(username);

        surveyFormService.deleteSurveyForm(formId, currentUser);
        return ResponseEntity.noContent().build();
    }
}
