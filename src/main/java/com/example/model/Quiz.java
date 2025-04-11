package com.example.model;

import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class Quiz {

    private List<Question> questions = new ArrayList<>();
    private int currentQuestionIndex = 0;
    private int correctQuest = 0;
    private static List<QuizHistory> quizHistory = new ArrayList<>();
    public int numberQuestionUserPicked = 0;

    private void loadQuestions() {
        try (InputStreamReader reader = new InputStreamReader(getClass().getResourceAsStream("/data.json"))) {
            Type questionListType = new TypeToken<List<Question>>() {
            }.getType();
            List<Question> loadedQuest = new Gson().fromJson(reader, questionListType);
            if (loadedQuest != null) {
                questions = loadedQuest;
            } else {
                System.out.println("null");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public int getNumberQuestionUserPicked(){
        return numberQuestionUserPicked;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setNumberQuestionUserPicked(int numQuests){
        this.numberQuestionUserPicked = numQuests;
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

    public int getCurrentQuestionIndex() {
        return currentQuestionIndex;
    }

    public boolean hasMoreQuestions() {
        return currentQuestionIndex < questions.size() - 1;
    }

    public void nextQuestion() {
        if (hasMoreQuestions()) {
            currentQuestionIndex++;
        }
    }

    public boolean hasPrevQuestion() {
        return currentQuestionIndex > 0;
    }

    public void previousQuest() {
        if (hasPrevQuestion()) {
            currentQuestionIndex = Math.max(0, currentQuestionIndex - 1);
        }
    }

    public void navigateToQuestion(int index) {
        if (index >= 0 && index < questions.size()) {
            currentQuestionIndex = index;
        }
    }

    public int countCorrectQuest() {
        return correctQuest;
    }

    public void setCountCorrectQuest(int c) {
        this.correctQuest = c;
    }

    public void evaluateAnswers() {
        correctQuest = 0;
        for (Question question : questions) {
            List<String> userChoices = question.getUserchoice();
            List<String> correctAnswers = question.getAnswer();

            if (userChoices != null && correctAnswers != null) {
                boolean allCorrect = correctAnswers.size() == userChoices.size() && correctAnswers.containsAll(userChoices);
                if (allCorrect) {
                    incrementCorrectChoiceCount();
                }
            }
        }
    }

    public void incrementCorrectChoiceCount() {
        correctQuest++;
    }

    public List<Question> getAllQuestions() {
        return new ArrayList<>(questions);
    }

    public void addQuestion(Question question) {
        questions.add(question);
    }

    public void saveQuizResults() {
        int totalQuestions = questions.size();
        int correctAnswers = countCorrectQuest();
        double score = ((double) correctAnswers / totalQuestions) * 10; // Score out of 10
        QuizHistory historyEntry = new QuizHistory(totalQuestions, correctAnswers, score);
        quizHistory.add(historyEntry);
    }

    // New method to get quiz history
    public static List<QuizHistory> getQuizHistory() {
        return quizHistory;
    }
}
