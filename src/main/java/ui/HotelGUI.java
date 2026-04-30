package ui;

import hotel.CheckInSystem;
import hotel.Room;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;

public class HotelGUI extends Application {
    private CheckInSystem system;
    private BorderPane rootLayout;
    private TextArea displayArea;

    @Override
    public void start(Stage primaryStage) {
        system = new CheckInSystem();

        rootLayout = new BorderPane();
        // Load the initial page with the animation!
        switchPage(createLandingPage());

        Scene mainScene = new Scene(rootLayout, 900, 600);

        URL cssUrl = getClass().getResource("/assets/style.css");
        if (cssUrl != null) {
            mainScene.getStylesheets().add(cssUrl.toExternalForm());
        } else {
            System.out.println("Warning: Could not find style.css");
        }

        primaryStage.setTitle("Morvath Hotel Management Terminal");
        primaryStage.setScene(mainScene);
        primaryStage.show();
    }

    // --- THE ANIMATION ENGINE ---
    private void switchPage(Node page) {
        rootLayout.setCenter(page);

        // 1. Fade In
        FadeTransition fade = new FadeTransition(Duration.millis(400), page);
        fade.setFromValue(0.0);
        fade.setToValue(1.0);

        // 2. Slide Up
        TranslateTransition slide = new TranslateTransition(Duration.millis(400), page);
        slide.setFromY(20); // Starts 20 pixels down
        slide.setToY(0); // Slides to its normal position

        // Play them both at the exact same time
        ParallelTransition transition = new ParallelTransition(fade, slide);
        transition.play();
    }

    // ==========================================
    // PAGE 1: LANDING
    // ==========================================
    private VBox createLandingPage() {
        VBox layout = new VBox(30);
        layout.setAlignment(Pos.CENTER);

        Label title = new Label("The Grand Morvath");
        title.getStyleClass().add("title-label");

        try {
            URL gifUrl = getClass().getResource("/assets/landing.gif");
            if (gifUrl != null) {
                ImageView gifView = new ImageView(new Image(gifUrl.toExternalForm()));
                layout.getChildren().add(gifView);
            }
        } catch (Exception e) {
            System.out.println("No landing.gif found, skipping image.");
        }

        Button enterBtn = new Button("Enter Terminal");
        enterBtn.getStyleClass().add("dashboard-button");

        enterBtn.setOnAction(e -> {
            updateDashboard();
            switchPage(createDashboardPage()); // Animated transition
        });

        layout.getChildren().addAll(title, enterBtn);
        return layout;
    }

    // ==========================================
    // PAGE 2: DASHBOARD
    // ==========================================
    private BorderPane createDashboardPage() {
        BorderPane layout = new BorderPane();
        layout.setPadding(new Insets(20));

        Label header = new Label("Reception Dashboard");
        header.getStyleClass().add("header-label");
        layout.setTop(header);
        BorderPane.setMargin(header, new Insets(0, 0, 15, 0));

        displayArea = new TextArea();
        displayArea.setEditable(false);
        displayArea.getStyleClass().add("display-area");

        VBox sidebar = new VBox(15);
        sidebar.setPadding(new Insets(0, 0, 0, 20));
        sidebar.setPrefWidth(220);

        Button checkInBtn = new Button("Check-In Menu");
        Button updateBtn = new Button("Update Status");
        Button checkOutBtn = new Button("Check-Out Menu");
        Button logoutBtn = new Button("Logout");

        Button[] buttons = { checkInBtn, updateBtn, checkOutBtn, logoutBtn };
        for (Button btn : buttons) {
            btn.getStyleClass().add("dashboard-button");
            btn.setMaxWidth(Double.MAX_VALUE);
        }

        // All routing now uses the animated switchPage method!
        checkInBtn.setOnAction(e -> switchPage(createCheckInPage()));
        updateBtn.setOnAction(e -> switchPage(createUpdatePage()));
        checkOutBtn.setOnAction(e -> switchPage(createCheckOutPage()));
        logoutBtn.setOnAction(e -> switchPage(createLandingPage()));

        sidebar.getChildren().addAll(checkInBtn, updateBtn, checkOutBtn, new Region(), logoutBtn);
        VBox.setVgrow(sidebar.getChildren().get(3), Priority.ALWAYS);

        layout.setCenter(displayArea);
        layout.setRight(sidebar);

        // FIX: Update the text right after the text area is created!
        updateDashboard();

        return layout;
    }

