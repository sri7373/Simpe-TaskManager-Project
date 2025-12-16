package com.eiu.taskmanager.forms.service;

import java.util.List;
import java.util.UUID;

import com.eiu.taskmanager.forms.model.Question;

public interface QuestionService {

    Question createQuestion(UUID formId, Question question);

    Question getQuestionById(UUID questionId);

    List<Question> getQuestionsByForm(UUID formId);

    Question updateQuestion(UUID questionId, Question updatedQuestion);

    void toggleQuestionActive(UUID questionId, boolean active);

    void reorderQuestions(UUID formId, List<UUID> orderedQuestionIds);

}
