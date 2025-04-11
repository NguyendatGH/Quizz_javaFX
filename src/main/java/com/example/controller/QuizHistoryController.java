package com.example.controller;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

import com.example.model.Quiz;
import com.example.model.QuizHistory;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class QuizHistoryController {

    @FXML
    private VBox historyContainer;

    @FXML
    private Button backButton;

    @FXML
    public void initialize() {
        backButton.setOnAction(event -> handleBackToHome());
        displayHistory();
    }

    private void displayHistory() {
        historyContainer.getChildren().clear();

        for (QuizHistory history : Quiz.getQuizHistory()) {
            VBox entryBox = new VBox(5);
          
            entryBox.setStyle("-fx-background-color: #061449; -fx-padding: 12px; "
                    + "-fx-border-radius: 12px; -fx-background-radius: 12px; "
                    + "-fx-border-color: #8C4CF5; -fx-border-width: 1px;");

            DropShadow dropShadow = new DropShadow();
            dropShadow.setOffsetX(4);
            dropShadow.setOffsetY(4);  
            dropShadow.setRadius(4);  
            dropShadow.setColor(Color.web("#a549de", 0.74)); 
            entryBox.setEffect(dropShadow);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yy HH:mm");
            String formattedDate = history.getTimestamp().format(formatter);


            Label dateLabel = new Label("Date: " + formattedDate);
            dateLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");

            Label scoreLabel = new Label(String.format("Score: %.0f/10", history.getScore()));
            scoreLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");

            Label correctLabel = new Label("Correct: " + history.getCorrectAnswers() + "/" + history.getTotalQuestions());
            correctLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");

            entryBox.getChildren().addAll(dateLabel, scoreLabel, correctLabel);
            historyContainer.getChildren().add(entryBox);
        }

        // If no history, show a message
        if (Quiz.getQuizHistory().isEmpty()) {
            Label noHistoryLabel = new Label("No quiz history available.");
            noHistoryLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");
            historyContainer.getChildren().add(noHistoryLabel);
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

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading home screen: " + e.getMessage());
            System.gc();
            System.exit(0);
        }
    }
}
