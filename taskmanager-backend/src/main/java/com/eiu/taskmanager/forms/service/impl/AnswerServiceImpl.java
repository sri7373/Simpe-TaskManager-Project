package com.eiu.taskmanager.forms.service.impl;

import com.eiu.taskmanager.forms.model.Answer;
import com.eiu.taskmanager.forms.model.Question;
import com.eiu.taskmanager.forms.model.Submission;
import com.eiu.taskmanager.forms.repository.AnswerRepository;
import com.eiu.taskmanager.forms.service.AnswerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.eiu.taskmanager.forms.service.QuestionService;
import com.eiu.taskmanager.forms.service.AnswerService;
import com.eiu.taskmanager.forms.service.SubmissionService;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class AnswerServiceImpl implements AnswerService {

    private final AnswerRepository answerRepository;

    @Autowired
    public AnswerServiceImpl(AnswerRepository answerRepository) {
        this.answerRepository = answerRepository;
    }

    @Override
    public Answer createAnswer(Submission submission, Question question, String value) {
        Answer answer = new Answer();
        answer.setSubmission(submission);
        answer.setQuestion(question);
        answer.setValue(value);
        return answerRepository.save(answer);
    }

    @Override
    public List<Answer> getAnswersBySubmission(Submission submission) {
        return answerRepository.findBySubmissionId(submission.getId());
    }

    @Override
    public List<Answer> getAnswersByQuestion(Question question) {
        return answerRepository.findByQuestionId(question.getId());
    }

    @Override
    public Answer getAnswerById(UUID answerId) {
        return answerRepository.findById(answerId)
                .orElseThrow(() -> new RuntimeException("Answer not found"));
    }
}
