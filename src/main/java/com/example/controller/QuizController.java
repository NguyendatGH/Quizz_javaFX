package com.example.controller;

import com.example.model.Question;
import com.example.model.Quiz;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;

public class QuizController {

    @FXML
    private Label questionLabel;

    @FXML
    private VBox optionsBox;

    @FXML
    private Label resultLabel;

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
            if (selectedAnswer.equals(correctAnswer)) {
                resultLabel.setText("Correct!");
            } else {
                resultLabel.setText("Wrong! Correct answer is: " + correctAnswer);
            }
            if (quiz.hasMoreQuestions()) {
                quiz.nextQuestion();
                displayQuestion(quiz.getCurrentQuestion());
            } else {
                questionLabel.setText("Quiz completed!");
                optionsBox.getChildren().clear();
            }
        } else {
            resultLabel.setText("Please select an answer.");
        }
    }
}
