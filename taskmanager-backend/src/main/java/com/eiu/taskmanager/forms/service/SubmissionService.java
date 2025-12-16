package com.eiu.taskmanager.forms.service;

import java.util.List;
import java.util.UUID;

import com.eiu.taskmanager.forms.model.Submission;
import com.eiu.taskmanager.forms.model.SurveyForm;
import com.eiu.taskmanager.model.User;

import org.springframework.security.access.prepost.PreAuthorize;

@PreAuthorize("isAuthenticated()") // Anyone authenticated is authorized
public interface SubmissionService {

    Submission createSubmission(SurveyForm form, User respondent);

    List<Submission> getSubmissionsByForm(SurveyForm form);

    Submission getSubmissionById(UUID submissionId);

    boolean hasUserSubmittedForm(SurveyForm form, User user);

    void deleteSubmission(UUID submissionId, User currentUser);

}
