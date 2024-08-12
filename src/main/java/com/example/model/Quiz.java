package com.example.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;
import java.util.ArrayList;

public class Quiz {
    private List<Question> questions = new ArrayList<>();
    private int currentQuestionIndex = 0;
    private int correctChoice = 0;

    public Quiz() {
        loadQuestions();
    }

    private void loadQuestions() {
        try (InputStreamReader reader = new InputStreamReader(getClass().getResourceAsStream("/data.json"))) {
            Type questionListType = new TypeToken<List<Question>>() {
            }.getType();
            List<Question> loadedQuest = new Gson().fromJson(reader, questionListType);
            if (loadedQuest != null) {
                questions = loadedQuest;
                System.out.println(questions);
            }else{
                System.out.println("null");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Question> getQuestions() {
        return questions;
    }
    
    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }


    public Question getCurrentQuestion() {
        if (currentQuestionIndex >= 0 && currentQuestionIndex < questions.size()) {
            return questions.get(currentQuestionIndex);
        }
        return null;
    }

    public boolean hasMoreQuestions() {
        return currentQuestionIndex < questions.size() - 1;
    }

    public void nextQuestion() {
        if (hasMoreQuestions()) {
            questions.get(currentQuestionIndex);
            currentQuestionIndex++;
        }
    }

    public boolean hasPrevQuestion() {
        if(currentQuestionIndex <= 0){
            return false;
        }
        return true;
    }

    public void previousQuest() {
        if (hasPrevQuestion()) {
            currentQuestionIndex = Math.max(0, currentQuestionIndex - 1);
            questions.get(currentQuestionIndex);
        }
    }

    public int countCorrectChoice() {
        return correctChoice;
    }

    public void incrementCorrectChoiceCount() {
        correctChoice++;
    }

    public List<Question> getAllQuestions() {
        return new ArrayList<>(questions);
    }

}
