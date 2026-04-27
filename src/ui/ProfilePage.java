package ui;

import database.UserManager;
import session.SessionManager;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ProfilePage extends VBox {

    public ProfilePage(Stage stage) {

        setSpacing(20);
        setAlignment(Pos.CENTER);

        User user = UserManager.getCurrentUser();

        Label title = new Label("User Profile");
        title.setStyle("-fx-font-size:28px;-fx-font-weight:bold;");

        Label name = new Label("Name: " + user.getName());
        Label age = new Label("Age: " + user.getAge());
        Label email = new Label("Email: " + user.getEmail());

        Button logoutBtn = new Button("Logout");

        logoutBtn.setOnAction(e -> {
            SessionManager.logout();
            stage.getScene().setRoot(new LoginPage(stage));
        });

        getChildren().addAll(title, name, age, email, logoutBtn);
    }
}
