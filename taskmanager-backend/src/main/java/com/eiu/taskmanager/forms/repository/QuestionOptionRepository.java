package com.eiu.taskmanager.forms.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.eiu.taskmanager.forms.model.QuestionOption;

@Repository
public interface QuestionOptionRepository extends JpaRepository<QuestionOption, UUID> {
    // Fetch options for a question in display order.
    List<QuestionOption> findByQuestionIdOrderByDisplayOrder(UUID questionId);
}
