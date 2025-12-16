package com.eiu.taskmanager.forms.service;

import java.util.List;
import java.util.UUID;

import com.eiu.taskmanager.forms.model.QuestionOption;

public interface QuestionOptionService {
    QuestionOption createOption(UUID questionId, String text);
    QuestionOption editOption(UUID optionId, String newText);
    // enable or disable the option without deleting it
    // so just controls if option is visible or usable in the form
    void toggleActive(UUID optionId);
    QuestionOption getOption(UUID optionId);
    void deleteOption(UUID optionId); // soft delete
    List<QuestionOption> getOptionsForQuestion(UUID questionId);
}
