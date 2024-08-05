package com.example.controller;

import com.example.model.Question;
import com.example.model.Quiz;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;

public class QuizController {

    @FXML
    private Label questionLabel;

    @FXML
    private VBox optionsBox;

    @FXML
    private Label resultLabel;

    @FXML
    private Button nextQuest;

    @FXML
    private Button prevQuest;

    private ToggleGroup toggleGroup = new ToggleGroup();

    private Quiz quiz = new Quiz();

    @FXML
    public void initialize() {
        displayQuestion(quiz.getCurrentQuestion());
    }

    private void displayQuestion(Question question) {
        if (question != null) {
            questionLabel.setText(question.getQuestion());
            optionsBox.getChildren().clear();
            for (String option : question.getOptions()) {
                RadioButton radioButton = new RadioButton(option);
                radioButton.setToggleGroup(toggleGroup);
                optionsBox.getChildren().add(radioButton);
            }
            resultLabel.setText("");
        } else {
            questionLabel.setText("Quiz completed!");
            optionsBox.getChildren().clear();
        }
    }

    @FXML
    private void handleNext() {
        RadioButton selectedRadioButton = (RadioButton) toggleGroup.getSelectedToggle();
        if (selectedRadioButton != null) {
            String selectedAnswer = selectedRadioButton.getText();
            String correctAnswer = quiz.getCurrentQuestion().getAnswer();
            System.out.println(correctAnswer);
            if (selectedAnswer.equals(correctAnswer)) {
                resultLabel.setText("Correct!");
                quiz.incrementCorrectChoiceCount();
            } else {
                resultLabel.setText("Wrong! Correct answer is: " + correctAnswer);
            }
            if (quiz.hasMoreQuestions()) {
                quiz.nextQuestion();
                displayQuestion(quiz.getCurrentQuestion());
            } else {
                questionLabel
                        .setText("Quiz completed!, your number of correct question is " + quiz.countCorrectChoice());
                optionsBox.getChildren().clear();
                prevQuest.setVisible(false);
                nextQuest.setText("Close app");
                nextQuest.setOnAction(event -> handleCloseApp());

            }
        } else {
            resultLabel.setText("Please select an answer.");
        }
    }

    @FXML
    private void handlePrev() {
        if (quiz.hasPrevQuestion()) {
            quiz.previousQuest();
            prevQuest.setVisible(quiz.hasPrevQuestion());
        } else {
            resultLabel.setText("No previous question.");
            prevQuest.setVisible(false);
        }
    }

    @FXML
    private void handleCloseApp() {
        System.exit(0);
    }
}
