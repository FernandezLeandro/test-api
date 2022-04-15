package com.leandro.fernandez.testapi.repository;

import com.leandro.fernandez.testapi.controller.Question;
import org.springframework.data.repository.CrudRepository;

public interface QuestionRepository extends CrudRepository<Question, Long> { }
