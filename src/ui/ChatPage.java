package ui;

import database.SaveManager;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import ui.services.AIChatService;
public class ChatPage extends BorderPane {

    private Stage stage;
    private String topic;

    private VBox chatContainer;
    private ScrollPane scrollPane;
    private TextField inputField;
    private HBox typingIndicatorBox;

    // Stores the entire chat history
    private StringBuilder fullChatHistory;

    public ChatPage(Stage stage, String topic) {
        this.stage = stage;
        this.topic = topic;
        this.fullChatHistory = new StringBuilder();

        fullChatHistory.append("=== Chat History: ").append(topic).append(" ===\n\n");

        this.getStyleClass().add("home-root");

        // ===== TOP BAR =====
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

        Label title = new Label("✧ AI Chat - " + topic);
        title.setStyle("-fx-text-fill: white; -fx-font-size: 20px; -fx-font-weight: bold;");

        Button saveBtn = new Button("⭐ Save Chat");
        saveBtn.getStyleClass().add("account-btn");

        saveBtn.setOnAction(e -> {

            // Save full chat in database
            SaveManager.saveContent(topic, fullChatHistory.toString());

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText("Complete chat saved successfully!");
            alert.show();
        });

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox topBar = new HBox(20, backBtn, title, spacer, saveBtn);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(20, 40, 10, 40));
        topBar.setStyle("-fx-border-color: rgba(255, 255, 255, 0.05); -fx-border-width: 0 0 1 0;");

        // ===== CHAT AREA =====
        chatContainer = new VBox(20);
        chatContainer.setPadding(new Insets(20, 40, 20, 40));
        chatContainer.setStyle("-fx-background-color: transparent;");

        scrollPane = new ScrollPane(chatContainer);
        scrollPane.getStyleClass().add("chat-scroll-pane");
        scrollPane.setFitToWidth(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        addAIMessage("Hi 👋 Let’s learn about " + topic + "! Ask me anything.");
        addUserMessage(topic);
        addTypingIndicator();

        new Thread(() -> {
            try { Thread.sleep(1000); } catch (Exception ignored) {}
            Platform.runLater(() -> {
                removeTypingIndicator();
                addAIMessage(AIChatService.getResponse(topic));
            });
        }).start();

        // ===== INPUT AREA =====
        inputField = new TextField();
        inputField.setPromptText("Ask something...");
        inputField.getStyleClass().add("chat-input-field");
        HBox.setHgrow(inputField, Priority.ALWAYS);

        Button sendBtn = new Button("Send 🚀");
        sendBtn.getStyleClass().add("chat-send-btn");

        sendBtn.setOnAction(e -> sendMessage());

        inputField.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                sendMessage();
            }
        });

        HBox inputBar = new HBox(15, inputField, sendBtn);
        inputBar.setAlignment(Pos.CENTER);
        inputBar.getStyleClass().add("chat-input-container");

        this.setTop(topBar);
        this.setCenter(scrollPane);
        this.setBottom(inputBar);
    }

    private void sendMessage() {

        String userText = inputField.getText().trim();
        if (userText.isEmpty()) return;

        addUserMessage(userText);
        inputField.clear();

        addTypingIndicator();

        new Thread(() -> {

            try {
                Thread.sleep(1200);
            } catch (InterruptedException ignored) {}

            Platform.runLater(() -> {

                removeTypingIndicator();

                String response = AIChatService.getResponse(userText);

                addAIMessage(response);

            });

        }).start();
    }

    private void addUserMessage(String text) {

        fullChatHistory.append("You: ").append(text).append("\n\n");

        Label message = new Label(text);
        message.setWrapText(true);
        message.setMaxWidth(600);
        message.getStyleClass().add("chat-bubble-user");

        HBox box = new HBox(message);
        box.setAlignment(Pos.CENTER_RIGHT);

        chatContainer.getChildren().add(box);

        scrollToBottom();
    }

    private void addAIMessage(String text) {

        fullChatHistory.append("AI: ").append(text).append("\n\n");

        Label message = new Label(text);
        message.setWrapText(true);
        message.setMaxWidth(600);
        message.getStyleClass().add("chat-bubble-ai");

        HBox box = new HBox(message);
        box.setAlignment(Pos.CENTER_LEFT);

        chatContainer.getChildren().add(box);

        scrollToBottom();
    }

    private void addTypingIndicator() {

        Label typingLabel = new Label("AI is typing...");
        typingLabel.setStyle("-fx-text-fill: #A1A1AA; -fx-font-style: italic; -fx-font-size: 13px;");

        typingIndicatorBox = new HBox(typingLabel);
        typingIndicatorBox.setAlignment(Pos.CENTER_LEFT);
        typingIndicatorBox.setPadding(new Insets(0, 0, 0, 10));

        chatContainer.getChildren().add(typingIndicatorBox);

        scrollToBottom();
    }

    private void removeTypingIndicator() {

        if (typingIndicatorBox != null) {
            chatContainer.getChildren().remove(typingIndicatorBox);
            typingIndicatorBox = null;
        }
    }

    private void scrollToBottom() {

        Platform.runLater(() -> {
            scrollPane.layout();
            scrollPane.setVvalue(1.0);
        });
    }
}