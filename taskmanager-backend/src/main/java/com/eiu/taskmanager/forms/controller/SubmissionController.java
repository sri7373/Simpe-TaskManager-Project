package com.eiu.taskmanager.forms.controller;

import com.eiu.taskmanager.forms.model.*;
import com.eiu.taskmanager.forms.service.AnswerService;
import com.eiu.taskmanager.forms.service.SubmissionService;
import com.eiu.taskmanager.forms.service.SurveyFormService;
import com.eiu.taskmanager.model.User;
import com.eiu.taskmanager.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/submissions")
public class SubmissionController {

    private final SubmissionService submissionService;
    private final AnswerService answerService;
    private final SurveyFormService surveyFormService;
    private final UserService userService;

    @Autowired
    public SubmissionController(SubmissionService submissionService,
                                AnswerService answerService,
                                SurveyFormService surveyFormService,
                                UserService userService) {
        this.submissionService = submissionService;
        this.answerService = answerService;
        this.surveyFormService = surveyFormService;
        this.userService = userService;
    }

    @PostMapping("/create/{formId}")
    public ResponseEntity<Submission> submitForm(@PathVariable UUID formId,
                                                 Authentication authentication,
                                                 @RequestBody List<Answer> answers) {
        String username = authentication.getName(); // Get username from Authentication
        User user = userService.getUserByUsername(username);
        SurveyForm form = surveyFormService.getFormById(formId);

        Submission submission = submissionService.createSubmission(form, user);

        for (Answer ans : answers) {
            ans.setSubmission(submission);
            answerService.createAnswer(submission, ans.getQuestion(), ans.getValue());
        }

        return ResponseEntity.ok(submission);
    }

    @GetMapping("/form/{formId}")
    public ResponseEntity<List<Submission>> getSubmissionsForForm(@PathVariable UUID formId) {
        SurveyForm form = surveyFormService.getFormById(formId);
        return ResponseEntity.ok(submissionService.getSubmissionsByForm(form));
    }

    @GetMapping("/{submissionId}")
    public ResponseEntity<Submission> getSubmission(@PathVariable UUID submissionId) {
        return ResponseEntity.ok(submissionService.getSubmissionById(submissionId));
    }
}