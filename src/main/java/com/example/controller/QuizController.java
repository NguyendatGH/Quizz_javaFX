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
    private Text questDecription;

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
    private Button submitButton;


    private Timeline timeline;
    private LocalTime endTime;

    private Quiz quiz = new Quiz();

    private List<Question> resultHistory = new ArrayList<>();
    private int resultIndex = 0; 


    @FXML
    public void initialize() {
        if (quiz.getCurrentQuestion() != null) {
            displayQuestion(quiz.getCurrentQuestion());
            LocalTime duration = LocalTime.of(1, 0, 0);

            endTime = LocalTime.now().plusHours(duration.getHour()).plusMinutes(duration.getMinute())
                    .plusSeconds(duration.getSecond());

            timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> updateClock()));
            timeline.setCycleCount(Timeline.INDEFINITE);
            timeline.play();
        } else {
            questionText.setText("No questions available!");
            questDecription.setText("");
            optionsBox.getChildren().clear();
        }
    }

    private void updateClock() {
        LocalTime now = LocalTime.now();
        LocalTime remainTime = LocalTime.ofSecondOfDay(endTime.toSecondOfDay() - now.toSecondOfDay());

        if (remainTime.toSecondOfDay() <= 0) {
            countdownLabel.setText("00:00:00");
            timeline.stop();
            handleSubmit();
        } else {
            countdownLabel.setText(String.format("%02d:%02d:%02d", remainTime.getHour(), remainTime.getMinute(),
                    remainTime.getSecond()));

        }
    }

    

    private void displayQuestion(Question question) {
        if (question != null) {
            questionText.setText(question.getQuestion());
            questDecription.setText(question.getDescript());
            optionsBox.getChildren().clear();

            List<String> userChoices = question.getUserchoice();
            if (userChoices == null) {
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
        if (!selectedAnswers.isEmpty()) {
            List<String> correctAnswers = quiz.getCurrentQuestion().getAnswer();

            boolean allCorrect = correctAnswers.containsAll(selectedAnswers);

            if (allCorrect) {
                resultText.setText("Correct!");
                quiz.incrementCorrectChoiceCount();
            } else {
                resultText.setText("Wrong! Correct answer is: " + String.join(", ", correctAnswers));
            }

            quiz.getCurrentQuestion().setUserChoices(selectedAnswers);

            resultHistory.add(quiz.getCurrentQuestion());

            if (quiz.hasMoreQuestions()) {
                quiz.nextQuestion();
                displayQuestion(quiz.getCurrentQuestion());
                prevQuest.setVisible(true);
            } else {
                questionText
                        .setText("Quiz completed!, your number of correct question is " + quiz.countCorrectChoice());
                questDecription.setText("");
                resultText.setText("Last question!, click submit to see your result");
            
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


    @FXML
    private void handleSubmit(){
        List<String> selectedAnswers = getSelectedAnswer();
        if (!selectedAnswers.isEmpty()) {
            quiz.getCurrentQuestion().setUserChoices(selectedAnswers);
            displayResult();
            resultHistory.add(quiz.getCurrentQuestion());
            resultText.setText("");
            if(timeline != null){
                timeline.stop();
            }

        }else{
            resultText.setText("cannot submit");
        }
    }

    @FXML
    private void displayResult() {
        if(resultHistory.isEmpty()){
            resultText.setText("no result");
            return;
        }
        nextQuest.setText("next");
        optionsBox.getChildren().clear();
        Question currentQuest = resultHistory.get(resultIndex);
        displayQuestionResult(currentQuest);

        submitButton.setVisible(false);

        nextQuest.setOnAction(event -> {
            if (resultIndex < resultHistory.size() - 1) {
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
        submitButton.setVisible(false);
    }


    private void displayQuestionResult(Question question){
        if(question != null){
            prevQuest.setVisible(true);
            nextQuest.setVisible(true);

            questionText.setText(question.getQuestion());
            questDecription.setText(question.getDescript());
            optionsBox.getChildren().clear();


            List<String> userChoices = question.getUserchoice();
            List<String> correctAns = question.getAnswer();

            for(String option: question.getOptions()){
                CheckBox checkBox = new CheckBox(option);
                checkBox.setSelected(userChoices.contains(option));
                checkBox.setDisable(true);
                optionsBox.getChildren().add(checkBox);
            }

            resultText.setText("Correct ans: "+ String.join(", ", correctAns));
        }else{
            questionText.setText("No question available");
        }
    }
}
