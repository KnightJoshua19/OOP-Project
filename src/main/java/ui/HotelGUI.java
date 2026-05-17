package ui;

import hotel.CheckInSystem;
import hotel.Room;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.animation.ScaleTransition;
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

import javafx.animation.SequentialTransition;
import javafx.animation.Interpolator;

import java.net.URL;
import java.util.Optional;

public class HotelGUI extends Application {
    private CheckInSystem system;
    private BorderPane rootLayout;
    // private FlowPane roomGrid;
    private VBox roomsContainer;

    // Overlay components kept as fields so we can show/hide them
    private StackPane dashboardStack; // wraps the dashboard content + overlay
    private VBox detailOverlay; // the semi-transparent backdrop
    private VBox detailPanel; // the glass card that slides in

    @Override
    public void start(Stage primaryStage) {
        system = new CheckInSystem();

        rootLayout = new BorderPane();
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

    // --- ANIMATION ENGINE ---
    private void switchPage(Node page) {
        rootLayout.setCenter(page);

        FadeTransition fade = new FadeTransition(Duration.millis(400), page);
        fade.setFromValue(0.0);
        fade.setToValue(1.0);

        TranslateTransition slide = new TranslateTransition(Duration.millis(400), page);
        slide.setFromY(20);
        slide.setToY(0);

        new ParallelTransition(fade, slide).play();
    }

    private void goToDashboard() {
        updateDashboard();
        switchPage(createDashboardPage());
    }

    // ==========================================
    // PAGE 1: CINEMATIC LANDING
    // ==========================================
    private VBox createLandingPage() {
        VBox layout = new VBox(25);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: linear-gradient(to bottom, rgba(0,0,0,0.3), rgba(0,0,0,0.8));");

        Label title = new Label("THE GRAND MORVATH");
        title.getStyleClass().add("landing-title");
        title.setOpacity(0);

        Label subtitle = new Label("A   W I Z A R D I N G   E X P E R I E N C E");
        subtitle.getStyleClass().add("landing-subtitle");
        subtitle.setOpacity(0);

        ImageView gifView = null;
        try {
            URL gifUrl = getClass().getResource("/assets/landing.gif");
            if (gifUrl != null) {
                gifView = new ImageView(new Image(gifUrl.toExternalForm()));
                gifView.setOpacity(0);
                gifView.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.6), 20, 0, 0, 10);");
            }
        } catch (Exception e) {
            System.out.println("No landing.gif found, skipping image.");
        }

        Button enterBtn = new Button("ENTER TERMINAL");
        enterBtn.getStyleClass().add("ghost-button");
        enterBtn.setOpacity(0);
        VBox.setMargin(enterBtn, new Insets(30, 0, 0, 0));

        enterBtn.setOnAction(e -> goToDashboard());

        if (gifView != null) {
            layout.getChildren().addAll(gifView, title, subtitle, enterBtn);
        } else {
            layout.getChildren().addAll(title, subtitle, enterBtn);
        }

        SequentialTransition cinematicEntry = new SequentialTransition();
        if (gifView != null)
            cinematicEntry.getChildren().add(createRevealAnim(gifView, 800));
        cinematicEntry.getChildren().add(createRevealAnim(title, 1000));
        cinematicEntry.getChildren().add(createRevealAnim(subtitle, 800));
        cinematicEntry.getChildren().add(createRevealAnim(enterBtn, 800));
        cinematicEntry.setDelay(Duration.millis(300));
        cinematicEntry.play();

