package com.example.model;

import java.util.ArrayList;
import java.util.List;

public class Question {
    private String question;
    private List<String> options;
    private List<String> answer;
    private List<String> userChoices;

    public Question(String question, List<String> options, List<String> answer) {
        this.question = question;
        this.options = options;
        this.answer = answer;
        this.userChoices = new ArrayList<>();
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public List<String> getAnswer() {
        return answer;
    }

    public void setAnswer(List<String> answer) {
        this.answer = answer;
    }

    public List<String> getUserchoice(){
        return userChoices;
    }

    public void setUserChoices(List<String> userChoices){
        this.userChoices = userChoices;
    }
}
