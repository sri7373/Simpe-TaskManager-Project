package com.eiu.taskmanager.forms.service.impl;

import com.eiu.taskmanager.forms.model.QuestionOption;
import com.eiu.taskmanager.forms.model.Question;
import com.eiu.taskmanager.forms.repository.QuestionOptionRepository;
import com.eiu.taskmanager.forms.repository.QuestionRepository;
import com.eiu.taskmanager.forms.service.QuestionOptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
public class QuestionOptionServiceImpl implements QuestionOptionService {

    private final QuestionOptionRepository optionRepo;
    private final QuestionRepository questionRepo;

    @Autowired
    public QuestionOptionServiceImpl(QuestionOptionRepository optionRepo, QuestionRepository questionRepo) {
        this.optionRepo = optionRepo;
        this.questionRepo = questionRepo;
    }

    @Override
    public QuestionOption createOption(UUID questionId, String text) {
        Question question = questionRepo.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Question not found"));

        int nextOrder = question.getOptions() != null ? question.getOptions().size() + 1 : 1;

        QuestionOption option = new QuestionOption();
        option.setText(text);
        option.setQuestion(question);
        option.setDisplayOrder(nextOrder);
        option.setActive(true);

        return optionRepo.save(option);
    }

    @Override
    public QuestionOption editOption(UUID optionId, String newText) {
        QuestionOption option = optionRepo.findById(optionId)
                .orElseThrow(() -> new RuntimeException("Option not found"));
        option.setText(newText);
        return optionRepo.save(option);
    }

    @Override
    public void toggleActive(UUID optionId) {
        QuestionOption option = optionRepo.findById(optionId)
                .orElseThrow(() -> new RuntimeException("Option not found"));
        option.setActive(!option.isActive());
        optionRepo.save(option);
    }

    @Override
    public QuestionOption getOption(UUID optionId) {
        return optionRepo.findById(optionId)
                .orElseThrow(() -> new RuntimeException("Option not found"));
    }

    @Override
    public void deleteOption(UUID optionId) {
        QuestionOption option = optionRepo.findById(optionId)
                .orElseThrow(() -> new RuntimeException("Option not found"));
        option.setActive(false); // soft delete
        optionRepo.save(option);
    }

    @Override
    public List<QuestionOption> getOptionsForQuestion(UUID questionId) {
        Question question = questionRepo.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Question not found"));

        List<QuestionOption> options = question.getOptions();
        options.sort(Comparator.comparingInt(QuestionOption::getDisplayOrder));
        return options;
    }
}
