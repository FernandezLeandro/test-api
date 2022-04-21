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
    public ResponseEntity<List<Question>> getQuestions() {
        ArrayList<Question> questions = findAllQuestions();

        if (questions.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(questions);
        }
    }

    @GetMapping ("/{id}")
    public ResponseEntity <Question> getQuestionByID (@PathVariable Long id) {
        ArrayList <Question> storedQuestions = findAllQuestions();
        ResponseEntity<Question> foundQuestion = searchQuestionResponseEntity(id, storedQuestions);
        if (foundQuestion != null){
            return foundQuestion;
        } else
            return ResponseEntity.noContent().build();
    }



    @DeleteMapping ("/{id}")
    public ResponseEntity <Long> deleteQuestionByID (@PathVariable Long id) {
        ArrayList <Question> storedQuestions = findAllQuestions();
        Question deletedQuestion = searchQuestion(id, storedQuestions);
        if (deletedQuestion != null) {
            questionRepository.delete(deletedQuestion);
            return new ResponseEntity<>(HttpStatus.OK);
        } else
            return new ResponseEntity<>(id, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Question> createQuestion(@RequestBody Question question) {
        ArrayList<Question> storedQuestions = findAllQuestions();

        if (isInvalid(question, storedQuestions)) {
            return ResponseEntity.badRequest().build();
        }

        Question createdQuestion = questionRepository.save(question);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdQuestion);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Question> updateQuestion(@PathVariable Long id, @RequestBody Question questionToUpdate) {
        log.info("Updating question with ID = " + id);
        Question question = questionRepository.findById(id).orElse(null);

        if (question == null) {
            return ResponseEntity.badRequest().build();
        }

        question.text = questionToUpdate.text;
        question.answers = questionToUpdate.answers;
        question.correctAnswerIndex = questionToUpdate.correctAnswerIndex;

        return ResponseEntity.ok(questionRepository.save(question));
    }


    private boolean isInvalid(Question question, ArrayList<Question> storedQuestions) {
        return questionAlreadyExists(storedQuestions, question)
                || !hasMinimumAnswers(question.answers.size())
                || correctAnswerIndexIsInvalid(question)
                || !answersAreUnique(question);
    }

    private boolean hasMinimumAnswers(int answers) {
        return answers >= 4;
    }

    private boolean answersAreUnique(Question question) {
        for (int i = 0; i < question.answers.size(); i++) {
            String answer1 = question.answers.get(i);
            for (int j = i + 1; j < question.answers.size(); j++) {
                String answer2 = question.answers.get(j);
                if (answer1.equalsIgnoreCase(answer2)) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean correctAnswerIndexIsInvalid(Question question) {
        return question.correctAnswerIndex < 0 || question.correctAnswerIndex > question.answers.size() - 1;
    }

    private boolean questionAlreadyExists(List<Question> questions, Question question) {
        for (Question q : questions) {
            if (q.text.equalsIgnoreCase(question.text)) {
                log.debug("Error, the question is repeated");
                return true;
            }
        }
        return false;
    }

    private ArrayList<Question> findAllQuestions() {
        ArrayList<Question> storedQuestions = new ArrayList<>();
        questionRepository.findAll().forEach(storedQuestions::add);
        return storedQuestions;
    }

    private ResponseEntity<Question> searchQuestionResponseEntity(Long id, ArrayList<Question> storedQuestions) {
        for (Question q : storedQuestions) {
            if (q.id.compareTo(id) == 0)
                return ResponseEntity.ok(q);
        }
        return null;
    }

    private Question searchQuestion(Long id, ArrayList<Question> storedQuestions) {
        for (Question q : storedQuestions) {
            if (q.id.compareTo(id) == 0)
                return q;
        }
        return null;
    }
}
