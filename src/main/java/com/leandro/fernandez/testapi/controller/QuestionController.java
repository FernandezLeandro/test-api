package com.leandro.fernandez.testapi.controller;

import com.leandro.fernandez.testapi.repository.QuestionRepository;
import lombok.extern.log4j.Log4j2;
import org.apache.coyote.Response;
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
    // Crear question
    @PostMapping
    public ResponseEntity<Question> createQuestion(@RequestBody Question question) {
        // Almaceno las preguntas de questionRepository en una lista
        ArrayList<Question> questions = new ArrayList<Question>();

        // Por cada objeto encontrado, agregara este a la lista
        questionRepository.findAll().forEach(questions::add);

        // Verifico si ya existe la pregunta
        boolean questionIsEqual= false;
        for (int i=0; i<questions.size(); i++){
            if (questions.get(i).text.equalsIgnoreCase(question.text)){
                log.info("Error, the question is repeated");
                i = questions.size()+1;
                questionIsEqual = true;
            }
        }

        // Si existe, retorno el bad request, sino almaceno correctamente
        if (questionIsEqual){
            log.info("Bad Request, question is equal");
            return ResponseEntity.badRequest().build();
        } else {
            // Verifico que haya minimamente 4 respuestas
            if (question.answers.size() < 4){
                log.info("Error, there are less than 4 answers");
                return ResponseEntity.badRequest().build();
            }

            // Verifica si rightAnswerIndex es valido
            if (question.rightAnswerIndex < 0 || question.rightAnswerIndex > question.answers.size()-1){
                log.info("Error, the right answer is incorrect, " + "index is: " + question.rightAnswerIndex);
                return ResponseEntity.badRequest().build();
            }

            // Verifica si alguna respuesta es igual
            boolean answersIsEqual = false;
            int j;
            int indexComparativo = 1;
            for (int i= 0; i<question.answers.size(); i++){
                j= indexComparativo;
                while (!answersIsEqual && (j<question.answers.size())){
                    if (question.answers.get(i).equalsIgnoreCase(question.answers.get(j))){
                        answersIsEqual = true;
                        log.info("A answer is equal, salta en " + i + " " + j);
                    } else j++;
                }
                indexComparativo++;

            }

            if (answersIsEqual) {
                log.info("Bad Request,a asnwser is equal");
                return ResponseEntity.badRequest().build();
            } else {
                Question createdQuestion = questionRepository.save(question);
                return ResponseEntity.status(HttpStatus.CREATED).body(createdQuestion);
            }
        }




    }

    // Actualizar question
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
