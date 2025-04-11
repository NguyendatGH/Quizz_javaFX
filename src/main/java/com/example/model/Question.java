package com.example.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;

public class Question {
    private String question;
    private String description;
    private List<String> options;
    private List<String> answer;
    private List<String> userChoices;

    // Default constructor (required by Jackson package)
    public Question() {
        this.question = null;
        this.description = null;
        this.options = new ArrayList<>();
        this.answer = new ArrayList<>();
        this.userChoices = new ArrayList<>();
    }

    public Question(String question, String description, List<String> options, List<String> answer) {
        this.question = question;
        this.description = description;
        this.options = options;
        this.answer = answer;
        this.userChoices = new ArrayList<>();
    }

    public Question(Question cpyQuestion) { // cpy == copy question
        this.question = cpyQuestion.question;
        this.description = cpyQuestion.description;
        this.options = new ArrayList<>(cpyQuestion.options);
        this.answer = new ArrayList<>(cpyQuestion.answer);
        this.userChoices = new ArrayList<>(cpyQuestion.userChoices);
    }

    @JsonProperty("question")
    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    @JsonProperty("description") 
    public String getDescription() {
        return description;
    }

    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
    }

    @JsonProperty("options")
    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    @JsonProperty("answer")
    public List<String> getAnswer() {
        return answer;
    }

    public void setAnswer(List<String> answer) {
        this.answer = answer;
    }

    @JsonProperty("userchoice")
    public List<String> getUserchoice() {
        return userChoices;
    }

    public void setUserChoices(List<String> userChoices) {
        this.userChoices = userChoices;
    }

    @Override
    public String toString() {
        return "Question: " + question;
    }
}