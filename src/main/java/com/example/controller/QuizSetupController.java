package com.example.controller;

import com.example.model.Question;
import com.fasterxml.jackson.databind.ObjectMapper;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import com.example.model.Quiz;

public class QuizSetupController {

    @FXML
    private Label totalQuestionsLabel;

    @FXML
    private TextField numQuestionsField;

    @FXML
    private Button startQuizButton;

    @FXML
    private Button backButton;

    private List<Question> allQuestions;

    @FXML
    public void initialize() {
        // Load questions from data.json and display total
        loadQuestions();
        totalQuestionsLabel.setText("Total Questions Available: " + allQuestions.size());

        // Set up button actions
        startQuizButton.setOnAction(event -> handleStartQuiz());
        backButton.setOnAction(event -> handleBackToHome());
    }

    private void loadQuestions() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("data.json");
            if (inputStream == null) {
                throw new IOException("data.json not found");
            }
            System.out.println("Deserializing into class: " + Question.class.getName());
            Question[] questionsArray = mapper.readValue(inputStream, Question[].class);
            allQuestions = Arrays.asList(questionsArray);
            System.out.println("Loaded " + allQuestions.size() + " questions from data.json");
            // Debug: Print the first question to verify deserialization
            if (!allQuestions.isEmpty()) {
                System.out.println("First question: " + allQuestions.get(0));
            }
        } catch (IOException e) {
            e.printStackTrace();
            totalQuestionsLabel.setText("Error loading questions");
            allQuestions = List.of(); // Empty list to prevent null issues
        }
    }
    @FXML
    private void handleStartQuiz() {
        try {
            String input = numQuestionsField.getText();
            int numQuestions = Integer.parseInt(input);
    
            System.out.println("The list has: " + allQuestions.size() + " questions");
            System.out.println("User requested: " + numQuestions + " questions");
    
            if (numQuestions <= 0 || numQuestions > allQuestions.size()) {
                numQuestionsField.setText("Invalid number");
                return;
            }
    
            System.out.println("Passing " + allQuestions.size() + " questions to QuizController");
    
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("quizMain.fxml"));
            Parent quizView = loader.load();
    
            QuizController quizController = loader.getController();
            quizController.setQuestions(allQuestions, numQuestions);
    
            Stage stage = (Stage) startQuizButton.getScene().getWindow();
            boolean wasFullScreen = stage.isFullScreen();
            Scene quizScene = new Scene(quizView);
            stage.setScene(quizScene);
    
            if (wasFullScreen) {
                stage.setFullScreen(true);
                stage.setFullScreenExitHint("");
            }
    
            stage.show();
    
        } catch (NumberFormatException e) {
            numQuestionsField.setText("Enter a valid number");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleBackToHome() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("home.fxml"));
            Parent homeView = loader.load();
            HomeController homeController = loader.getController();
            Stage stage = (Stage) backButton.getScene().getWindow();
            Scene homeScene = new Scene(homeView);
            
            homeController.setStage(stage);
            stage.setScene(homeScene);
            stage.setFullScreen(true);
            stage.setFullScreenExitHint("");
            stage.show();

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading home screen: " + e.getMessage());
            System.gc();
            System.exit(0);
        }
    }


   
}