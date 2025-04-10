package com.example.controller;

import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class HomeController {

    @FXML private Font titleFont;
    @FXML private Font subtitleFont;
    @FXML private Font footerFont;
    @FXML private ImageView heroImage;
    @FXML private Button newQuizButton;
    @FXML private Button historyButton;
    @FXML private Button feedbackButton;
    @FXML private Button exitButton;
    
    @FXML private Label titleLabel;
    @FXML private Label subtitleLabel;
    @FXML private Label footerLabel;

    // Original sizes
    private double originalTitleSize = 25.0;
    private double originalSubtitleSize = 14.0;
    private double originalFooterSize = 12.0;
    private double originalButtonWidth = 200.0;
    private double originalButtonHeight = 40.0;
    private double originalImageWidth = 150.0;
    private double originalImageHeight = 150.0;
    
    // Maximum scale values to prevent excessive sizing
    private final double MAX_SCALE_FACTOR = 2.0;
    private final double MIN_SCALE_FACTOR = 1.0;
    
    private Stage stage;
    private boolean isAdjusting = false;

    @FXML
    public void initialize() {
        exitButton.setOnAction(event -> handleExit());
    }

    public void setStage(Stage stage) {
        this.stage = stage;
        
        // Register window size change listener
        ChangeListener<Number> stageSizeListener = (observable, oldValue, newValue) -> {
            if (!isAdjusting) {
                adjustElementSizes();
            }
        };
        
        stage.widthProperty().addListener(stageSizeListener);
        stage.heightProperty().addListener(stageSizeListener);
        
        // Fullscreen listener
        stage.fullScreenProperty().addListener((obs, oldVal, newVal) -> {
            System.out.println("Fullscreen changed to: " + newVal);
            if (!isAdjusting) {
                adjustElementSizes();
            }
        });
        
        // Initialize sizes
        stage.setOnShown(event -> {
            if (!isAdjusting) {
                adjustElementSizes();
            }
        });
    }
    
    private void adjustElementSizes() {
        if (stage == null) return;
        
        try {
            isAdjusting = true;
            
            // Log window size
            if (stage.getWidth() > 0 && stage.getHeight() > 0) {
                System.out.println("Window size: " + stage.getWidth() + "x" + stage.getHeight());
                System.out.println("Fullscreen: " + stage.isFullScreen());
            } else {
                return; // Skip if dimensions are invalid
            }
            
            // Calculate scale factor with constraints
            double scaleFactorWidth = stage.getWidth() / 600.0;
            double scaleFactorHeight = stage.getHeight() / 400.0;
            double scaleFactor = Math.min(scaleFactorWidth, scaleFactorHeight);
            
            // Apply minimum scale
            scaleFactor = Math.max(MIN_SCALE_FACTOR, scaleFactor);
            
            if (stage.isFullScreen()) {
                // Limit the maximum scale in fullscreen mode
                scaleFactor = Math.min(MAX_SCALE_FACTOR, scaleFactor);
                
                // Apply controlled scale for fullscreen
                applyControlledScale(scaleFactor);
            } else {
                // Non-fullscreen - use default sizes or slight scaling
                double windowScaleFactor = Math.min(1.2, scaleFactor); // Limit window scaling
                applyControlledScale(windowScaleFactor);
            }
        } finally {
            isAdjusting = false;
        }
    }
    
    // New method with controlled scaling
    private void applyControlledScale(double scaleFactor) {
        System.out.println("Applying scale factor: " + scaleFactor);
        
        // Title - moderate scaling
        if (titleLabel != null) {
            double titleSize = originalTitleSize * Math.min(1.8, scaleFactor);
            titleLabel.setStyle("-fx-font-size: " + titleSize + "px; -fx-text-fill: white;");
        }
        
        // Subtitle - minimal scaling
        if (subtitleLabel != null) {
            double subtitleSize = originalSubtitleSize * Math.min(1.5, scaleFactor);
            subtitleLabel.setStyle("-fx-font-size: " + subtitleSize + "px; -fx-text-fill: white;");
        }
        
        // Footer - minimal scaling
        if (footerLabel != null) {
            double footerSize = originalFooterSize * Math.min(1.5, scaleFactor);
            footerLabel.setStyle("-fx-font-size: " + footerSize + "px; -fx-text-fill: white;");
        }
        
        // Hero image - moderate scaling
        if (heroImage != null) {
            double imageScale = Math.min(1.8, scaleFactor);
            heroImage.setFitWidth(originalImageWidth * imageScale);
            heroImage.setFitHeight(originalImageHeight * imageScale);
        }
        
        // Buttons - controlled scaling
        double buttonWidth = originalButtonWidth * Math.min(1.5, scaleFactor);
        double buttonHeight = originalButtonHeight * Math.min(1.3, scaleFactor);
        double buttonFontSize = 14 * Math.min(1.4, scaleFactor);
        
        applyButtonStyles(buttonFontSize, buttonWidth, buttonHeight);
    }
    
    private void applyButtonStyles(double fontSize, double width, double height) {
        String newQuizStyle = "-fx-font-size: " + fontSize + "px; -fx-background-color: #9932CC; " +
                             "-fx-text-fill: white; -fx-background-radius: 14;";
        String exitStyle = "-fx-font-size: " + fontSize + "px; -fx-background-color: #FF1493; " +
                          "-fx-text-fill: white; -fx-background-radius: 14;";
        
        if (newQuizButton != null) {
            newQuizButton.setPrefWidth(width);
            newQuizButton.setPrefHeight(height);
            newQuizButton.setStyle(newQuizStyle);
        }
        
        if (historyButton != null) {
            historyButton.setPrefWidth(width);
            historyButton.setPrefHeight(height);
            historyButton.setStyle(newQuizStyle);
        }
        
        if (feedbackButton != null) {
            feedbackButton.setPrefWidth(width);
            feedbackButton.setPrefHeight(height);
            feedbackButton.setStyle(newQuizStyle);
        }
        
        if (exitButton != null) {
            exitButton.setPrefWidth(width);
            exitButton.setPrefHeight(height);
            exitButton.setStyle(exitStyle);
        }
    }

    @FXML
    private void handleExit() {
        System.gc();
        System.exit(0);
    }
}