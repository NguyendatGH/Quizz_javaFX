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
            // Lấy controller và thiết lập stage
            HomeController controller = loader.getController();
            if (controller != null) {
                // Thiết lập controller trước khi hiển thị stage
                controller.setStage(primaryStage);
            } else {
                System.err.println("Không thể tải HomeController. Kiểm tra đường dẫn controller trong FXML.");
            }
            
            // Đặt fullscreen
            primaryStage.setFullScreenExitHint(""); // ẩn thông báo phím tắt thoát
            
            // Hiển thị stage
            primaryStage.show();
            
            // Đặt fullscreen sau khi show (khuyến nghị để tránh một số vấn đề với một số trình quản lý cửa sổ)
            // Bỏ comment dòng dưới nếu bạn muốn mặc định là fullscreen
            // primaryStage.setFullScreen(true);
        } catch (Exception e) {
            System.err.println("Lỗi khi khởi động ứng dụng: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}