    // ==========================================
    // PAGE 3: CHECK-IN FORM
    // ==========================================
    private VBox createCheckInPage() {
        VBox layout = new VBox(15);
        layout.setAlignment(Pos.CENTER);
        layout.setMaxWidth(400);

        Label title = new Label("Guest Check-In");
        title.getStyleClass().add("header-label");

        VBox nameBox = createInputBox("Guest Name:");
        TextField nameField = (TextField) nameBox.getChildren().get(1);

        VBox roomBox = createInputBox("Specific Room (Leave blank for Random):");
        TextField roomField = (TextField) roomBox.getChildren().get(1);

        HBox btnRow = new HBox(15);
        btnRow.setAlignment(Pos.CENTER);
        Button cancelBtn = new Button("Cancel");
        Button confirmBtn = new Button("Confirm");

        cancelBtn.getStyleClass().add("dashboard-button");
        confirmBtn.getStyleClass().add("dashboard-button");

        cancelBtn.setOnAction(e -> switchPage(createDashboardPage()));

        confirmBtn.setOnAction(e -> {
            String name = nameField.getText().trim();
            String roomText = roomField.getText().trim();
            Room r = null;

            if (roomText.isEmpty()) {
                r = system.processCheckInRandomRoom(name);
            } else {
                try {
                    r = system.processCheckInSpecificRoom(name, Integer.parseInt(roomText));
                } catch (NumberFormatException ex) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Please enter a valid room number.");
                    return;
                }
            }

            if (r != null) {
                showAlert(Alert.AlertType.INFORMATION, "Success",
                        "Welcome!\nRoom Amenities:\n" + r.amenities.getStatus());
                updateDashboard();
                switchPage(createDashboardPage());
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Room unavailable or does not exist.");
            }
        });

        btnRow.getChildren().addAll(cancelBtn, confirmBtn);
        layout.getChildren().addAll(title, nameBox, roomBox, btnRow);
        return layout;
    }

    // ==========================================
    // PAGE 4: UPDATE ROOM FORM
    // ==========================================
    private VBox createUpdatePage() {
        VBox layout = new VBox(15);
        layout.setAlignment(Pos.CENTER);
        layout.setMaxWidth(300);

        Label title = new Label("Update Room");
        title.getStyleClass().add("header-label");

        VBox roomBox = createInputBox("Room Number:");
        TextField roomNum = (TextField) roomBox.getChildren().get(1);

        CheckBox clean = new CheckBox("Room is Clean");
        CheckBox stock = new CheckBox("Supplies are Stocked");
        CheckBox pool = new CheckBox("Pool Access");
        CheckBox gym = new CheckBox("Gym Access");
        CheckBox rest = new CheckBox("Restaurant Access");

        HBox btnRow = new HBox(15);
        btnRow.setAlignment(Pos.CENTER);
        Button cancelBtn = new Button("Cancel");
        Button saveBtn = new Button("Save Changes");

        cancelBtn.getStyleClass().add("dashboard-button");
        saveBtn.getStyleClass().add("dashboard-button");

        cancelBtn.setOnAction(e -> switchPage(createDashboardPage()));

        saveBtn.setOnAction(e -> {
            try {
                int rNum = Integer.parseInt(roomNum.getText().trim());
                system.updateRoomStatus(rNum, clean.isSelected(), stock.isSelected(), pool.isSelected(),
                        gym.isSelected(), rest.isSelected());
                updateDashboard();
                switchPage(createDashboardPage());
            } catch (NumberFormatException ex) {
                showAlert(Alert.AlertType.ERROR, "Error", "Please enter a valid room number.");
            }
        });

        btnRow.getChildren().addAll(cancelBtn, saveBtn);
        layout.getChildren().addAll(title, roomBox, clean, stock, pool, gym, rest, btnRow);
        return layout;
    }

    // ==========================================
    // PAGE 5: CHECK-OUT FORM
    // ==========================================
    private VBox createCheckOutPage() {
        VBox layout = new VBox(15);
        layout.setAlignment(Pos.CENTER);
        layout.setMaxWidth(300);

        Label title = new Label("Guest Check-Out");
        title.getStyleClass().add("header-label");

        VBox roomBox = createInputBox("Room Number to Vacate:");
        TextField roomField = (TextField) roomBox.getChildren().get(1);

        HBox btnRow = new HBox(15);
        btnRow.setAlignment(Pos.CENTER);
        Button cancelBtn = new Button("Cancel");
        Button confirmBtn = new Button("Confirm");

        cancelBtn.getStyleClass().add("dashboard-button");
        confirmBtn.getStyleClass().add("dashboard-button");

        cancelBtn.setOnAction(e -> switchPage(createDashboardPage()));

        confirmBtn.setOnAction(e -> {
            try {
                int roomNum = Integer.parseInt(roomField.getText().trim());
                Room room = system.getRoom(roomNum);

                if (room == null) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Room " + roomNum + " does not exist.");
                } else if (room.isAvailable) {
                    showAlert(Alert.AlertType.INFORMATION, "Info", "Room " + roomNum + " is already empty.");
                } else {
                    system.processCheckout(roomNum);
                    showAlert(Alert.AlertType.INFORMATION, "Success",
                            "Check-out successful.\nRoom requires cleaning and restocking.");
                    updateDashboard();
                    switchPage(createDashboardPage());
                }
            } catch (NumberFormatException ex) {
                showAlert(Alert.AlertType.ERROR, "Error", "Please enter a valid room number.");
            }
        });

        btnRow.getChildren().addAll(cancelBtn, confirmBtn);
        layout.getChildren().addAll(title, roomBox, btnRow);
        return layout;
    }

    // --- UTILITIES ---
    private VBox createInputBox(String labelText) {
        VBox box = new VBox(5);
        Label label = new Label(labelText);
        label.getStyleClass().add("form-label");
        TextField field = new TextField();
        field.getStyleClass().add("text-field");
        box.getChildren().addAll(label, field);
        return box;
    }

    private void updateDashboard() {
        if (displayArea != null) {
            displayArea.setText(system.getFullDashboardText());
        }
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
