package ui;

import database.UserManager;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LoginPage extends VBox {

    private Label title;
    private TextField email;
    private PasswordField password;
    private Button loginBtn;
    private Button signupBtn;
    private Label status;

    public LoginPage(Stage stage) {
        setSpacing(25);
        setAlignment(Pos.CENTER);
        this.setStyle("-fx-background-color: #09090B;");

        title = new Label("AI Learning Platform");
        title.setStyle("-fx-font-size: 32px; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-family: 'Segoe UI';");

        email = new TextField();
        email.setPromptText("Email");
        email.setMaxWidth(300);
        email.setPrefHeight(45);
        email.setStyle(
                "-fx-background-color: rgba(255,255,255,0.04); " +
                        "-fx-text-fill: white; " +
                        "-fx-background-radius: 10; " +
                        "-fx-border-color: rgba(255,255,255,0.1); " +
                        "-fx-border-radius: 10; " +
                        "-fx-padding: 0 15;"
        );

        password = new PasswordField();
        password.setPromptText("Password");
        password.setMaxWidth(300);
        password.setPrefHeight(45);
        password.setStyle(
                "-fx-background-color: rgba(255,255,255,0.04); " +
                        "-fx-text-fill: white; " +
                        "-fx-background-radius: 10; " +
                        "-fx-border-color: rgba(255,255,255,0.1); " +
                        "-fx-border-radius: 10; " +
                        "-fx-padding: 0 15;"
        );

        loginBtn = new Button("Login");
        loginBtn.setPrefWidth(300);
        loginBtn.setPrefHeight(45);
        loginBtn.setStyle(
                "-fx-background-color: linear-gradient(to right, #8b5cf6, #c084fc); " +
                        "-fx-text-fill: white; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-radius: 10; " +
                        "-fx-cursor: hand;"
        );

        signupBtn = new Button("Don't have an account? Create one");
        signupBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #94a3b8; -fx-cursor: hand;");

        status = new Label();
        status.setStyle("-fx-text-fill: #ef4444; -fx-font-size: 13px;");

        loginBtn.setOnAction(e -> {

            boolean success = UserManager.login(
                    email.getText(),
                    password.getText()
            );

            if (success) {
                stage.getScene().setRoot(new HomePage(stage));
            } else {
                status.setText("Invalid email or password");
            }

        });

        signupBtn.setOnAction(e ->
                stage.getScene().setRoot(new SignupPage(stage))
        );

        getChildren().addAll(title, email, password, loginBtn, signupBtn, status);
    }
}