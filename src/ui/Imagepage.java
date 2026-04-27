package ui;

import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Imagepage extends BorderPane {

    private Stage stage;
    private String topic;
    private TextField promptField;
    private StackPane displayArea;

    public Imagepage(Stage stage, String topic) {
        this.stage = stage;
        this.topic = topic;

        this.getStyleClass().add("home-root");

        Button backBtn = new Button("← Back");
        backBtn.getStyleClass().add("back-btn");

        backBtn.setOnMouseEntered(e -> {
            ScaleTransition scaleUp = new ScaleTransition(Duration.millis(150), backBtn);
            scaleUp.setToX(1.05);
            scaleUp.setToY(1.05);
            scaleUp.play();
        });
        backBtn.setOnMouseExited(e -> {
            ScaleTransition scaleDown = new ScaleTransition(Duration.millis(150), backBtn);
            scaleDown.setToX(1.0);
            scaleDown.setToY(1.0);
            scaleDown.play();
        });
        backBtn.setOnAction(e -> stage.getScene().setRoot(new HomePage(stage)));

        Label title = new Label("▨ AI Image Generator - " + topic);
        title.setStyle("-fx-text-fill: white; -fx-font-size: 20px; -fx-font-weight: bold;");

        HBox topBar = new HBox(20, backBtn, title);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(20, 40, 10, 40));
        topBar.setStyle("-fx-border-color: rgba(255, 255, 255, 0.05); -fx-border-width: 0 0 1 0;");

        displayArea = new StackPane();
        displayArea.setPadding(new Insets(40));

        VBox placeholder = new VBox(15);
        placeholder.setAlignment(Pos.CENTER);
        placeholder.setMaxSize(500, 350);
        placeholder.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.02); " +
                        "-fx-border-color: rgba(255, 255, 255, 0.1); " +
                        "-fx-border-width: 1; " +
                        "-fx-border-style: dashed; " +
                        "-fx-border-radius: 20; " +
                        "-fx-background-radius: 20;"
        );

        Label icon = new Label("🖼️");
        icon.setStyle("-fx-font-size: 50px;");

        Label hint = new Label("Waiting for prompt: " + topic);
        hint.setStyle("-fx-text-fill: #64748b; -fx-font-size: 14px;");

        placeholder.getChildren().addAll(icon, hint);
        displayArea.getChildren().add(placeholder);

        promptField = new TextField(topic);
        promptField.setPromptText("Describe an image...");
        promptField.getStyleClass().add("media-input-field");
        HBox.setHgrow(promptField, Priority.ALWAYS);

        Button genBtn = new Button("Generate image ✨");
        genBtn.getStyleClass().add("media-generate-btn");

        genBtn.setOnAction(e -> handleGeneration());
        promptField.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) handleGeneration();
        });

        HBox controlBar = new HBox(15, promptField, genBtn);
        controlBar.setAlignment(Pos.CENTER);
        controlBar.getStyleClass().add("media-input-container");

        this.setTop(topBar);
        this.setCenter(displayArea);
        this.setBottom(controlBar);
    }

    private void handleGeneration() {
        String prompt = promptField.getText().trim();
        if (prompt.isEmpty()) return;

        ProgressIndicator spinner = new ProgressIndicator();
        spinner.setStyle("-fx-progress-color: #c084fc;");

        Label loadingLabel = new Label("Generating your vision...");
        loadingLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");

        VBox loadingBox = new VBox(15, spinner, loadingLabel);
        loadingBox.setAlignment(Pos.CENTER);
        displayArea.getChildren().setAll(loadingBox);

        new Thread(() -> {
            try {
                String response = ui.services.AIImageService.generateImage(prompt);

                Platform.runLater(() -> {
                    if (response.startsWith("Image saved")) {
                        try {
                            Image genImage = new Image("file:generated_image.png");
                            ImageView imageView = new ImageView(genImage);

                            imageView.setPreserveRatio(true);
                            imageView.setFitWidth(450);
                            imageView.setFitHeight(300);

                            VBox imageContainer = new VBox(imageView);
                            imageContainer.setAlignment(Pos.CENTER);
                            imageContainer.setMaxSize(470, 320);
                            imageContainer.setStyle(
                                    "-fx-background-color: rgba(255, 255, 255, 0.05); " +
                                            "-fx-border-color: rgba(255, 255, 255, 0.1); " +
                                            "-fx-border-width: 1; " +
                                            "-fx-background-radius: 15; " +
                                            "-fx-border-radius: 15; " +
                                            "-fx-padding: 10; " +
                                            "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.6), 20, 0, 0, 10);"
                            );

                            displayArea.getChildren().setAll(imageContainer);
                        } catch (Exception ex) {
                            showError("Error loading the image file into the UI.");
                        }
                    } else {
                        showError(response);
                    }
                });

            } catch (Exception e) {
                Platform.runLater(() -> showError("Network Error: " + e.getMessage()));
            }
        }).start();
    }

    private void showError(String message) {
        Label error = new Label("❌ " + message);
        error.setWrapText(true);
        error.setMaxWidth(400);
        error.setStyle("-fx-text-fill: #ef4444; -fx-font-size: 14px; -fx-text-alignment: center;");
        displayArea.getChildren().setAll(error);
    }
}