package com.example;

import com.example.controller.HomeController;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/home.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root, 600, 400);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Quiz App");
            primaryStage.setFullScreen(true);

            HomeController controller = loader.getController();
            if (controller != null) {

                controller.setStage(primaryStage);
            } else {
                System.err.println("Không thể tải HomeController. Kiểm tra đường dẫn controller trong FXML.");
            }

            primaryStage.setFullScreenExitHint("");

            primaryStage.show();

        } catch (Exception e) {
            System.err.println("Lỗi khi khởi động ứng dụng: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
