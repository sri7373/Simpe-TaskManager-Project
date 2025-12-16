package com.eiu.taskmanager.forms.service;

import java.util.List;
import java.util.UUID;

import com.eiu.taskmanager.forms.model.SurveyForm;
import com.eiu.taskmanager.model.User;

import org.springframework.security.access.prepost.PreAuthorize;

@PreAuthorize("isAuthenticated()") // Anyone authenticated is authorized
public interface SurveyFormService {

    SurveyForm createForm(SurveyForm form, User creator);

    SurveyForm getFormById(UUID formId);

    List<SurveyForm> getAllActiveForms();

    List<SurveyForm> getFormsByCreator(User creator);

    SurveyForm updateForm(UUID formId, SurveyForm updatedForm, User requester);

    // soft delete (using inactive, dont actually delete from db, just keep it as inactive)
    
    void softDeleteForm(UUID formId, User requester);

    void expireForm(UUID formId);
}
