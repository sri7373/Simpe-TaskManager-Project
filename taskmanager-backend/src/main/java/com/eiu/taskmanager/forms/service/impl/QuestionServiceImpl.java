package com.eiu.taskmanager.forms.service.impl;

import com.eiu.taskmanager.forms.model.Question;
import com.eiu.taskmanager.forms.model.SurveyForm;
import com.eiu.taskmanager.forms.repository.QuestionRepository;
import com.eiu.taskmanager.forms.repository.SurveyFormRepository;
import com.eiu.taskmanager.forms.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;
    private final SurveyFormRepository surveyFormRepository;

    @Autowired
    public QuestionServiceImpl(QuestionRepository questionRepository,
                               SurveyFormRepository surveyFormRepository) {
        this.questionRepository = questionRepository;
        this.surveyFormRepository = surveyFormRepository;
    }

    @Override
    public Question createQuestion(UUID formId, Question question) {
        SurveyForm form = surveyFormRepository.findById(formId)
                .orElseThrow(() -> new RuntimeException("Form not found"));

        question.setSurveyForm(form);

        // Set displayOrder to last position, so that most recently added qn goes to the back
        int maxOrder = questionRepository.findMaxDisplayOrderByFormId(formId).orElse(0);
        question.setDisplayOrder(maxOrder + 1);

        return questionRepository.save(question);
    }

    @Override
    public Question getQuestionById(UUID questionId) {
        return questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Question not found"));
    }

    @Override
    public List<Question> getQuestionsByForm(UUID formId) {
        return questionRepository.findBySurveyFormIdOrderByDisplayOrderAsc(formId);
    }

    @Override
    public Question updateQuestion(UUID questionId, Question updatedQuestion) {
        Question existing = getQuestionById(questionId);

        if (updatedQuestion.getText() != null) existing.setText(updatedQuestion.getText());
        if (updatedQuestion.getType() != null) existing.setType(updatedQuestion.getType());
        existing.setRequired(updatedQuestion.isRequired());

        return questionRepository.save(existing);
    }

    @Override
    public void toggleQuestionActive(UUID questionId, boolean active) {
        Question q = getQuestionById(questionId);
        q.setActive(active);
        questionRepository.save(q);
    }

    @Override
    public void reorderQuestions(UUID formId, List<UUID> orderedQuestionIds) {
        List<Question> questions = getQuestionsByForm(formId);

        if (questions.size() != orderedQuestionIds.size()) {
            throw new RuntimeException("Mismatch in number of questions for reorder");
        }

        for (int i = 0; i < orderedQuestionIds.size(); i++) {
            UUID qId = orderedQuestionIds.get(i);
            Question q = questions.stream()
                    .filter(question -> question.getId().equals(qId))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Question ID not found in form: " + qId));
            q.setDisplayOrder(i + 1);
            questionRepository.save(q);
        }
    }
}
