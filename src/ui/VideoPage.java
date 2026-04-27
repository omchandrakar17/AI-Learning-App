package ui;

import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.util.Duration;

// ✅ IMPORT YOUR SERVICE HERE
import ui.services.AIVideoService;

import java.io.File;

public class VideoPage extends BorderPane {

    private Stage stage;
    private String topic;
    private TextField promptField;
    private StackPane displayArea;
    private MediaPlayer mediaPlayer;

    public VideoPage(Stage stage, String topic) {
        this.stage = stage;
        this.topic = topic;

        this.getStyleClass().add("home-root");

        Button backBtn = new Button("← Back");
        backBtn.getStyleClass().add("back-btn");
        backBtn.setOnAction(e -> {
            if (mediaPlayer != null) mediaPlayer.stop();
            stage.getScene().setRoot(new Homepage(stage));
        });

        Label title = new Label("▷ AI Video Generator - " + topic);
        title.setStyle("-fx-text-fill: white; -fx-font-size: 20px; -fx-font-weight: bold;");

        HBox topBar = new HBox(20, backBtn, title);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(20, 40, 10, 40));
        topBar.setStyle("-fx-border-color: rgba(255, 255, 255, 0.05); -fx-border-width: 0 0 1 0;");

        displayArea = new StackPane();
        displayArea.setPadding(new Insets(40));

        VBox placeholder = new VBox(15);
        placeholder.setAlignment(Pos.CENTER);
        placeholder.setStyle("-fx-background-color: rgba(255,255,255,0.02); -fx-border-style: dashed; -fx-border-color: rgba(255,255,255,0.2); -fx-border-radius: 20;");
        placeholder.getChildren().addAll(new Label("🎬"), new Label("Ready to animate: " + topic));
        displayArea.getChildren().add(placeholder);

        promptField = new TextField(topic);
        promptField.getStyleClass().add("media-input-field");
        HBox.setHgrow(promptField, Priority.ALWAYS);

        Button genBtn = new Button("Generate Video ▷");
        genBtn.getStyleClass().add("media-generate-btn");

        genBtn.setOnAction(e -> handleVideoGeneration());
        promptField.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) handleVideoGeneration();
        });

        HBox controlBar = new HBox(15, promptField, genBtn);
        controlBar.setAlignment(Pos.CENTER);
        controlBar.getStyleClass().add("media-input-container");

        this.setTop(topBar);
        this.setCenter(displayArea);
        this.setBottom(controlBar);
    }

    private void handleVideoGeneration() {
        String prompt = promptField.getText().trim();
        if (prompt.isEmpty()) return;

        ProgressIndicator spinner = new ProgressIndicator();
        spinner.setStyle("-fx-progress-color: #c084fc;");
        VBox loadingBox = new VBox(15, spinner, new Label("Creating video (approx 20s)..."));
        loadingBox.setAlignment(Pos.CENTER);
        displayArea.getChildren().setAll(loadingBox);

        new Thread(() -> {
            try {
                // ✅ THIS CALLS THE SERVICE AND REMOVES THE "NO USAGE" ERROR
                String result = AIVideoService.generateVideo(prompt);

                Platform.runLater(() -> {
                    if (result.startsWith("Video saved")) {
                        playVideo("generated_video.mp4");
                    } else {
                        Label errorLabel = new Label(result);
                        errorLabel.setStyle("-fx-text-fill: #ef4444;");
                        displayArea.getChildren().setAll(errorLabel);
                    }
                });
            } catch (Exception e) {
                Platform.runLater(() -> displayArea.getChildren().setAll(new Label("Failed to connect.")));
            }
        }).start();
    }

    private void playVideo(String path) {
        try {
            File file = new File(path);
            Media media = new Media(file.toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            MediaView mediaView = new MediaView(mediaPlayer);

            mediaView.setFitWidth(500);
            mediaView.setPreserveRatio(true);

            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            mediaPlayer.play();

            displayArea.getChildren().setAll(mediaView);
        } catch (Exception e) {
            displayArea.getChildren().setAll(new Label("Playback Error: " + e.getMessage()));
        }
    }
}