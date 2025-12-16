package com.eiu.taskmanager.forms.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.eiu.taskmanager.forms.model.Submission;
import com.eiu.taskmanager.model.User;

@Repository
public interface SubmissionRepository extends JpaRepository<Submission, UUID> {

    Optional<Submission> findBySurveyFormIdAndUser_Id(UUID surveyFormId, Long userId);

    List<Submission> findByUser(User user);

    List<Submission> findBySurveyFormId(UUID surveyFormId);

    long countBySurveyFormId(UUID surveyFormId);

    boolean existsBySurveyFormIdAndUser_Id(UUID surveyFormId, Long userId);
}
