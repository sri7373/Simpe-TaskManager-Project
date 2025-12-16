package com.eiu.taskmanager.forms.service.impl;

import com.eiu.taskmanager.forms.model.Submission;
import com.eiu.taskmanager.forms.model.SurveyForm;
import com.eiu.taskmanager.model.User;
import com.eiu.taskmanager.forms.repository.SubmissionRepository;
import com.eiu.taskmanager.forms.service.SubmissionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class SubmissionServiceImpl implements SubmissionService {

    private final SubmissionRepository submissionRepository;

    @Autowired
    public SubmissionServiceImpl(SubmissionRepository submissionRepository) {
        this.submissionRepository = submissionRepository;
    }

    @Override
    public Submission createSubmission(SurveyForm form, User user) {

        if (hasUserSubmittedForm(form, user)) {
            throw new RuntimeException("User has already submitted this form");
        }

        Submission submission = new Submission();
        submission.setSurveyForm(form);
        submission.setRespondent(user);
        submission.setSubmittedAt(LocalDateTime.now());

        return submissionRepository.save(submission);
    }

    @Override
    public List<Submission> getSubmissionsByForm(SurveyForm form) {
        return submissionRepository.findBySurveyFormId(form.getId());
    }

    @Override
    public Submission getSubmissionById(UUID submissionId) {
        return submissionRepository.findById(submissionId)
                .orElseThrow(() -> new RuntimeException("Submission not found"));
    }

    @Override
    public boolean hasUserSubmittedForm(SurveyForm form, User user) {

        return submissionRepository
                .existsBySurveyFormIdAndUser_Id(form.getId(), user.getId());
    }

    @Override
@Transactional
public void deleteSubmission(UUID submissionId, User currentUser) {

    Submission submission = submissionRepository
            .findById(submissionId)
            .orElseThrow(() -> new RuntimeException("Submission not found"));

    boolean isSubmitter = submission.getUser().getId().equals(currentUser.getId());
    boolean isFormOwner = submission.getSurveyForm()
                                    .getOwner()
                                    .getId()
                                    .equals(currentUser.getId());

    if (!isSubmitter && !isFormOwner) {
        throw new RuntimeException("You are not allowed to delete this submission");
    }

    submissionRepository.delete(submission);
}

}
