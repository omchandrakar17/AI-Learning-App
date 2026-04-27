package ui;

import database.HistoryManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class HistoryPage extends VBox {

    public HistoryPage(Stage stage) {

        setSpacing(15);
        setPadding(new Insets(30));
        setAlignment(Pos.TOP_CENTER);
        getStyleClass().add("home-root");

        Label title = new Label("📜 Learning History");
        title.setStyle("-fx-text-fill:white; -fx-font-size:22px; -fx-font-weight:bold;");

        Button backBtn = new Button("← Back");
        backBtn.getStyleClass().add("back-btn");

        backBtn.setOnAction(e -> stage.getScene().setRoot(new AccountPage(stage)));

        getChildren().addAll(title, backBtn);

        for (String topic : HistoryManager.getHistory()) {

            Label item = new Label(topic);
            item.setStyle("-fx-text-fill:white; -fx-font-size:16px;");

            getChildren().add(item);
        }

        if (HistoryManager.getHistory().isEmpty()) {

            Label empty = new Label("No history yet.");
            empty.setStyle("-fx-text-fill:gray;");

            getChildren().add(empty);
        }
    }
}