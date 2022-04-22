package com.leandro.fernandez.testapi.domain.repository;

import com.leandro.fernandez.testapi.http.controller.Question;
import org.springframework.data.repository.CrudRepository;

public interface QuestionRepository extends CrudRepository<Question, Long> { }
