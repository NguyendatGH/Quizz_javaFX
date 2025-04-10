package com.example.controller;

import java.util.ArrayList;
import java.util.List;

import com.example.model.Question;
import com.example.model.Quiz;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class QuizController {

    @FXML
    private Label quest_content;

    @FXML
    private Button opt1Button;

    @FXML
    private Button opt2Button;

    @FXML
    private Button opt3Button;

    @FXML
    private Button opt4Button;

    @FXML
    private Text resultText;

    @FXML
    private Button exitButton;

    @FXML
    private Text userPoint;

    @FXML
    private Label resultBanner;

    private Quiz quiz = new Quiz();

    @FXML
    public void initialize() {
        if (quiz.getCurrentQuestion() != null) {
            displayQuestion(quiz.getCurrentQuestion());
        } else {
            quest_content.setText("No questions available!");
            disableOptions();
        }
        resultBanner.setVisible(false);
        exitButton.setVisible(false); 
    }

    private void displayQuestion(Question question) {
        if (question != null) {
            quest_content.setText(question.getQuestion());
            List<String> options = question.getOptions();
            if (options.size() >= 4) {
                opt1Button.setText(options.get(0));
                opt2Button.setText(options.get(1));
                opt3Button.setText(options.get(2));
                opt4Button.setText(options.get(3));
            }
            enableOptions();
            resultText.setText("");
            resultBanner.setVisible(false);
            exitButton.setVisible(false); 

            opt1Button.setStyle("-fx-background-color: #7B68EE;");
            opt2Button.setStyle("-fx-background-color: #7B68EE;");
            opt3Button.setStyle("-fx-background-color: #7B68EE;");
            opt4Button.setStyle("-fx-background-color: #7B68EE;");

            opt1Button.setOnAction(event -> handleOptionButtonAction(options.get(0)));
            opt2Button.setOnAction(event -> handleOptionButtonAction(options.get(1)));
            opt3Button.setOnAction(event -> handleOptionButtonAction(options.get(2)));
            opt4Button.setOnAction(event -> handleOptionButtonAction(options.get(3)));
        } else {
            showFinalResult();
        }
    }

    private void handleOptionButtonAction(String option) {
        List<String> selectedAnswer = new ArrayList<>();
        selectedAnswer.add(option);
        Question currentQuestion = quiz.getCurrentQuestion();
        currentQuestion.setUserChoices(selectedAnswer);


        List<String> correctAns = currentQuestion.getAnswer();
        boolean isCorrect = correctAns.contains(option);

    
        resultBanner.setText(isCorrect ? "Correct!" : "Wrong!");
        resultBanner.setStyle(isCorrect ? "-fx-background-color:rgb(0, 255, 0); -fx-text-fill: black;" : "-fx-background-color: #ff6347; -fx-text-fill: white;");
        resultBanner.setVisible(true);


        opt1Button.setStyle(option.equals(opt1Button.getText()) ? (isCorrect ? "-fx-background-color:rgb(31, 206, 31);" : "-fx-background-color:rgb(180, 61, 40);") : "-fx-background-color:rgba(41, 12, 204, 0.76);");
        opt2Button.setStyle(option.equals(opt2Button.getText()) ? (isCorrect ? "-fx-background-color: rgb(31, 206, 31);" : "-fx-background-color: rgb(180, 61, 40);;") : "-fx-background-color: rgba(41, 12, 204, 0.76);");
        opt3Button.setStyle(option.equals(opt3Button.getText()) ? (isCorrect ? "-fx-background-color: rgb(31, 206, 31);" : "-fx-background-color: rgb(180, 61, 40);;") : "-fx-background-color: rgba(41, 12, 204, 0.76);");
        opt4Button.setStyle(option.equals(opt4Button.getText()) ? (isCorrect ? "-fx-background-color: rgb(31, 206, 31);" : "-fx-background-color: rgb(180, 61, 40);;") : "-fx-background-color: rgba(41, 12, 204, 0.76);");

        disableOptions();

        Timeline delay = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            resultBanner.setVisible(false);
            if (quiz.hasMoreQuestions()) {
                quiz.nextQuestion();
                displayQuestion(quiz.getCurrentQuestion());
            } else {
                showFinalResult();
            }
        }));
        delay.setCycleCount(1);
        delay.play();
    }

    @FXML
    private void handleExit() {
        System.gc();
        System.exit(0);
    }

    private void showFinalResult() {
        quest_content.setText("Quiz Completed!");
        disableOptions();
        opt1Button.setVisible(false); 
        opt2Button.setVisible(false);
        opt3Button.setVisible(false);
        opt4Button.setVisible(false);
        quiz.evaluateAnswers(); 
        int totalQuestion = quiz.getAllQuestions().size();
        int correctQuest = quiz.countCorrectQuest();
        double accuracyRate = (double) correctQuest / totalQuestion;
        double finalScore = accuracyRate * 10;
        String formattedScore = String.format("%.2f", finalScore);
        userPoint.setText("Your score: " + formattedScore + " (" + correctQuest + "/" + totalQuestion + ")");
        resultText.setText("");
        resultBanner.setVisible(false);
        exitButton.setVisible(true); 
    }

    private void disableOptions() {
        opt1Button.setDisable(true);
        opt2Button.setDisable(true);
        opt3Button.setDisable(true);
        opt4Button.setDisable(true);
    }

    private void enableOptions() {
        opt1Button.setDisable(false);
        opt2Button.setDisable(false);
        opt3Button.setDisable(false);
        opt4Button.setDisable(false);
    }
}