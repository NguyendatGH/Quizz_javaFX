package com.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/quiz.fxml"));
        primaryStage.setScene(new Scene(loader.load()));
        primaryStage.setTitle("Quiz App");
        primaryStage.setFullScreen(true);
        // primaryStage.setFullScreenExitKeyCombination(new KeyCodeCombination(KeyCode.X, KeyCombination.CONTROL_DOWN)); // ctrl +x to exit full screen mode
        primaryStage.setFullScreenExitHint(""); // auto hide key exit
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
