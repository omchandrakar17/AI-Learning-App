package ui;

import database.SaveManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SavedPage extends VBox {

    public SavedPage(Stage stage) {

        setSpacing(15);
        setPadding(new Insets(30));
        setAlignment(Pos.TOP_CENTER);
        getStyleClass().add("home-root");

        Label title = new Label("⭐ Saved Chats");
        title.setStyle("-fx-text-fill:white; -fx-font-size:22px; -fx-font-weight:bold;");

        Button backBtn = new Button("← Back");
        backBtn.getStyleClass().add("back-btn");

        backBtn.setOnAction(e -> stage.getScene().setRoot(new AccountPage(stage)));

        getChildren().addAll(title, backBtn);

        for (String chat : SaveManager.getSavedChats()) {

            Label item = new Label(chat);
            item.setWrapText(true);
            item.setMaxWidth(600);
            item.setStyle("-fx-text-fill:white; -fx-font-size:15px;");

            getChildren().add(item);
        }

        if (SaveManager.getSavedChats().isEmpty()) {

            Label empty = new Label("No saved chats yet.");
            empty.setStyle("-fx-text-fill:gray;");

            getChildren().add(empty);
        }
    }
}