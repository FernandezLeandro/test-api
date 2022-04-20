package com.leandro.fernandez.testapi.controller;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Question { //JSON (javascript object notation)
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    public Long id;
    public String text;
    @ElementCollection
    public List<String> answers;
    public Integer correctAnswerIndex;

    public Question() {
        this.text = "";
        this.answers = new ArrayList<>();
        this.correctAnswerIndex = 0;
    }
}
