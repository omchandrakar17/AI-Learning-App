package ui;

import javafx.animation.ScaleTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import database.UserManager;
import database.HistoryManager;
import database.SaveManager;

public class AccountPage extends BorderPane {

    private Stage stage;

    public AccountPage(Stage stage) {

        this.stage = stage;
        this.getStyleClass().add("home-root");

        // ===== GET CURRENT USER =====
        User user = UserManager.getCurrentUser();

        String userName = user != null ? user.getName() : "Guest";
        String userEmail = user != null ? user.getEmail() : "No Email";
        String userAge = user != null ? String.valueOf(user.getAge()) : "-";

        int historyCount = HistoryManager.getHistoryCount();
        int savedCount = SaveManager.getSavedCount();

        // ===== TOP BAR =====
        Button backBtn = new Button("← Back");
        backBtn.getStyleClass().add("back-btn");

        backBtn.setOnMouseEntered(e -> animateScale(backBtn,1.05));
        backBtn.setOnMouseExited(e -> animateScale(backBtn,1.0));

        backBtn.setOnAction(e ->
                stage.getScene().setRoot(new HomePage(stage))
        );

        Label title = new Label("👤 Profile");
        title.setStyle("-fx-text-fill:white;-fx-font-size:20px;-fx-font-weight:bold;");

        HBox topBar = new HBox(20, backBtn, title);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(20,40,10,40));
        topBar.setStyle("-fx-border-color: rgba(255,255,255,0.05); -fx-border-width:0 0 1 0;");

        setTop(topBar);

        // ===== CENTER CONTAINER =====
        VBox centerContainer = new VBox();
        centerContainer.setAlignment(Pos.CENTER);
        centerContainer.setPadding(new Insets(40));

        VBox accountCard = new VBox(25);
        accountCard.setAlignment(Pos.CENTER);
        accountCard.setMaxWidth(450);
        accountCard.getStyleClass().add("account-card");

        // ===== AVATAR =====
        StackPane avatarPane = new StackPane();
        avatarPane.setPrefSize(100,100);
        avatarPane.setMaxSize(100,100);
        avatarPane.getStyleClass().add("account-avatar");

        Label initialLabel =
                new Label(userName.substring(0,1).toUpperCase());

        initialLabel.setStyle(
                "-fx-text-fill:white;" +
                        "-fx-font-size:40px;" +
                        "-fx-font-weight:bold;"
        );

        avatarPane.getChildren().add(initialLabel);

        // ===== USER INFO =====
        Label nameLabel = new Label(userName);
        nameLabel.setStyle(
                "-fx-text-fill:white;" +
                        "-fx-font-size:24px;" +
                        "-fx-font-weight:bold;"
        );

        Label emailLabel = new Label(userEmail);
        emailLabel.setStyle("-fx-text-fill:#94a3b8;-fx-font-size:14px;");

        Label ageLabel = new Label("Age: " + userAge);
        ageLabel.setStyle("-fx-text-fill:#94a3b8;-fx-font-size:14px;");

        VBox infoBox = new VBox(6,nameLabel,emailLabel,ageLabel);
        infoBox.setAlignment(Pos.CENTER);

        // ===== STATS =====
        HBox statsRow = new HBox(20);
        statsRow.setAlignment(Pos.CENTER);

        statsRow.getChildren().addAll(

                createStatBox("Plan","Free Tier"),
                createStatBox("History",String.valueOf(historyCount)),
                createStatBox("Saved",String.valueOf(savedCount))

        );

        // ===== BUTTONS =====
        Button historyBtn = new Button("View History");
        historyBtn.getStyleClass().add("search-button");

        historyBtn.setOnAction(e ->
                stage.getScene().setRoot(new HistoryPage(stage))
        );

        Button savedBtn = new Button("Saved Chats");
        savedBtn.getStyleClass().add("search-button");

        savedBtn.setOnAction(e ->
                stage.getScene().setRoot(new SavedPage(stage))
        );

        Button logoutBtn = new Button("Log Out");
        logoutBtn.getStyleClass().add("logout-button");

        logoutBtn.setOnMouseEntered(e -> animateScale(logoutBtn,1.05));
        logoutBtn.setOnMouseExited(e -> animateScale(logoutBtn,1.0));

        logoutBtn.setOnAction(e -> {

            session.SessionManager.logout();
            stage.getScene().setRoot(new LoginPage(stage));

        });

        VBox actionButtons =
                new VBox(12, historyBtn, savedBtn, logoutBtn);

        actionButtons.setAlignment(Pos.CENTER);

        accountCard.getChildren().addAll(
                avatarPane,
                infoBox,
                statsRow,
                actionButtons
        );

        centerContainer.getChildren().add(accountCard);

        setCenter(centerContainer);
    }

    private VBox createStatBox(String title,String value){

        VBox box = new VBox(5);
        box.setAlignment(Pos.CENTER);
        box.setPrefWidth(120);

        box.getStyleClass().add("account-stat-box");

        Label titleLabel = new Label(title);
        titleLabel.setStyle(
                "-fx-text-fill:#64748b;" +
                        "-fx-font-size:12px;" +
                        "-fx-font-weight:bold;"
        );

        Label valueLabel = new Label(value);
        valueLabel.setStyle(
                "-fx-text-fill:white;" +
                        "-fx-font-size:16px;" +
                        "-fx-font-weight:bold;"
        );

        box.getChildren().addAll(titleLabel,valueLabel);

        return box;
    }

    private void animateScale(Button btn,double scale){

        ScaleTransition st =
                new ScaleTransition(Duration.millis(150),btn);

        st.setToX(scale);
        st.setToY(scale);
        st.play();
    }

}
