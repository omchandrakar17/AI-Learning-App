package ui;

import database.HistoryManager;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class Homepage extends BorderPane {

    private Stage stage;
    private TextField searchField;
    private String selectedMode = null;
    private Button searchButton;
    private List<VBox> allCards = new ArrayList<>();

    public Homepage(Stage stage) {

        this.stage = stage;

        this.getStyleClass().add("home-root");

        Button accountBtn = new Button("👤 Account");
        accountBtn.getStyleClass().add("account-btn");
        accountBtn.setOnAction(e -> stage.getScene().setRoot(new AccountPage(stage)));

        HBox topNav = new HBox(accountBtn);
        topNav.setAlignment(Pos.TOP_RIGHT);
        topNav.setPadding(new Insets(20, 40, 0, 0));

        setTop(topNav);

        VBox centerContent = new VBox();
        centerContent.setSpacing(45);
        centerContent.setAlignment(Pos.CENTER);
        centerContent.setPadding(new Insets(20, 50, 50, 50));

        VBox header = new VBox(8);
        header.setAlignment(Pos.CENTER);

        Label title = new Label("AI Learning Hub");
        title.getStyleClass().add("main-title");

        Label subtitle = new Label("What would you like to explore today?");
        subtitle.setStyle("-fx-font-size: 16px; -fx-text-fill: #A1A1AA;");

        header.getChildren().addAll(title, subtitle);
        addFadeInAnimation(header, 0);

        searchField = new TextField();
        searchField.setPromptText("Enter topic...");
        searchField.setMaxWidth(650);
        searchField.setMinHeight(55);
        searchField.getStyleClass().add("search-input");

        addFadeInAnimation(searchField, 150);

        HBox cardsContainer = new HBox(25);
        cardsContainer.setAlignment(Pos.CENTER);

        VBox chatCard = createActionCard("✧", "AI Chat");
        VBox imageCard = createActionCard("▨", "Images");
        VBox videoCard = createActionCard("▷", "Videos");

        allCards.addAll(List.of(chatCard, imageCard, videoCard));
        cardsContainer.getChildren().addAll(allCards);

        addFadeInAnimation(cardsContainer, 300);

        searchButton = new Button("Search");
        searchButton.setPrefWidth(200);
        searchButton.setPrefHeight(45);
        searchButton.getStyleClass().add("search-button");
        searchButton.setDisable(true);

        searchButton.setOnAction(e -> handleSearch());

        addFadeInAnimation(searchButton, 450);

        centerContent.getChildren().addAll(header, searchField, cardsContainer, searchButton);

        setCenter(centerContent);
    }

    private VBox createActionCard(String icon, String modeName) {

        Label iconLabel = new Label(icon);
        Label titleLabel = new Label(modeName);

        VBox card = new VBox(12, iconLabel, titleLabel);
        card.setAlignment(Pos.CENTER);
        card.setPrefSize(180, 190);
        card.getStyleClass().add("action-card");

        card.setOnMouseClicked(e -> {
            selectedMode = modeName;
            searchButton.setDisable(false);
        });

        return card;
    }

    private void handleSearch() {

        String topic = searchField.getText().trim();

        if (topic.isEmpty()) {
            showAlert("Enter topic first");
            return;
        }

        // ✅ FIXED
        HistoryManager.addTopic(topic);

        switch (selectedMode) {
            case "AI Chat":
                stage.getScene().setRoot(new ChatPage(stage, topic));
                break;
            case "Images":
                stage.getScene().setRoot(new Imagepage(stage, topic));
                break;
            case "Videos":
                stage.getScene().setRoot(new VideoPage(stage, topic));
                break;
        }
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setContentText(msg);
        alert.show();
    }

    private void addFadeInAnimation(Node node, int delay) {
        FadeTransition ft = new FadeTransition(Duration.millis(800), node);
        ft.setFromValue(0);
        ft.setToValue(1);
        ft.setDelay(Duration.millis(delay));
        ft.play();
    }
}