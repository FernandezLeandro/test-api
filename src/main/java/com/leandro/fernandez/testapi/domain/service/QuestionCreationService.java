package com.leandro.fernandez.testapi.domain.service;

import com.leandro.fernandez.testapi.domain.exception.InvalidQuestionException;
import com.leandro.fernandez.testapi.domain.exception.QuestionAlreadyExistsException;
import com.leandro.fernandez.testapi.domain.repository.QuestionRepository;
import com.leandro.fernandez.testapi.http.controller.Question;

import java.util.Optional;

public class QuestionCreationService {

    private final QuestionRepository questionRepository;

    public QuestionCreationService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }
    public Question create(Question question) {
        Optional<Question> savedQuestion = questionRepository.findById(question.id);

        if(savedQuestion.isPresent()) {
            throw new QuestionAlreadyExistsException();
        } else if (question.answers.size() < 4) {
            throw new InvalidQuestionException();
        } else {
            return questionRepository.save(question);
        }
    }
}
