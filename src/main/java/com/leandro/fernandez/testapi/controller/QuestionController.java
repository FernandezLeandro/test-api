package com.leandro.fernandez.testapi.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/questions")
public class QuestionController {

    @GetMapping
    public String getQuestions() { // -> GET /questions
        return "Como se llama pepe?";
    }
}
