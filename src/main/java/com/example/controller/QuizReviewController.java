package com.example.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.example.model.Question;
import com.example.model.Quiz;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class QuizReviewController {

    @FXML
    private Label questionLabel;
    
    @FXML
    private Label questionNumberLabel;
    
    @FXML
    private VBox optionsContainer;
    
    @FXML
    private Button prevButton;
    
    @FXML
    private Button nextButton;
    
    @FXML
    private Button backToResultsButton;
    
    @FXML
    private VBox progressIndicator;
    
    private Quiz quiz;
    private int currentQuestionIndex = 0;
    private List<StackPane> questionCircles = new ArrayList<>();
    private final int CIRCLES_PER_ROW = 6; // Number of circles per row
    
    // Color constants - matching the colors in the image
    private final String CORRECT_COLOR = "#1FCE1F"; // Green for correct answers
    private final String INCORRECT_COLOR = "#B43D28"; // Red for incorrect answers
    private final String NEUTRAL_COLOR = "#666666"; // Gray for unanswered
    private final String CURRENT_COLOR = "#FF1493"; // Pink for current question
    
    @FXML
    public void initialize() {
        // Set up button actions
        prevButton.setOnAction(event -> handlePreviousQuestion());
        nextButton.setOnAction(event -> handleNextQuestion());
        backToResultsButton.setOnAction(event -> handleBackToResults());
        
        // Initialize labels with placeholder text
        questionLabel.setText("Question will be displayed here");
        questionNumberLabel.setText("Question 0/0");
        
        // Disable navigation buttons initially until we have quiz data
        prevButton.setDisable(true);
        nextButton.setDisable(true);
        
        // Configure options container
        optionsContainer.setSpacing(10);
    }
    
    public void setQuizData(Quiz quiz) {
        this.quiz = quiz;
        currentQuestionIndex = 0;
        
        // Generate question circles
        generateQuestionCircles();
        
        // Display the first question
        displayCurrentQuestion();
        updateNavigationButtons();
        updateCircleColors();
    }
    
    private void generateQuestionCircles() {
        // Clear existing circles
        progressIndicator.getChildren().clear();
        questionCircles.clear();
        
        if (quiz == null || quiz.getAllQuestions().isEmpty()) {
            return;
        }
        
        List<Question> allQuestions = quiz.getAllQuestions();
        int totalQuestions = allQuestions.size();
        
        // Determine how many rows are needed
        int numRows = (int) Math.ceil((double) totalQuestions / CIRCLES_PER_ROW);
        
        for (int row = 0; row < numRows; row++) {
            HBox rowBox = new HBox();
            rowBox.setAlignment(Pos.CENTER);
            rowBox.setSpacing(10);
            
            int startIndex = row * CIRCLES_PER_ROW;
            int endIndex = Math.min(startIndex + CIRCLES_PER_ROW, totalQuestions);
            
            for (int i = startIndex; i < endIndex; i++) {
                final int questionIndex = i;
                
                StackPane stackPane = new StackPane();
                Circle circle = new Circle(15, Color.web(NEUTRAL_COLOR));
                Text questionNumber = new Text(String.valueOf(i + 1));
                questionNumber.setFill(Color.WHITE);
                questionNumber.setStyle("-fx-font-size: 12px;");
                
                stackPane.getChildren().addAll(circle, questionNumber);
                stackPane.setOnMouseClicked(event -> navigateToQuestion(questionIndex));
                
                rowBox.getChildren().add(stackPane);
                questionCircles.add(stackPane);
            }
            
            progressIndicator.getChildren().add(rowBox);
        }
    }
    
    private void navigateToQuestion(int questionIndex) {
        if (quiz != null && questionIndex >= 0 && questionIndex < quiz.getAllQuestions().size()) {
            currentQuestionIndex = questionIndex;
            displayCurrentQuestion();
            updateNavigationButtons();
            updateCircleColors();
        }
    }
    
    private void displayCurrentQuestion() {
        if (quiz == null || quiz.getAllQuestions().isEmpty()) {
            return;
        }
        
        List<Question> questions = quiz.getAllQuestions();
        if (currentQuestionIndex >= 0 && currentQuestionIndex < questions.size()) {
            Question question = questions.get(currentQuestionIndex);
            
            // Update question number and text
            questionNumberLabel.setText("Question " + (currentQuestionIndex + 1) + "/" + questions.size());
            questionLabel.setText(question.getQuestion());
            
            // Clear previous options
            optionsContainer.getChildren().clear();
            
            // Display options with correct/incorrect marking
            List<String> options = question.getOptions();
            List<String> correctAnswers = question.getAnswer();
            List<String> userChoices = question.getUserchoice();
            
            char optionLetter = 'A';
            for (String option : options) {
                HBox optionBox = new HBox(10);
                optionBox.getStyleClass().add("option-box");
                
                // Determine if this option is the user's choice and if it's correct
                boolean isUserChoice = userChoices != null && userChoices.contains(option);
                boolean isCorrectAnswer = correctAnswers != null && correctAnswers.contains(option);
                
                // Set appropriate style
                String optionStyle = "-fx-padding: 10px; -fx-border-radius: 8px; -fx-background-radius: 8px; ";
                
                if (isUserChoice) {
                    if (isCorrectAnswer) {
                        // User chose correctly
                        optionStyle += "-fx-background-color: rgba(31, 206, 31, 0.7); -fx-border-color: #1FCE1F; -fx-border-width: 2px;";
                    } else {
                        // User chose incorrectly
                        optionStyle += "-fx-background-color: rgba(180, 61, 40, 0.7); -fx-border-color: #B43D28; -fx-border-width: 2px;";
                    }
                } else if (isCorrectAnswer) {
                    // Correct answer not chosen by user
                    optionStyle += "-fx-background-color: rgba(31, 206, 31, 0.3); -fx-border-color: #1FCE1F; -fx-border-width: 2px;";
                } else {
                    // Regular option
                    optionStyle += "-fx-background-color: #061449; -fx-border-color: #0B206D; -fx-border-width: 2px;";
                }
                
                optionBox.setStyle(optionStyle);
                
                // Option label with letter prefix
                Label optionLabel = new Label(optionLetter + "     " + option);
                optionLabel.setWrapText(true);
                optionLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");
                
                optionBox.getChildren().add(optionLabel);
                optionsContainer.getChildren().add(optionBox);
                
                // Increment option letter for next option
                optionLetter++;
            }
            
            // Add explanation if available
            if (question.getDescript() != null && !question.getDescript().isEmpty()) {
                Label explanationLabel = new Label("Explanation: " + question.getDescript());
                explanationLabel.setWrapText(true);
                explanationLabel.setStyle("-fx-text-fill: #B15BFF; -fx-font-size: 14px; -fx-padding: 10px 0 0 0;");
                optionsContainer.getChildren().add(explanationLabel);
            }
        }
    }
    
    private void updateCircleColors() {
        if (quiz == null || quiz.getAllQuestions().isEmpty()) {
            return;
        }
        
        List<Question> allQuestions = quiz.getAllQuestions();
        
        for (int i = 0; i < questionCircles.size(); i++) {
            if (i < allQuestions.size()) {
                StackPane circlePane = questionCircles.get(i);
                Node circleNode = circlePane.getChildren().get(0);
                
                if (circleNode instanceof Circle) {
                    Circle circle = (Circle) circleNode;
                    Question question = allQuestions.get(i);
                    
                    if (i == currentQuestionIndex) {
                        // Always highlight current question with pink
                        circle.setFill(Color.web(CURRENT_COLOR));
                    } else {
                        // For other questions, check if they're correct/incorrect
                        List<String> userChoices = question.getUserchoice();
                        List<String> correctAnswers = question.getAnswer();
                        
                        if (userChoices != null && !userChoices.isEmpty()) {
                            // Check if the answer is completely correct
                            boolean isCorrect = true;
                            
                            // All correct answers must be selected
                            if (correctAnswers != null) {
                                for (String correctAns : correctAnswers) {
                                    if (!userChoices.contains(correctAns)) {
                                        isCorrect = false;
                                        break;
                                    }
                                }
                                
                                // No incorrect answers can be selected
                                for (String userChoice : userChoices) {
                                    if (!correctAnswers.contains(userChoice)) {
                                        isCorrect = false;
                                        break;
                                    }
                                }
                            }
                            
                            // Color the circle based on correctness
                            if (isCorrect) {
                                circle.setFill(Color.web(CORRECT_COLOR)); // Green for correct
                            } else {
                                circle.setFill(Color.web(INCORRECT_COLOR)); // Red for incorrect
                            }
                        } else {
                            // No answer was given
                            circle.setFill(Color.web(NEUTRAL_COLOR)); // Gray for unanswered
                        }
                    }
                }
            }
        }
    }
    
    @FXML
    public void handlePreviousQuestion() {
        if (currentQuestionIndex > 0) {
            currentQuestionIndex--;
            displayCurrentQuestion();
            updateNavigationButtons();
            updateCircleColors();
        }
    }
    
    @FXML
    public void handleNextQuestion() {
        if (quiz != null && currentQuestionIndex < quiz.getAllQuestions().size() - 1) {
            currentQuestionIndex++;
            displayCurrentQuestion();
            updateNavigationButtons();
            updateCircleColors();
        }
    }
    
    private void updateNavigationButtons() {
        prevButton.setDisable(currentQuestionIndex <= 0);
        nextButton.setDisable(quiz == null || currentQuestionIndex >= quiz.getAllQuestions().size() - 1);
    }
    
    @FXML
    public void handleBackToResults() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("quizResult.fxml"));
            Parent resultView = loader.load();
            
            // Pass quiz data back to the result controller
            QuizResultController resultController = loader.getController();
            resultController.setQuizData(quiz);
            
            // Switch back to results scene
            Stage stage = (Stage) backToResultsButton.getScene().getWindow();
            Scene resultScene = new Scene(resultView);
            stage.setScene(resultScene);
            stage.setFullScreen(true);
            stage.setFullScreenExitHint("");
            stage.show();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}