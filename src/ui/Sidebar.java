package ui;

import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Sidebar extends VBox {


    public Sidebar(Stage stage) {

        setSpacing(10);

        Button home = new Button("Home");
        Button history = new Button("History");
        Button saved = new Button("Saved");
        Button profile = new Button("Profile");

        home.setOnAction(e ->
                stage.getScene().setRoot(new HomePage(stage)));

        history.setOnAction(e ->
                stage.getScene().setRoot(new HistoryPage(stage)));

        saved.setOnAction(e ->
                stage.getScene().setRoot(new SavedPage(stage)));

        profile.setOnAction(e ->
                stage.getScene().setRoot(new ProfilePage(stage)));

        getChildren().addAll(home, history, saved, profile);
    }


}
