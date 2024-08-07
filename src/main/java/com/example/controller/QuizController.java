package com.example.controller;

import com.example.model.Question;
import com.example.model.Quiz;

import java.time.LocalTime;
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
    private Text questionText;

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

    
    private Timeline timeline;
    private LocalTime endTime;

    private Quiz quiz = new Quiz();

    @FXML
    public void initialize() {
        if (quiz.getCurrentQuestion() != null) {
            displayQuestion(quiz.getCurrentQuestion());
            LocalTime duration = LocalTime.of(1, 0, 0);
            
            endTime = LocalTime.now().plusHours(duration.getHour()).plusMinutes(duration.getMinute()).plusSeconds(duration.getSecond());

            timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> updateClock()));
            timeline.setCycleCount(Timeline.INDEFINITE);
            timeline.play();
        } else {
            questionText.setText("No questions available!");
            optionsBox.getChildren().clear();
        }
    }

    private void updateClock(){
        LocalTime now = LocalTime.now();
        LocalTime remainTime = LocalTime.ofSecondOfDay(endTime.toSecondOfDay() - now.toSecondOfDay());

        if(remainTime.toSecondOfDay() <= 0){
            countdownLabel.setText("00:00:00");
            timeline.stop();
        }else{
            countdownLabel.setText(String.format("%02d:%02d:%02d", remainTime.getHour(), remainTime.getMinute(), remainTime.getSecond()));
            
        }
    }


    private void displayQuestion(Question question) {
        if (question != null) {
            questionText.setText(question.getQuestion());
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
            resultText.setText("");
        } else {
            questionText.setText("Quiz completed!");
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
                resultText.setText("Correct!");
                quiz.incrementCorrectChoiceCount();
            } else {
                resultText.setText("Wrong! Correct answer is: " + String.join(", ", correctAnswers));
            }

            quiz.getCurrentQuestion().setUserChoices(selectedAnswers);

            if (quiz.hasMoreQuestions()) {
                quiz.nextQuestion();
                displayQuestion(quiz.getCurrentQuestion());
            } else {
                questionText
                        .setText("Quiz completed!, your number of correct question is " + quiz.countCorrectChoice());
                optionsBox.getChildren().clear();
                prevQuest.setVisible(false);
                nextQuest.setText("Close app");
                nextQuest.setOnAction(event -> handleCloseApp());
                resultText.setText("");

            }
        } else {
            resultText.setText("Please select an answer.");
        }
    }

    @FXML
    private void handlePrev() {
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
}
