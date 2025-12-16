package com.eiu.taskmanager.forms.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eiu.taskmanager.forms.model.Question;
import com.eiu.taskmanager.forms.model.QuestionOption;
import com.eiu.taskmanager.forms.model.SurveyForm;
import com.eiu.taskmanager.forms.repository.SurveyFormRepository;
import com.eiu.taskmanager.forms.service.SurveyFormService;
import com.eiu.taskmanager.model.User;

@Service
@Transactional
public class SurveyFormServiceImpl implements SurveyFormService {

    private final SurveyFormRepository surveyFormRepository;

    @Autowired
    public SurveyFormServiceImpl(SurveyFormRepository surveyFormRepository) {
        this.surveyFormRepository = surveyFormRepository;
    }

    @Override
    public SurveyForm createForm(SurveyForm form, User owner) {
        form.setOwner(owner);
        form.setCreatedAt(LocalDateTime.now());
        form.setUpdatedAt(LocalDateTime.now());
        form.setActive(true);

        if (form.getQuestions() != null) {
            for (Question question : form.getQuestions()) {
                // Set the parent form reference
                question.setSurveyForm(form);
                
                // Set bidirectional relationship for options
                if (question.getOptions() != null) {
                    for (QuestionOption option : question.getOptions()) {
                        option.setQuestion(question);
                    }
                }
            }
        }

        return surveyFormRepository.save(form);
    }

    @Override
    public SurveyForm getFormById(UUID formId) {
        return surveyFormRepository.findById(formId)
                .orElseThrow(() -> new RuntimeException("Form not found"));
    }

    @Override
    public List<SurveyForm> getAllActiveForms() {
        LocalDateTime now = LocalDateTime.now();
        List<SurveyForm> result = new ArrayList<>();

        result.addAll(
                surveyFormRepository.findByActiveTrueAndExpiryDateAfter(now)
        );
        result.addAll(
                surveyFormRepository.findByActiveTrueAndExpiryDateIsNull()
        );

        return result;
    }

    @Override
    public List<SurveyForm> getFormsByCreator(User owner) {
        return surveyFormRepository.findByOwner(owner);
    }

    @Override
    public SurveyForm updateForm(UUID formId, SurveyForm updatedForm, User requester) {
        SurveyForm existingForm = getFormById(formId);

        if (!existingForm.getOwner().getId().equals(requester.getId())) {
            throw new RuntimeException("Unauthorized: Only the creator can update this form");
        }

        existingForm.setTitle(updatedForm.getTitle());
        existingForm.setDescription(updatedForm.getDescription());
        existingForm.setExpiryDate(updatedForm.getExpiryDate());
        existingForm.setUpdatedAt(LocalDateTime.now());

        return surveyFormRepository.save(existingForm);
    }

    @Override
    public void softDeleteForm(UUID formId, User requester) {
        SurveyForm existingForm = getFormById(formId);

        if (!existingForm.getOwner().getId().equals(requester.getId())) {
            throw new RuntimeException("Unauthorized: Only the creator can delete this form");
        }

        existingForm.setActive(false);
        existingForm.setUpdatedAt(LocalDateTime.now());
        surveyFormRepository.save(existingForm);
    }

    @Override
    public void expireForm(UUID formId) {
        SurveyForm existingForm = getFormById(formId);
        existingForm.setExpiryDate(LocalDateTime.now());
        existingForm.setUpdatedAt(LocalDateTime.now());
        surveyFormRepository.save(existingForm);
    }
}