package com.leandro.fernandez.testapi.controller;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Question { //JSON (javascript object notation)
    @Id
    public String question;
    @ElementCollection
    public List<String> posQuestions;
    public Integer rta;

    public Question() {
        this.question = "";
        this.posQuestions = new ArrayList<>();
        this.rta = 0;
    }
}
