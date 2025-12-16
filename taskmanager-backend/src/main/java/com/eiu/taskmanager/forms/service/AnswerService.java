package com.eiu.taskmanager.forms.service;

import java.util.List;
import java.util.UUID;

import com.eiu.taskmanager.forms.model.Answer;
import com.eiu.taskmanager.forms.model.Question;
import com.eiu.taskmanager.forms.model.Submission;

public interface AnswerService {

    Answer createAnswer(Submission submission, Question question, String value);

    List<Answer> getAnswersBySubmission(Submission submission);

    List<Answer> getAnswersByQuestion(Question question);

    Answer getAnswerById(UUID answerId);
}
