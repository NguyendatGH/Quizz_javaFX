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

public class QuizController {

    @FXML
    private Label quest_content;

    @FXML
    private Button opt1Button;

    @FXML
    private Button opt2Button;

    @FXML
    private Button opt3Button;

    @FXML
    private Button opt4Button;

    @FXML
    private Button prevButton;

    @FXML
    private Button nextButton;

    @FXML
    private Button finishButton;

    @FXML
    private Text userPoint;

    @FXML
    private Label questionNumberLabel;
    
    @FXML
    private VBox progressIndicator;

    // Color constants
    private final String COMPLETED_COLOR = "#8C4CF5"; // Purple
    private final String INCOMPLETE_COLOR = "#666666"; // Gray
    private final String CURRENT_COLOR = "#FF1493";   // Pink
    private final String SELECTED_COLOR = "#0B206D";  // Dark Blue for selected options
    
    private Quiz quiz = new Quiz();
    private List<StackPane> questionCircles = new ArrayList<>();
    private final int CIRCLES_PER_ROW = 6; // Número de círculos por linha
    
    // Array de botões para fácil acesso
    private Button[] optionButtons;

    @FXML
    public void initialize() {
        // Inicializar array de botões
        optionButtons = new Button[] {opt1Button, opt2Button, opt3Button, opt4Button};
        
        // Limpe o conteúdo atual do VBox progressIndicator
        progressIndicator.getChildren().clear();
        questionCircles.clear();
        
        // Gere os círculos dinamicamente com base no número de perguntas
        generateQuestionCircles();
        
        if (quiz.getCurrentQuestion() != null) {
            displayQuestion(quiz.getCurrentQuestion());
            updateQuestionNumberLabel();
            updateCircleColors();
        } else {
            quest_content.setText("No questions available!");
            disableOptions();
        }
        
        updateNavigationButtons();
    }

    private void generateQuestionCircles() {
        List<Question> allQuestions = quiz.getAllQuestions();
        int totalQuestions = allQuestions.size();
        
        // Determine quantas linhas são necessárias
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
                Circle circle = new Circle(15, Color.web(INCOMPLETE_COLOR));
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
        if (questionIndex >= 0 && questionIndex < quiz.getAllQuestions().size()) {
            quiz.navigateToQuestion(questionIndex);
            displayQuestion(quiz.getCurrentQuestion());
            updateQuestionNumberLabel();
            updateCircleColors();
            updateNavigationButtons();
        }
    }

    private void displayQuestion(Question question) {
        if (question != null) {
            quest_content.setText(question.getQuestion());
            List<String> options = question.getOptions();
            int optionCount = options.size();
            
            // Reset all buttons first
            resetOptionButtonStyles();
            disableAllOptions();
            
            // Configure cada botão de opção com base no número de opções disponíveis
            for (int i = 0; i < optionButtons.length; i++) {
                Button button = optionButtons[i];
                
                if (i < optionCount) {
                    // Este botão deve ser exibido
                    button.setVisible(true);
                    button.setDisable(false);
                    
                    // Definir o texto da opção (A, B, C, D + conteúdo)
                    char optionLetter = (char)('A' + i);
                    button.setText(optionLetter + "     " + options.get(i));
                    
                    // Definir o evento de clique
                    final int optionIndex = i;
                    button.setOnAction(event -> handleOptionButtonAction(options.get(optionIndex)));
                } else {
                    // Ocultar botões para opções que não existem
                    button.setVisible(false);
                }
            }

            // Highlight existing user choices
            List<String> userChoices = question.getUserchoice();
            if (userChoices != null && !userChoices.isEmpty()) {
                highlightUserSelections(userChoices);
            }
        } else {
            showFinalResult();
        }
    }

    private void resetOptionButtonStyles() {
        String defaultStyle = "-fx-background-color: #061449; " +
                "-fx-background-radius: 8px; " +
                "-fx-font-size: 14px; " +
                "-fx-text-fill: white; " +
                "-fx-alignment: CENTER_LEFT; " +
                "-fx-padding: 0 0 0 15; " + 
                "-fx-border-color: #0B206D; " +
                "-fx-border-width: 2px; " +
                "-fx-border-radius: 8px;";
        
        for (Button button : optionButtons) {
            button.setStyle(defaultStyle);
        }
    }

    private void highlightUserSelections(List<String> selectedOptions) {
        String selectedStyle = "-fx-background-color: #0B206D; " +
                "-fx-background-radius: 8px; " +
                "-fx-font-size: 14px; " +
                "-fx-text-fill: white; " +
                "-fx-alignment: CENTER_LEFT; " +
                "-fx-padding: 0 0 0 15; " + 
                "-fx-border-color: #8C4CF5; " +
                "-fx-border-width: 2px; " +
                "-fx-border-radius: 8px;";
        
        String defaultStyle = "-fx-background-color: #061449; " +
                "-fx-background-radius: 8px; " +
                "-fx-font-size: 14px; " +
                "-fx-text-fill: white; " +
                "-fx-alignment: CENTER_LEFT; " +
                "-fx-padding: 0 0 0 15; " + 
                "-fx-border-color: #0B206D; " +
                "-fx-border-width: 2px; " +
                "-fx-border-radius: 8px;";

        // Para cada botão visível, verifique se deve ser destacado
        for (Button button : optionButtons) {
            if (button.isVisible()) {
                String buttonText = button.getText().substring(button.getText().indexOf(" ")).trim();
                button.setStyle(selectedOptions != null && selectedOptions.contains(buttonText) ? 
                                selectedStyle : defaultStyle);
            }
        }
    }

