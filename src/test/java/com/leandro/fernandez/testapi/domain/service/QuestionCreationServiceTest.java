package com.leandro.fernandez.testapi.domain.service;

import com.leandro.fernandez.testapi.domain.exception.InvalidQuestionException;
import com.leandro.fernandez.testapi.domain.exception.QuestionAlreadyExistsException;
import com.leandro.fernandez.testapi.domain.repository.QuestionRepository;
import com.leandro.fernandez.testapi.http.controller.Question;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class QuestionCreationServiceTest {

    public static final Question QUESTION = new Question(1L);
    private QuestionRepository questionRepository;
    private QuestionCreationService questionCreationService;
    private Question questionToCreate;
    private Question createdQuestion;

    @BeforeEach
    public void setup() {
        questionRepository = mock(QuestionRepository.class);
        questionCreationService = new QuestionCreationService(questionRepository);
        questionToCreate = QUESTION;
    }

    @Test
    public void shouldSave() {
        //setup: definis los parametros de entrada de tu test
        givenAValidQuestionToCreate();

        //exercise: pones a prueba tu SUT (subject under test)
        whenCreatingQuestion();

        //assert: validas que el test haya pasado correctamente
        verify(questionRepository).save(questionToCreate);
    }

    @Test
    public void shouldFailIfQuestionAlreadyExists() {
        givenQuestionAlreadyExists();

        assertThrows(QuestionAlreadyExistsException.class, this::whenCreatingQuestion);
    }

    @Test
    public void shouldFailIfDoesNotHaveMinimumAmountOfAnswers() {
        givenAQuestionWithoutAnswers();

        assertThrows(InvalidQuestionException.class, this::whenCreatingQuestion);
    }

    private void givenAValidQuestionToCreate() {
        questionToCreate.answers.add("");
        questionToCreate.answers.add("");
        questionToCreate.answers.add("");
        questionToCreate.answers.add("");
    }

    private void givenAQuestionWithoutAnswers() {
        questionToCreate = QUESTION;
        questionToCreate.answers = Collections.emptyList();
    }

    private void givenQuestionAlreadyExists() {
        when(questionRepository.findById(any())).thenReturn(Optional.of(QUESTION));
    }

    private void whenCreatingQuestion() {
        createdQuestion = questionCreationService.create(questionToCreate);
    }

}