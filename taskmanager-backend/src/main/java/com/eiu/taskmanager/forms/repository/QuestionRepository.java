package com.eiu.taskmanager.forms.repository;

import com.eiu.taskmanager.forms.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface QuestionRepository extends JpaRepository<Question, UUID> {

    // Return Optional<Integer> so that .orElse() works
    @Query("SELECT MAX(q.displayOrder) FROM Question q WHERE q.surveyForm.id = :formId")
    Optional<Integer> findMaxDisplayOrderByFormId(@Param("formId") UUID formId);
    // to ensure that new qn appears at the end, after all existing qns

    // Return questions in display order
    List<Question> findBySurveyFormIdOrderByDisplayOrderAsc(UUID surveyFormId);
}
