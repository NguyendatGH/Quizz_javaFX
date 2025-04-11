package com.example.controller;

import java.io.IOException;
import java.util.List;

import com.example.model.Question;
import com.example.model.Quiz;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class QuizResultController {

    @FXML
    private Label resultTitle;
    
    @FXML
    private Text scoreText;
    
    @FXML
    private Label correctAnswersLabel;
    
    @FXML
    private VBox answerContainer;
    
    @FXML
    private Button reviewButton;
    
    @FXML
    private Button exitButton;
    
    private Quiz quizData;
    
    @FXML
    public void initialize() {
        // Initialize the UI components with placeholder text
        if (resultTitle != null) {
            resultTitle.setText("Quiz Results");
        }
        if (scoreText != null) {
            scoreText.setText("0.0%");
        }
        if (correctAnswersLabel != null) {
            correctAnswersLabel.setText("0 of 0 questions answered correctly");
        }
    }
    
    public void setQuizData(Quiz quiz) {
        this.quizData = quiz;
        displayResults();
    }
    
    private void displayResults() {
        if (quizData == null) {
            return;
        }
        
        // Display the score
        int totalQuestions = quizData.getAllQuestions().size();
        int correctAnswers = quizData.countCorrectQuest();
        double percentage = (double) correctAnswers / totalQuestions * 100;
        
        scoreText.setText(String.format("%.1f%%", percentage));
        correctAnswersLabel.setText(correctAnswers + " of " + totalQuestions + " questions answered correctly");
        
        // Display correct/incorrect for each question
        answerContainer.getChildren().clear();
        
        List<Question> questions = quizData.getAllQuestions();
        for (int i = 0; i < questions.size(); i++) {
            Question q = questions.get(i);
            
            VBox questionBox = new VBox(10); // Increased spacing between elements
            questionBox.getStyleClass().add("question-review-box");
            questionBox.setStyle("-fx-background-color: #061449; -fx-padding: 15px; " +
                    "-fx-border-radius: 8px; -fx-background-radius: 8px; " +
                    "-fx-border-color: #0B206D; -fx-border-width: 2px;");
            
            // Question number and text - made larger and more prominent
            Label questionLabel = new Label("Question " + (i + 1) + ":");
            questionLabel.setWrapText(true);
            questionLabel.setStyle("-fx-text-fill: #B15BFF; -fx-font-size: 16px; -fx-font-weight: bold;");
            questionBox.getChildren().add(questionLabel);
            
            // Question content
            Label questionContent = new Label(q.getQuestion());
            questionContent.setWrapText(true);
            questionContent.setStyle("-fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 5px 0px 10px 0px;");
            questionBox.getChildren().add(questionContent);
            
            // User answer
            Label userAnswerLabel = new Label();
            if (q.getUserchoice() != null && !q.getUserchoice().isEmpty()) {
                String userAnswer = String.join(", ", q.getUserchoice());
                userAnswerLabel.setText("Your answer: " + userAnswer);
                
                // Check if correct
                boolean isCorrect = q.getUserchoice().size() == q.getAnswer().size() && 
                                   q.getAnswer().containsAll(q.getUserchoice());
                
                if (isCorrect) {
                    userAnswerLabel.setTextFill(Color.GREEN);
                    userAnswerLabel.setStyle("-fx-font-size: 15px; -fx-font-weight: bold;");
                } else {
                    userAnswerLabel.setTextFill(Color.RED);
                    userAnswerLabel.setStyle("-fx-font-size: 15px;");
                    
                    // Show correct answer if wrong
                    Label correctLabel = new Label("Correct answer: " + String.join(", ", q.getAnswer()));
                    correctLabel.setTextFill(Color.GREEN);
                    correctLabel.setStyle("-fx-font-size: 15px; -fx-font-weight: bold;");
                    correctLabel.setWrapText(true);
                    questionBox.getChildren().add(correctLabel);
                }
            } else {
                userAnswerLabel.setText("Not answered");
                userAnswerLabel.setTextFill(Color.GRAY);
                userAnswerLabel.setStyle("-fx-font-size: 15px;");
                
                // Show correct answer for unanswered questions
                Label correctLabel = new Label("Correct answer: " + String.join(", ", q.getAnswer()));
                correctLabel.setTextFill(Color.GREEN);
                correctLabel.setStyle("-fx-font-size: 15px; -fx-font-weight: bold;");
                correctLabel.setWrapText(true);
                questionBox.getChildren().add(correctLabel);
            }
            
            userAnswerLabel.setWrapText(true);
            questionBox.getChildren().add(userAnswerLabel);
            answerContainer.getChildren().add(questionBox);
        }
    }
    
    @FXML
    public void handleReviewQuiz() {
        try {
          
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("quizReview.fxml"));
            System.out.println("go to quizReview");
            Parent reviewView = loader.load();
            
            // Pass quiz data to review controller
            QuizReviewController reviewController = loader.getController();
            reviewController.setQuizData(quizData);
            
            Stage stage = (Stage) reviewButton.getScene().getWindow();
            Scene reviewScene = new Scene(reviewView);
          
            stage.setScene(reviewScene);
            stage.setFullScreen(true);
            stage.setFullScreenExitHint("");
            stage.show();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    public void handleExit() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("home.fxml"));
            Parent homeView = loader.load();
            HomeController homeController = loader.getController();
            Stage stage = (Stage) exitButton.getScene().getWindow();
            Scene homeScene = new Scene(homeView);

            homeController.setStage(stage);
            stage.setScene(homeScene);
            stage.setFullScreen(true);
            stage.setFullScreenExitHint("");
            stage.show();
            
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading home screen: " + e.getMessage());
            System.gc();
            System.exit(0);
        }
    }
}