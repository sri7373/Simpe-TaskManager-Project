package com.eiu.taskmanager.forms.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.eiu.taskmanager.forms.model.Answer;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, UUID> {

    // Find answers by submission ID
    List<Answer> findBySubmissionId(UUID submissionId);

    // Find answers by survey form ID (traverse through submission -> surveyForm)
    @Query("SELECT a FROM Answer a WHERE a.submission.surveyForm.id = :formId")
    List<Answer> findBySurveyFormId(@Param("formId") UUID surveyFormId);

    // Find answers by question ID
    List<Answer> findByQuestionId(UUID questionId);
}