        return layout;
    }

    private ParallelTransition createRevealAnim(Node node, int durationMillis) {
        FadeTransition fade = new FadeTransition(Duration.millis(durationMillis), node);
        fade.setFromValue(0);
        fade.setToValue(1);

        TranslateTransition slide = new TranslateTransition(Duration.millis(durationMillis), node);
        slide.setFromY(30);
        slide.setToY(0);
        slide.setInterpolator(Interpolator.EASE_OUT);

        return new ParallelTransition(fade, slide);
    }

    // ==========================================
    // PAGE 2: DASHBOARD
    // ==========================================
    private StackPane createDashboardPage() {
        // --- MAIN DASHBOARD LAYOUT ---
        BorderPane layout = new BorderPane();
        layout.setStyle("-fx-background-color: transparent;");

        // Sidebar
        VBox sidebar = new VBox(5);
        sidebar.getStyleClass().add("sidebar");
        sidebar.setPrefWidth(220);
        sidebar.setPadding(new Insets(30, 0, 0, 0));

        Label brand = new Label("MORVATH\nHOTEL");
        brand.setStyle(
                "-fx-text-fill: #D4AF37; -fx-font-family: 'Georgia'; -fx-font-size: 20px; -fx-font-weight: bold; -fx-text-alignment: center;");
        brand.setAlignment(Pos.CENTER);
        brand.setMaxWidth(Double.MAX_VALUE);
        VBox.setMargin(brand, new Insets(0, 0, 40, 0));

        Button viewRoomsBtn = new Button("View Rooms");
        Button checkInBtn = new Button("Check-In");
        Button updateBtn = new Button("Update Status");
        Button checkOutBtn = new Button("Check-Out");
        Button logoutBtn = new Button("Logout");

        for (Button btn : new Button[] { viewRoomsBtn, checkInBtn, updateBtn, checkOutBtn, logoutBtn }) {
            btn.getStyleClass().add("sidebar-button");
            btn.setMaxWidth(Double.MAX_VALUE);
        }

        viewRoomsBtn.setOnAction(e -> goToDashboard());
        checkInBtn.setOnAction(e -> switchPage(createCheckInPage()));
        updateBtn.setOnAction(e -> switchPage(createUpdatePage()));
        checkOutBtn.setOnAction(e -> switchPage(createCheckOutPage()));
        logoutBtn.setOnAction(e -> switchPage(createLandingPage()));

        sidebar.getChildren().addAll(brand, viewRoomsBtn, checkInBtn, updateBtn, checkOutBtn, new Region(), logoutBtn);
        VBox.setVgrow(sidebar.getChildren().get(5), Priority.ALWAYS);

        // Center: room grid
        VBox centerArea = new VBox(20);
        centerArea.setPadding(new Insets(40, 40, 40, 60));

        Label mainTitle = new Label("ROOM STATUS");
        mainTitle.getStyleClass().add("gold-title");
        mainTitle.setStyle("-fx-font-size: 42px;");

        Label hint = new Label("Click any room card to view details");
        hint.setStyle("-fx-text-fill: rgba(180,180,180,0.6); -fx-font-size: 12px; -fx-font-style: italic;");

        roomsContainer = new VBox(40); // 40px spacing between categories
        roomsContainer.setAlignment(Pos.TOP_LEFT);

        ScrollPane scrollPane = new ScrollPane(roomsContainer);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        VBox.setVgrow(scrollPane, Priority.ALWAYS);
        centerArea.getChildren().addAll(mainTitle, hint, scrollPane);

        layout.setLeft(sidebar);
        layout.setCenter(centerArea);

        // --- DETAIL OVERLAY (hidden by default) ---
        detailOverlay = new VBox();
        detailOverlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.65);");
        detailOverlay.setAlignment(Pos.CENTER);
        detailOverlay.setVisible(false);
        detailOverlay.setOpacity(0);

        detailPanel = new VBox(16);
        detailPanel.getStyleClass().add("glass-panel");
        detailPanel.setMaxWidth(420);
        detailPanel.setPadding(new Insets(36));
        detailPanel.setOpacity(0);

        detailOverlay.getChildren().add(detailPanel);

        // Clicking the dark backdrop (outside the panel) dismisses it
        detailOverlay.setOnMouseClicked(e -> {
            if (e.getTarget() == detailOverlay)
                hideDetailOverlay();
        });

        // Stack: dashboard behind, overlay on top
        dashboardStack = new StackPane(layout, detailOverlay);

        updateDashboard();
        return dashboardStack;
    }

    // ==========================================
    // ROOM DETAIL OVERLAY — SHOW
    // ==========================================
    private void showDetailOverlay(Room r) {
        detailPanel.getChildren().clear();

        // ── HEADER ──
        Label roomNumLabel = new Label("Room " + r.roomNumber);
        roomNumLabel.setStyle(
                "-fx-text-fill: #D4AF37; -fx-font-size: 28px; -fx-font-weight: bold; -fx-font-family: 'Georgia';");

        Label roomTypeLabel = new Label(r.roomType.toUpperCase());
        roomTypeLabel.setStyle("-fx-text-fill: #AAAAAA; -fx-font-size: 11px; -fx-font-weight: bold;");

        // ── AVAILABILITY STATUS ──
        String statusText;
        String statusColor;
        if (!r.isAvailable) {
            statusText = "● OCCUPIED";
            statusColor = "#F44336";
        } else if (!r.isClean || !r.suppliesStocked) {
            statusText = "● NEEDS ATTENTION";
            statusColor = "#FFA726";
        } else {
            statusText = "● AVAILABLE";
            statusColor = "#4CAF50";
        }
        Label statusLabel = new Label(statusText);
        statusLabel.setStyle("-fx-text-fill: " + statusColor + "; -fx-font-weight: bold; -fx-font-size: 14px;");

        detailPanel.getChildren().addAll(roomNumLabel, roomTypeLabel, statusLabel);

        // ── GUEST INFO (only when occupied) ──
        if (!r.isAvailable && r.currentGuestName != null && !r.currentGuestName.isEmpty()) {
            detailPanel.getChildren().add(makeDivider());

            Label guestHeader = new Label("GUEST INFORMATION");
            guestHeader.setStyle("-fx-text-fill: #D4AF37; -fx-font-size: 11px; -fx-font-weight: bold;");

            Label guestName = new Label("Name:   " + r.currentGuestName);
            guestName.setStyle("-fx-text-fill: #FFFFFF; -fx-font-size: 14px;");

            detailPanel.getChildren().addAll(guestHeader, guestName);
        }

        // ── ROOM CONDITION ──
        detailPanel.getChildren().add(makeDivider());

        Label conditionHeader = new Label("ROOM CONDITION");
        conditionHeader.setStyle("-fx-text-fill: #D4AF37; -fx-font-size: 11px; -fx-font-weight: bold;");

        Label cleanLabel = new Label(r.isClean ? "✓  Clean & Ready" : "✗  Requires Cleaning");
        cleanLabel.setStyle("-fx-text-fill: " + (r.isClean ? "#4CAF50" : "#FF7043") + "; -fx-font-size: 13px;");

        Label stockLabel = new Label(r.suppliesStocked ? "✓  Fully Stocked" : "✗  Needs Restocking");
        stockLabel.setStyle("-fx-text-fill: " + (r.suppliesStocked ? "#4CAF50" : "#FF7043") + "; -fx-font-size: 13px;");

        detailPanel.getChildren().addAll(conditionHeader, cleanLabel, stockLabel);

        // ── AMENITIES ──
        detailPanel.getChildren().add(makeDivider());

        Label amenitiesHeader = new Label("AMENITIES");
        amenitiesHeader.setStyle("-fx-text-fill: #D4AF37; -fx-font-size: 11px; -fx-font-weight: bold;");

        VBox amenityRows = new VBox(6);
        amenityRows.getChildren().addAll(
                makeAmenityRow("Pool Access", r.amenities.poolAccess),
                makeAmenityRow("Gym Access", r.amenities.gymAccess),
                makeAmenityRow("Restaurant Access", r.amenities.restaurantAccess));

        detailPanel.getChildren().addAll(amenitiesHeader, amenityRows);

        // ── CLOSE BUTTON ──
        Button closeBtn = new Button("CLOSE");
        closeBtn.getStyleClass().add("cancel-button");
        closeBtn.setOnAction(e -> hideDetailOverlay());
        VBox.setMargin(closeBtn, new Insets(12, 0, 0, 0));

        detailPanel.getChildren().add(closeBtn);

        // ── ANIMATE IN ──
        detailOverlay.setVisible(true);

        FadeTransition backdropFade = new FadeTransition(Duration.millis(200), detailOverlay);
        backdropFade.setFromValue(0);
        backdropFade.setToValue(1);

        FadeTransition panelFade = new FadeTransition(Duration.millis(250), detailPanel);
        panelFade.setFromValue(0);
        panelFade.setToValue(1);

        ScaleTransition panelScale = new ScaleTransition(Duration.millis(250), detailPanel);
        panelScale.setFromX(0.92);
        panelScale.setFromY(0.92);
        panelScale.setToX(1.0);
        panelScale.setToY(1.0);
        panelScale.setInterpolator(Interpolator.EASE_OUT);

        backdropFade.play();
        new ParallelTransition(panelFade, panelScale).play();
    }

    // ==========================================
    // ROOM DETAIL OVERLAY — HIDE
    // ==========================================
    private void hideDetailOverlay() {
        FadeTransition backdropFade = new FadeTransition(Duration.millis(200), detailOverlay);
        backdropFade.setFromValue(1);
        backdropFade.setToValue(0);
        backdropFade.setOnFinished(e -> detailOverlay.setVisible(false));

        FadeTransition panelFade = new FadeTransition(Duration.millis(150), detailPanel);
        panelFade.setFromValue(1);
        panelFade.setToValue(0);

        ScaleTransition panelScale = new ScaleTransition(Duration.millis(150), detailPanel);
        panelScale.setToX(0.95);
        panelScale.setToY(0.95);

        new ParallelTransition(panelFade, panelScale).play();
        backdropFade.play();
    }

    // ==========================================
    // OVERLAY HELPERS
    // ==========================================
    private Region makeDivider() {
        Region line = new Region();
        line.setStyle("-fx-background-color: rgba(255,255,255,0.15);");
        line.setMinHeight(1);
        VBox.setMargin(line, new Insets(4, 0, 4, 0));
        return line;
    }

    private HBox makeAmenityRow(String name, boolean enabled) {
        Label icon = new Label(enabled ? "✓" : "✗");
        icon.setStyle("-fx-text-fill: " + (enabled ? "#4CAF50" : "#555555")
                + "; -fx-font-size: 13px; -fx-font-weight: bold;");
        icon.setMinWidth(20);

        Label label = new Label(name);
        label.setStyle("-fx-text-fill: " + (enabled ? "#CCCCCC" : "#555555") + "; -fx-font-size: 13px;");

        HBox row = new HBox(10, icon, label);
        row.setAlignment(Pos.CENTER_LEFT);
        return row;
    }

    // ==========================================
    // ROOM CARDS
    // ==========================================
    private void updateDashboard() {
        if (roomsContainer != null) {
            roomsContainer.getChildren().clear();

            // Create separate lists for each room type
            java.util.List<Room> standardRooms = new java.util.ArrayList<>();
            java.util.List<Room> deluxeRooms = new java.util.ArrayList<>();
            java.util.List<Room> suiteRooms = new java.util.ArrayList<>();

            // Sort rooms into their respective lists
            for (Room r : system.getAllRooms()) {
                if (r.roomType.equals("suite")) {
                    suiteRooms.add(r);
                } else if (r.roomType.equals("deluxe")) {
                    deluxeRooms.add(r);
                } else {
                    standardRooms.add(r);
                }
            }

            // Add categories to the container (if they have rooms)
            if (!suiteRooms.isEmpty()) {
                roomsContainer.getChildren().add(createCategorySection("SUITE ROOMS", suiteRooms));
            }
            if (!deluxeRooms.isEmpty()) {
                roomsContainer.getChildren().add(createCategorySection("DELUXE ROOMS", deluxeRooms));
            }
            if (!standardRooms.isEmpty()) {
                roomsContainer.getChildren().add(createCategorySection("STANDARD ROOMS", standardRooms));
            }
        }
    }

    private VBox createCategorySection(String titleText, java.util.List<Room> rooms) {
        VBox section = new VBox(15);

        Label categoryTitle = new Label(titleText);
        categoryTitle.setStyle(
                "-fx-text-fill: #D4AF37; -fx-font-size: 20px; -fx-font-weight: bold; -fx-font-family: 'Georgia';");

        FlowPane grid = new FlowPane(20, 20);
        grid.setAlignment(Pos.TOP_LEFT);

        for (Room r : rooms) {
            grid.getChildren().add(createRoomCard(r));
        }

        section.getChildren().addAll(categoryTitle, grid);
        return section;
    }

    private VBox createRoomCard(Room r) {
        VBox card = new VBox(8);
        card.getStyleClass().add("glass-panel");
        card.setPrefWidth(220);
        card.setStyle("-fx-cursor: hand;");

        Label header = new Label("Room " + r.roomNumber);
        header.setStyle(
                "-fx-text-fill: #D4AF37; -fx-font-size: 22px; -fx-font-weight: bold; -fx-font-family: 'Georgia';");

        Label type = new Label(r.roomType.toUpperCase());
        type.setStyle("-fx-text-fill: #FFFFFF; -fx-font-size: 12px; -fx-font-weight: bold;");

        String statusText;
        String statusColor;
        if (!r.isAvailable) {
            statusText = "● OCCUPIED";
            statusColor = "#F44336";
        } else if (!r.isClean || !r.suppliesStocked) {
            statusText = "● NEEDS ATTENTION";
            statusColor = "#FFA726";
        } else {
            statusText = "● AVAILABLE";
            statusColor = "#4CAF50";
        }
        Label status = new Label(statusText);
        status.setStyle("-fx-text-fill: " + statusColor + "; -fx-font-weight: bold; -fx-font-size: 13px;");

        Label clean = new Label(r.isClean ? "✓ Clean" : "✗ Requires Cleaning");
        clean.setStyle("-fx-text-fill: " + (r.isClean ? "#A0A0A0" : "#FF7043") + "; -fx-font-size: 13px;");

        Label stock = new Label(r.suppliesStocked ? "✓ Stocked" : "✗ Needs Restock");
        stock.setStyle("-fx-text-fill: " + (r.suppliesStocked ? "#A0A0A0" : "#FF7043") + "; -fx-font-size: 13px;");

        Region line = new Region();
        line.setStyle("-fx-background-color: rgba(255, 255, 255, 0.2);");
        line.setMinHeight(1);
        VBox.setMargin(line, new Insets(5, 0, 5, 0));

        Label amenities = new Label("Amenities:\n" + r.amenities.getStatus());
        amenities.setStyle("-fx-text-fill: #819590; -fx-font-size: 12px;");
        amenities.setWrapText(true);

        Label expandHint = new Label("Tap to inspect ›");
        expandHint.setStyle("-fx-text-fill: rgba(212,175,55,0.5); -fx-font-size: 11px; -fx-font-style: italic;");
        VBox.setMargin(expandHint, new Insets(4, 0, 0, 0));

        card.getChildren().addAll(header, type, status, clean, stock, line, amenities, expandHint);

        // Hover: subtle scale-up
        card.setOnMouseEntered(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(120), card);
            st.setToX(1.03);
            st.setToY(1.03);
            st.play();
        });
        card.setOnMouseExited(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(120), card);
            st.setToX(1.0);
            st.setToY(1.0);
            st.play();
        });

        // Click: open the detail overlay for this room
        card.setOnMouseClicked(e -> showDetailOverlay(r));

        return card;
    }

    // ==========================================
    // PAGE 3: CHECK-IN FORM
    // ==========================================
    private VBox createCheckInPage() {
        VBox container = new VBox();
        container.setAlignment(Pos.CENTER);

        VBox form = new VBox(20);
        form.getStyleClass().add("glass-panel");
        form.setMaxWidth(450);
        form.setPadding(new Insets(40));
        form.setAlignment(Pos.CENTER);

        Label title = new Label("GUEST CHECK-IN");
        title.getStyleClass().add("gold-title");
        title.setStyle("-fx-font-size: 32px;");

        VBox nameBox = createInputBox("Guest Name:");
        TextField nameField = (TextField) nameBox.getChildren().get(1);

        VBox roomBox = createInputBox("Specific Room (Leave blank for Random):");
        TextField roomField = (TextField) roomBox.getChildren().get(1);

        roomField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("\\d*"))
                roomField.setText(newVal.replaceAll("[^\\d]", ""));
        });

        HBox btnRow = new HBox(15);
        btnRow.setAlignment(Pos.CENTER);
        VBox.setMargin(btnRow, new Insets(20, 0, 0, 0));

        Button cancelBtn = new Button("Cancel");
        Button confirmBtn = new Button("Confirm Check-In");

        cancelBtn.getStyleClass().add("cancel-button");
        confirmBtn.getStyleClass().add("action-button");

        cancelBtn.setOnAction(e -> goToDashboard());

        confirmBtn.setOnAction(e -> {
            String name = nameField.getText().trim();
            String roomText = roomField.getText().trim();

            if (name.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Validation Error", "Guest name cannot be empty.");
                return;
            }

            if (!roomText.isEmpty()) {
                Room target = system.getRoom(Integer.parseInt(roomText));
                if (target != null && (!target.isClean || !target.suppliesStocked)) {
                    showAlert(Alert.AlertType.ERROR, "Room Not Ready",
                            "Room " + roomText + " requires cleaning and restocking and cannot be occupied.");
                    return; // This stops the check-in right here
                }
            } else {
                {
                    // Blocks it if they want a random room, but NOTHING is ready
                    boolean hasReadyRoom = system.getAllRooms().stream()
                            .anyMatch(room -> room.isAvailable && room.isClean && room.suppliesStocked);

                    if (!hasReadyRoom) {
                        showAlert(Alert.AlertType.ERROR, "No Rooms Ready",
                                "There are no clean and fully stocked rooms available right now.");
                        return;
                    }
                }
            }

            Room r = roomText.isEmpty()
                    ? system.processCheckInRandomRoom(name)
                    : system.processCheckInSpecificRoom(name, Integer.parseInt(roomText));

            if (r != null) {
                showAlert(Alert.AlertType.INFORMATION, "Success",
                        "Welcome, " + name + "!\nRoom " + r.roomNumber + " assigned.\nAmenities:\n"
                                + r.amenities.getStatus());
                goToDashboard();
            } else {
                if (roomText.isEmpty()) {
                    // Random assignment failed — figure out why
                    boolean anyAvailable = system.getAllRooms().stream().anyMatch(room -> room.isAvailable);
                    if (!anyAvailable) {
                        showAlert(Alert.AlertType.ERROR, "Check-In Failed", "No available rooms at this time.");
                    } else {
                        showAlert(Alert.AlertType.WARNING, "Check-In Failed",
                                "All available rooms still require cleaning or restocking.\nPlease use Update Status to prepare a room first.");
                    }
                } else {
                    // Specific room failed — tell them exactly why
                    Room target = system.getRoom(Integer.parseInt(roomText));
                    if (target == null) {
                        showAlert(Alert.AlertType.ERROR, "Check-In Failed", "Room " + roomText + " does not exist.");
                    } else if (!target.isAvailable) {
                        showAlert(Alert.AlertType.ERROR, "Check-In Failed",
                                "Room " + roomText + " is currently occupied.");
                    } else if (!target.isClean && !target.suppliesStocked) {
                        showAlert(Alert.AlertType.WARNING, "Room Not Ready",
                                "Room " + roomText
                                        + " requires cleaning and restocking.\nPlease update its status first.");
                    } else if (!target.isClean) {
                        showAlert(Alert.AlertType.WARNING, "Room Not Ready",
                                "Room " + roomText + " has not been cleaned yet.\nPlease update its status first.");
                    } else {
                        showAlert(Alert.AlertType.WARNING, "Room Not Ready",
                                "Room " + roomText + " needs restocking.\nPlease update its status first.");
                    }
                }
            }
        });

        btnRow.getChildren().addAll(cancelBtn, confirmBtn);
        form.getChildren().addAll(title, nameBox, roomBox, btnRow);
        container.getChildren().add(form);
        return container;
    }

    // ==========================================
    // PAGE 4: UPDATE ROOM FORM
    // ==========================================
    private VBox createUpdatePage() {
        VBox container = new VBox();
        container.setAlignment(Pos.CENTER);

        VBox form = new VBox(20);
        form.getStyleClass().add("glass-panel");
        form.setMaxWidth(400);
        form.setPadding(new Insets(40));
        form.setAlignment(Pos.CENTER_LEFT);

        Label title = new Label("UPDATE ROOM");
        title.getStyleClass().add("gold-title");
        title.setStyle("-fx-font-size: 32px;");
        title.setMaxWidth(Double.MAX_VALUE);
        title.setAlignment(Pos.CENTER);

        VBox roomBox = createInputBox("Room Number:");
        TextField roomNum = (TextField) roomBox.getChildren().get(1);

        roomNum.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("\\d*"))
                roomNum.setText(newVal.replaceAll("[^\\d]", ""));
        });

        VBox checks = new VBox(12);
        CheckBox clean = new CheckBox("Room is Clean");
        CheckBox stock = new CheckBox("Supplies are Stocked");

        roomNum.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (!isNowFocused && !roomNum.getText().trim().isEmpty()) {
                try {
                    Room existing = system.getRoom(Integer.parseInt(roomNum.getText().trim()));
                    if (existing != null) {
                        clean.setSelected(existing.isClean);
                        stock.setSelected(existing.suppliesStocked);
                    }
                } catch (NumberFormatException ignored) {
                }
            }
        });

        HBox btnRow = new HBox(15);
        btnRow.setAlignment(Pos.CENTER);
        VBox.setMargin(btnRow, new Insets(20, 0, 0, 0));

        Button cancelBtn = new Button("Cancel");
        Button saveBtn = new Button("Save Changes");

        cancelBtn.getStyleClass().add("cancel-button");
        saveBtn.getStyleClass().add("action-button");

        cancelBtn.setOnAction(e -> goToDashboard());

        saveBtn.setOnAction(e -> {
            String roomText = roomNum.getText().trim();

            if (roomText.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Validation Error", "Please enter a room number.");
                return;
            }

            int rNum = Integer.parseInt(roomText);
            Room existing = system.getRoom(rNum);

            if (existing == null) {
                showAlert(Alert.AlertType.ERROR, "Not Found", "Room " + rNum + " does not exist.");
                return;
            }

            system.updateRoomStatus(rNum, clean.isSelected(), stock.isSelected());
            showAlert(Alert.AlertType.INFORMATION, "Updated", "Room " + rNum + " has been updated.");
            goToDashboard();
        });

        btnRow.getChildren().addAll(cancelBtn, saveBtn);
        form.getChildren().addAll(title, roomBox, checks, btnRow);
        container.getChildren().add(form);
        return container;
    }

    // ==========================================
    // PAGE 5: CHECK-OUT FORM
    // ==========================================
    private VBox createCheckOutPage() {
        VBox container = new VBox();
        container.setAlignment(Pos.CENTER);

        VBox form = new VBox(20);
        form.getStyleClass().add("glass-panel");
        form.setMaxWidth(400);
        form.setPadding(new Insets(40));
        form.setAlignment(Pos.CENTER);

        Label title = new Label("CHECK-OUT");
        title.getStyleClass().add("gold-title");
        title.setStyle("-fx-font-size: 32px;");

        VBox roomBox = createInputBox("Room Number to Vacate:");
        TextField roomField = (TextField) roomBox.getChildren().get(1);

        roomField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("\\d*"))
                roomField.setText(newVal.replaceAll("[^\\d]", ""));
        });

        HBox btnRow = new HBox(15);
        btnRow.setAlignment(Pos.CENTER);
        VBox.setMargin(btnRow, new Insets(20, 0, 0, 0));

        Button cancelBtn = new Button("Cancel");
        Button confirmBtn = new Button("Confirm Check-Out");

        cancelBtn.getStyleClass().add("cancel-button");
        confirmBtn.getStyleClass().add("action-button");

        cancelBtn.setOnAction(e -> goToDashboard());

        confirmBtn.setOnAction(e -> {
            String roomText = roomField.getText().trim();

            if (roomText.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Validation Error", "Please enter a room number.");
                return;
            }

            int roomNum = Integer.parseInt(roomText);
            Room room = system.getRoom(roomNum);

            if (room == null) {
                showAlert(Alert.AlertType.ERROR, "Not Found", "Room " + roomNum + " does not exist.");
            } else if (room.isAvailable) {
                showAlert(Alert.AlertType.INFORMATION, "Already Empty", "Room " + roomNum + " is already vacant.");
            } else {
                Optional<ButtonType> result = showConfirmation(
                        "Confirm Check-Out",
                        "Check out guest from Room " + roomNum
                                + "?\nThe room will be flagged for cleaning and restocking.");
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    system.processCheckout(roomNum);
                    showAlert(Alert.AlertType.INFORMATION, "Success",
                            "Room " + roomNum + " checked out.\nFlagged for cleaning and restocking.");
                    goToDashboard();
                }
            }
        });

        btnRow.getChildren().addAll(cancelBtn, confirmBtn);
        form.getChildren().addAll(title, roomBox, btnRow);
        container.getChildren().add(form);
        return container;
    }

    // --- UTILITIES ---
    private VBox createInputBox(String labelText) {
        VBox box = new VBox(8);
        box.setAlignment(Pos.CENTER_LEFT);
        Label label = new Label(labelText);
        label.getStyleClass().add("form-label");
        TextField field = new TextField();
        field.getStyleClass().add("text-field");
        field.setMaxWidth(Double.MAX_VALUE);
        box.getChildren().addAll(label, field);
        return box;
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private Optional<ButtonType> showConfirmation(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        return alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
