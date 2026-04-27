package ui;

import database.UserManager;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SignupPage extends VBox {

    private Label title;
    private TextField name;
    private TextField age;
    private TextField email;
    private PasswordField password;
    private Button createBtn;
    private Button backBtn;
    private Label status;

    public SignupPage(Stage stage) {
        setSpacing(25);
        setAlignment(Pos.CENTER);
        this.setStyle("-fx-background-color: #09090B;");

        title = new Label("Create Account");
        title.setStyle("-fx-font-size: 32px; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-family: 'Segoe UI';");

        name = new TextField();
        name.setPromptText("Full Name");
        name.setMaxWidth(300);
        name.setPrefHeight(45);
        name.setStyle(
                "-fx-background-color: rgba(255,255,255,0.04); " +
                        "-fx-text-fill: white; " +
                        "-fx-background-radius: 10; " +
                        "-fx-border-color: rgba(255,255,255,0.1); " +
                        "-fx-border-radius: 10; " +
                        "-fx-padding: 0 15;"
        );

        age = new TextField();
        age.setPromptText("Age");
        age.setMaxWidth(300);
        age.setPrefHeight(45);
        age.setStyle(
                "-fx-background-color: rgba(255,255,255,0.04); " +
                        "-fx-text-fill: white; " +
                        "-fx-background-radius: 10; " +
                        "-fx-border-color: rgba(255,255,255,0.1); " +
                        "-fx-border-radius: 10; " +
                        "-fx-padding: 0 15;"
        );

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

        createBtn = new Button("Create Account");
        createBtn.setPrefWidth(300);
        createBtn.setPrefHeight(45);
        createBtn.setStyle(
                "-fx-background-color: linear-gradient(to right, #8b5cf6, #c084fc); " +
                        "-fx-text-fill: white; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-radius: 10; " +
                        "-fx-cursor: hand;"
        );

        backBtn = new Button("Already have an account? Login");
        backBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #94a3b8; -fx-cursor: hand;");
        backBtn.setOnAction(e -> stage.getScene().setRoot(new LoginPage(stage)));

        status = new Label();
        status.setStyle("-fx-text-fill: #ef4444; -fx-font-size: 13px;");

        createBtn.setOnAction(e -> {
            try {
                boolean success = UserManager.signup(
                        name.getText(),
                        Integer.parseInt(age.getText()),
                        email.getText(),
                        password.getText()
                );

                if (success) {
                    status.setText("Account created! Please login.");
                    stage.getScene().setRoot(new LoginPage(stage));
                } else {
                    status.setText("Email already exists.");
                }
            } catch (Exception ex) {
                status.setText("Please enter valid age.");
            }
        });

        getChildren().addAll(title, name, age, email, password, createBtn, backBtn, status);
    }
}