    private void handleOptionButtonAction(String option) {
        Question currentQuestion = quiz.getCurrentQuestion();
        List<String> userChoices = currentQuestion.getUserchoice();
    
        if (userChoices == null) {
            userChoices = new ArrayList<>();
            currentQuestion.setUserChoices(userChoices);
        }
    
        if (userChoices.contains(option)) {
            userChoices.remove(option);
        } else {
            userChoices.add(option);
        }
        
        highlightUserSelections(userChoices);
        updateCircleColors();
    }
    
    private void resetQuestionAnswer(int questionIndex) {
        if (questionIndex >= 0 && questionIndex < quiz.getAllQuestions().size()) {
            Question question = quiz.getAllQuestions().get(questionIndex);
            question.setUserChoices(new ArrayList<>());
            if (questionIndex == quiz.getCurrentQuestionIndex()) {
                displayQuestion(question);
            }
            updateCircleColors();
        }
    }

    @FXML
    public void handleNext() {
        if (quiz.hasMoreQuestions()) {
            quiz.nextQuestion();
            displayQuestion(quiz.getCurrentQuestion());
            updateQuestionNumberLabel();
            updateCircleColors();
            updateNavigationButtons();
        }
    }

    @FXML
    public void handlePrev() {
        if (quiz.hasPrevQuestion()) {
            quiz.previousQuest();
            displayQuestion(quiz.getCurrentQuestion());
            updateQuestionNumberLabel();
            updateCircleColors();
            updateNavigationButtons();
        }
    }

    private void updateNavigationButtons() {
        prevButton.setDisable(!quiz.hasPrevQuestion());
        nextButton.setDisable(!quiz.hasMoreQuestions());
    }

    private void updateQuestionNumberLabel() {
        int currentIndex = quiz.getCurrentQuestionIndex() + 1;
        int totalQuestions = quiz.getAllQuestions().size();
        questionNumberLabel.setText("Question " + currentIndex + "/" + totalQuestions);
    }

    private void updateCircleColors() {
        List<Question> allQuestions = quiz.getAllQuestions();
        int currentIndex = quiz.getCurrentQuestionIndex();
        
        for (int i = 0; i < questionCircles.size(); i++) {
            if (i < allQuestions.size()) {
                StackPane circlePane = questionCircles.get(i);
                Node circleNode = circlePane.getChildren().get(0);
                
                if (circleNode instanceof Circle) {
                    Circle circle = (Circle) circleNode;
                    if (i == currentIndex) {
                        circle.setFill(Color.web(CURRENT_COLOR));
                    } else if (i < allQuestions.size() && 
                             allQuestions.get(i).getUserchoice() != null && 
                             !allQuestions.get(i).getUserchoice().isEmpty()) {
                        circle.setFill(Color.web(COMPLETED_COLOR));
                    } else {
                        circle.setFill(Color.web(INCOMPLETE_COLOR));
                    }
                }
            }
        }
    }

    @FXML
    public void showFinalResult() {
        try {
            quiz.evaluateAnswers();
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("quizResult.fxml"));
            Parent resultView = loader.load();
            QuizResultController resultController = loader.getController();
            resultController.setQuizData(quiz);
            Stage stage = (Stage) finishButton.getScene().getWindow();
            
            // Preserve fullscreen state
            boolean wasFullScreen = stage.isFullScreen();
            
            Scene resultScene = new Scene(resultView);
            stage.setScene(resultScene);
            
            // Reapply fullscreen state if it was fullscreen
            if (wasFullScreen) {
                stage.setFullScreen(true);
                stage.setFullScreenExitHint("");
            }
            
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showFallbackResult();
        }
    }
    
    public void showCorrectAnswers() {
        List<Question> allQuestions = quiz.getAllQuestions();
        for (Question question : allQuestions) {
            if (question.getUserchoice() != null && !question.getUserchoice().isEmpty()) {
                List<String> userChoices = question.getUserchoice();
                List<String> correctAns = question.getAnswer();
                boolean isCorrect = userChoices.size() == correctAns.size() && correctAns.containsAll(userChoices);
                // Apply color based on correctness (for review mode)
            }
        }
    }
    
    private void showFallbackResult() {
        quest_content.setText("Quiz Completed!");
        disableAllOptions();
        
        for (Button button : optionButtons) {
            button.setVisible(false);
        }
        
        quiz.evaluateAnswers();
        int totalQuestion = quiz.getAllQuestions().size();
        int correctQuest = quiz.countCorrectQuest();
        double accuracyRate = (double) correctQuest / totalQuestion;
        double finalScore = accuracyRate * 10;
        String formattedScore = String.format("%.2f", finalScore);
        
        userPoint.setText("Your score: " + formattedScore + " (" + correctQuest + "/" + totalQuestion + ")");
    }

    private void disableAllOptions() {
        for (Button button : optionButtons) {
            button.setDisable(true);
        }
    }

    private void enableAllOptions() {
        for (Button button : optionButtons) {
            button.setDisable(false);
        }
    }
    
    // Métodos antigos mantidos para compatibilidade
    private void disableOptions() {
        disableAllOptions();
    }

    private void enableOptions() {
        enableAllOptions();
    }
}