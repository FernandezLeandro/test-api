package com.leandro.fernandez.testapi.controller;

import com.leandro.fernandez.testapi.repository.QuestionRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/questions")
@Log4j2
public class QuestionController {

    @Autowired
    private QuestionRepository questionRepository;

    @GetMapping
    public ResponseEntity<List<Question>> getQuestions() { // -> GET /questions
        ArrayList<Question> questions = new ArrayList();
        questionRepository.findAll().forEach(questions::add);

        if(questions.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(questions);
        }
    }

    @PostMapping
    public ResponseEntity<Question> createQuestion(@RequestBody Question question) {
        Question createdQuestion = questionRepository.save(question);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdQuestion);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Question> updateQuestion(@PathVariable Long id, @RequestBody Question questionToUpdate) {
        log.info("Updating question with ID = " + id);
        Question question = questionRepository.findById(id).orElse(null);

        if(question == null) {
            return ResponseEntity.badRequest().build();
        }

        question.text = questionToUpdate.text;
        question.answers = questionToUpdate.answers;
        question.rightAnswerIndex = questionToUpdate.rightAnswerIndex;

        return ResponseEntity.ok(questionRepository.save(question));
    }
}
