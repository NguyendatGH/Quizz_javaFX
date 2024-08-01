package com.example.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;

public class Quiz {
    private List<Question> questions;
    private int currentQuestionIndex = 0;

    public Quiz() {
        loadQuestions();
    }

    private void loadQuestions() {
        try (InputStreamReader reader = new InputStreamReader(getClass().getResourceAsStream("/data.json"))) {
            Type questionListType = new TypeToken<List<Question>>() {}.getType();
            questions = new Gson().fromJson(reader, questionListType);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Question getCurrentQuestion() {
        if (currentQuestionIndex < questions.size()) {
            return questions.get(currentQuestionIndex);
        }
        return null;
    }

    public void nextQuestion() {
        if (currentQuestionIndex < questions.size()) {
            currentQuestionIndex++;
        }
    }

    public boolean hasMoreQuestions() {
        return currentQuestionIndex < questions.size() - 1;
    }
}
