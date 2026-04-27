package ui;

import database.DatabaseManager;
import database.UserManager;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Mainapp extends Application {

    @Override
    public void start(Stage primaryStage) {

        Scene scene;

        if (UserManager.getCurrentUser() != null) {
            scene = new Scene(new HomePage(primaryStage), 1200, 800);
        } else {
            scene = new Scene(new LoginPage(primaryStage), 1200, 800);
        }

        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());

        try {
            primaryStage.getIcons().add(
                    new Image(getClass().getResourceAsStream("/Resource/logo.png"))
            );
        } catch (Exception e) {
            System.out.println("Logo not found");
        }

        primaryStage.setTitle("AI Learning Platform");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(800);
        primaryStage.setMinHeight(600);
        primaryStage.show();
    }

    public static void main(String[] args) {

        DatabaseManager.getConnection();

        launch(args);
    }
}