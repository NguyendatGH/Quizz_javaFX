package com.example.controller;

import com.example.model.Question;
import com.example.model.Quiz;

import java.util.ArrayList;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.CheckBox;
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


    private Quiz quiz = new Quiz();

    @FXML
    public void initialize() {
        if (quiz.getCurrentQuestion() != null) {
            displayQuestion(quiz.getCurrentQuestion());
        } else {
            questionLabel.setText("No questions available!");
            optionsBox.getChildren().clear();
        }
    }

    private void displayQuestion(Question question) {
        if (question != null) {
            questionLabel.setText(question.getQuestion());
            optionsBox.getChildren().clear();

            List<String> userChoices = question.getUserchoice();
            if(userChoices == null){
                userChoices = new ArrayList<>();
            }

            for (String option : question.getOptions()) {
                CheckBox checkBox = new CheckBox(option);
                checkBox.setSelected(userChoices.contains(option));
                optionsBox.getChildren().add(checkBox);
            }
            resultLabel.setText("");
        } else {
            questionLabel.setText("Quiz completed!");
            optionsBox.getChildren().clear();
        }
    }

    private List<String> getSelectedAnswer() {
        List<String> selectedAnswer  = new ArrayList<>();
        for (javafx.scene.Node node : optionsBox.getChildren()) {
            if (node instanceof CheckBox) {
                CheckBox checkBox = (CheckBox) node;
                if (checkBox.isSelected()) {
                    selectedAnswer.add(checkBox.getText());
                }
            }
        }
        return selectedAnswer;
    }


    @FXML
    private void handleNext() {
        List<String> selectedAnswers = getSelectedAnswer();
        if (!selectedAnswers.isEmpty()) {
            List<String> correctAnswers = quiz.getCurrentQuestion().getAnswer();
            // System.out.println(correctAnswers);

            boolean allCorrect = correctAnswers.containsAll(selectedAnswers);
            // boolean inCorrect = !selectedAnswers.containsAll(correctAnswers);


            if (allCorrect) {
                resultLabel.setText("Correct!");
                quiz.incrementCorrectChoiceCount();
            } else {
                resultLabel.setText("Wrong! Correct answer is: " + String.join(", ", correctAnswers));
            }

            quiz.getCurrentQuestion().setUserChoices(selectedAnswers);

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
                resultLabel.setText("");

            }
        } else {
            resultLabel.setText("Please select an answer.");
        }
    }

    @FXML
    private void handlePrev() {
        if (quiz.hasPrevQuestion()) {
            quiz.previousQuest();
            displayQuestion(quiz.getCurrentQuestion());
        } else {
            resultLabel.setText("No previous question.");
        }
    }

   

    @FXML
    private void handleCloseApp() {
        System.exit(0);
    }
}
