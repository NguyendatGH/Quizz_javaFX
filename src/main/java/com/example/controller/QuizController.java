package com.example.controller;

import com.example.model.Question;
import com.example.model.Quiz;

import java.util.ArrayList;
import java.util.List;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class QuizController {

    @FXML
    private Text quest_content;

    @FXML
    private Text option_toggle;

    @FXML
    private VBox optionsBox;

    @FXML
    private Text resultText;

    @FXML
    private Button nextQuest;

    @FXML
    private Button prevQuest;

    @FXML
    private Label countdownLabel;

    @FXML
    private Button closeApp;

    @FXML
    private CheckBox confirmSubmit;

    @FXML
    private Button submitButton;

    @FXML
    private Text userPoint;

    @FXML
    private Label TESTCASE;

    private Timeline timeline;

    private Quiz quiz = new Quiz();

    private int resultIndex = 0;

    private int limitTime;

    private boolean closeAppEnable = true;

    @FXML
    public void initialize() {
        if (quiz.getCurrentQuestion() != null) {
            displayQuestion(quiz.getCurrentQuestion());

            limitTime = 3600;

            timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> updateClock()));
            timeline.setCycleCount(Timeline.INDEFINITE);
            timeline.play();
            System.out.println("Timeline started.");
        } else {
            quest_content.setText("No questions available!");
            option_toggle.setText("");
            optionsBox.getChildren().clear();
        }
    }

    private void updateClock() {
        try {

            if (limitTime <= 0) {
                countdownLabel.setText("00:00:00");
                timeline.stop();
                handleSubmit();
            } else {
                limitTime = limitTime - 1;
                int hours = limitTime / 3600;
                int min = (limitTime % 3600) / 60;
                int sec = limitTime % 60;

                countdownLabel.setText(String.format("%02d:%02d:%02d", hours, min, sec));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void displayQuestion(Question question) {
        if (question != null) {
            quest_content.setText(question.getQuestion());
            option_toggle.setText(question.getDescript());
            optionsBox.getChildren().clear();

            List<String> userChoices = question.getUserchoice();
            if (userChoices == null) {
                userChoices = new ArrayList<>();
            }

            for (String option : question.getOptions()) {
                CheckBox checkBox = new CheckBox(option);
                checkBox.setSelected(userChoices.contains(option));
                checkBox.setOnAction(event -> handleCheckBoxAction(checkBox));
                optionsBox.getChildren().add(checkBox);
            }
            resultText.setText("");
        } else {
            quest_content.setText("Quiz completed!");
            optionsBox.getChildren().clear();
        }
    }

    private void handleCheckBoxAction(CheckBox checkBox){
        List<String> selectedAnswer = getSelectedAnswer();
        quiz.getCurrentQuestion().setUserChoices(selectedAnswer);   
    }


    private List<String> getSelectedAnswer() {
        List<String> selectedAnswer = new ArrayList<>();
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
        quiz.getCurrentQuestion().setUserChoices(selectedAnswers);

        if (quiz.hasMoreQuestions() || !quiz.getCurrentQuestion().getOptions().isEmpty()) {
            if (quiz.hasMoreQuestions()) {
                quiz.nextQuestion();
                displayQuestion(quiz.getCurrentQuestion());
                prevQuest.setVisible(true);
            } else {
                resultText.setText("Last question!, click submit to see your result");
            }
        } else {
            resultText.setText("Please select an answer.");
        }

    }

    @FXML
    private void handlePrev() {
        List<String> selectedAnswers = getSelectedAnswer();
        quiz.getCurrentQuestion().setUserChoices(selectedAnswers);

        if (quiz.hasPrevQuestion()) {
            quiz.previousQuest();
            displayQuestion(quiz.getCurrentQuestion());
        } else {
            resultText.setText("No previous question.");
        }
    }

    @FXML
    private void handleCloseApp() {
        System.gc();
        System.exit(0);
    }

    private void calScore(int totalQuestion) {
        int correctQuest = quiz.countCorrectQuest();

        double accuracyRate = (double) correctQuest / totalQuestion;
        double final_Score = accuracyRate * 10;

        String formattedScore = String.format("%.2f", final_Score);

        userPoint.setText(
                "Your score: " + formattedScore + "(" + correctQuest + "/" + quiz.getAllQuestions().size() + ")");
    }

    @FXML
    private void handleSubmit() {
        if (confirmSubmit.isSelected() || limitTime <= 0) {
            quiz.evaluateAnswers();

            resultText.setText("");
            displayResult();
            if (timeline != null) {
                timeline.stop();
            }
            calScore(quiz.getAllQuestions().size());

            if (closeAppEnable) {
                submitButton.setText("Close app");
                confirmSubmit.setDisable(true);
                submitButton.setOnAction(event -> handleCloseApp());
            }

        } else {
            resultText.setText("please confirm submission.");
        }
    }

    @FXML
    private void displayResult() {

        List<Question> questList = quiz.getAllQuestions();

        if (questList.isEmpty()) {
            resultText.setText("no result");
            return;
        }

        nextQuest.setText("Next");
        prevQuest.setText("Previous");
        optionsBox.getChildren().clear();

        Question currQuest = questList.get(resultIndex);

        displayQuestionResult(currQuest);

        nextQuest.setOnAction(event -> {
            if (resultIndex < questList.size() - 1) {
                resultIndex++;
                displayResult();
            }
        });

        prevQuest.setOnAction(event -> {
            if (resultIndex > 0) {
                resultIndex--;
                displayResult();
            }
        });
    }

    private void displayQuestionResult(Question question) {
        if (question != null) {
            prevQuest.setVisible(true);
            nextQuest.setVisible(true);

            quest_content.setText(question.getQuestion());
            option_toggle.setText(question.getDescript());
            optionsBox.getChildren().clear();

            List<String> userChoices = question.getUserchoice() != null ? question.getUserchoice() : new ArrayList<>();
            List<String> correctAns = question.getAnswer() != null ? question.getAnswer() : new ArrayList<>();

            for (String option : question.getOptions()) {
                CheckBox checkBox = new CheckBox(option);
                checkBox.setSelected(userChoices.contains(option));
                checkBox.setDisable(true);
                optionsBox.getChildren().add(checkBox);
            }

            resultText.setText("Correct ans: " + String.join(", ", correctAns));
        } else {
            quest_content.setText("No question available");
        }
    }
}
