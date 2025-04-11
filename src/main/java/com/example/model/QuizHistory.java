package com.example.model;

import java.time.LocalDateTime;

public class QuizHistory {
    private LocalDateTime timestamp;
    private int totalQuestions;
    private int correctAnswers;
    private double score;

    public QuizHistory(int totalQuestions, int correctAnswers, double score) {
        this.timestamp = LocalDateTime.now();
        this.totalQuestions = totalQuestions;
        this.correctAnswers = correctAnswers;
        this.score = score;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public int getTotalQuestions() {
        return totalQuestions;
    }

    public int getCorrectAnswers() {
        return correctAnswers;
    }

    public double getScore() {
        return score;
    }

    @Override
    public String toString() {
        return String.format("Quiz taken on %s: %d/%d correct, Score: %.2f",
                timestamp.toString(), correctAnswers, totalQuestions, score);
    }
}