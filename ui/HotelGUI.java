package ui;

import hotel.CheckInSystem;
import hotel.Room;
import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class HotelGUI {
    private CheckInSystem system;
    private JTextArea displayArea;

    public HotelGUI() {
        system = new CheckInSystem();

        UIManager.put("Button.arc", 15);
        UIManager.put("Component.arc", 15);
        UIManager.put("TextComponent.arc", 15);
        UIManager.put("ScrollBar.thumbArc", 15);
        UIManager.put("Button.margin", new Insets(8, 14, 8, 14));

        Color darkOlive = new Color(66, 71, 50);
        Color mutedTeal = new Color(129, 149, 144);
        Color warmBeige = new Color(187, 170, 150);
        Color offWhite = new Color(242, 239, 235);

        UIManager.put("Panel.background", offWhite);
        UIManager.put("OptionPane.background", offWhite);
        UIManager.put("Button.background", darkOlive);
        UIManager.put("Button.foreground", offWhite);
        UIManager.put("Button.hoverBackground", mutedTeal);
        UIManager.put("TextArea.background", warmBeige);
        UIManager.put("TextArea.foreground", darkOlive);
        UIManager.put("Label.foreground", darkOlive);
        UIManager.put("ScrollPane.background", offWhite);

        try {
            FlatLightLaf.setup();
        } catch (Exception e) {
            System.err.println("Failed to initialize FlatLaf");
        }

        setupWindow();
    }

    private void setupWindow() {
        JFrame frame = new JFrame("Hotel Reception Dashboard");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 500);
        frame.setLayout(new BorderLayout(15, 15));
        frame.getContentPane().setBackground(UIManager.getColor("Panel.background"));

        ((JPanel) frame.getContentPane()).setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Room Management System");
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        frame.add(title, BorderLayout.NORTH);

        displayArea = new JTextArea();
        displayArea.setEditable(false);
        displayArea.setFont(new Font("Monospaced", Font.BOLD, 14));
        displayArea.setMargin(new Insets(15, 15, 15, 15));

        JScrollPane scrollPane = new JScrollPane(displayArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        frame.add(scrollPane, BorderLayout.CENTER);

        // Back to 6 buttons since we combined the update feature
        JPanel sidebar = new JPanel(new GridLayout(6, 1, 10, 10));
        sidebar.setPreferredSize(new Dimension(220, 0));
        sidebar.setOpaque(false);

        JButton viewBtn = new JButton("Refresh Rooms");
        JButton checkInRandomBtn = new JButton("Quick Check-In");
        JButton checkInSpecificBtn = new JButton("Specific Check-In");
        JButton checkOutBtn = new JButton("Check-Out");
        JButton updateBtn = new JButton("Update Room Status");

        sidebar.add(viewBtn);
        sidebar.add(checkInRandomBtn);
        sidebar.add(checkInSpecificBtn);
        sidebar.add(checkOutBtn);
        sidebar.add(updateBtn);

        frame.add(sidebar, BorderLayout.EAST);

        // --- ACTIONS ---

        viewBtn.addActionListener(e -> updateScreen());

        checkInRandomBtn.addActionListener(e -> {
            String name = JOptionPane.showInputDialog(frame, "Enter Guest Name:");
            if (name != null && !name.trim().isEmpty()) {
                Room assigned = system.processCheckInRandomRoom(name);
                if (assigned != null) {
                    JOptionPane.showMessageDialog(frame,
                            "Check-in Successful!\nRoom Amenities:\n" + assigned.amenities.getStatus(), "Info",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(frame, "No available rooms.", "Error", JOptionPane.ERROR_MESSAGE);
                }
                updateScreen();
            }
        });

        checkInSpecificBtn.addActionListener(e -> {
            JTextField nameField = new JTextField();
            JTextField roomField = new JTextField();
            Object[] message = { "Guest Name:", nameField, "Room Number:", roomField };

            int option = JOptionPane.showConfirmDialog(frame, message, "Specific Check-In",
                    JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                try {
                    int roomNum = Integer.parseInt(roomField.getText().trim());
                    Room assigned = system.processCheckInSpecificRoom(nameField.getText(), roomNum);
                    if (assigned != null) {
                        JOptionPane
                                .showMessageDialog(frame,
                                        "Check-in Successful for Room " + roomNum + "!\nRoom Amenities:\n"
                                                + assigned.amenities.getStatus(),
                                        "Info", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(frame, "Room is unavailable or doesn't exist.", "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                    updateScreen();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Please enter a valid room number.");
                }
            }
        });

        checkOutBtn.addActionListener(e -> {
            String room = JOptionPane.showInputDialog(frame, "Enter Room Number to Check Out:");
            if (room != null && !room.trim().isEmpty()) {
                try {
                    system.processCheckout(Integer.parseInt(room.trim()));
                    updateScreen();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Please enter a valid number.");
                }
            }
        });

        // The New Two-Step Update Logic
        updateBtn.addActionListener(e -> {
            String roomStr = JOptionPane.showInputDialog(frame, "Enter Room Number to Update:");
            if (roomStr != null && !roomStr.trim().isEmpty()) {
                try {
                    int roomNum = Integer.parseInt(roomStr.trim());
                    Room targetRoom = system.getRoom(roomNum);

                    if (targetRoom == null) {
                        JOptionPane.showMessageDialog(frame, "Room not found.", "Error", JOptionPane.ERROR_MESSAGE);
                        return; // Stop here if the room doesn't exist
                    }

                    // Pre-check the boxes based on the room's current state!
                    JCheckBox cleanBox = new JCheckBox("Room is Clean", targetRoom.isClean);
                    JCheckBox stockedBox = new JCheckBox("Supplies are Stocked", targetRoom.suppliesStocked);
                    JCheckBox poolBox = new JCheckBox("Pool Access", targetRoom.amenities.poolAccess);
                    JCheckBox gymBox = new JCheckBox("Gym Access", targetRoom.amenities.gymAccess);
                    JCheckBox restBox = new JCheckBox("Restaurant Access", targetRoom.amenities.restaurantAccess);

                    Object[] message = {
                            "--- Room Status ---", cleanBox, stockedBox,
                            "--- Amenities Access ---", poolBox, gymBox, restBox
                    };

                    int option = JOptionPane.showConfirmDialog(frame, message, "Updating Room " + roomNum,
                            JOptionPane.OK_CANCEL_OPTION);
                    if (option == JOptionPane.OK_OPTION) {
                        system.updateRoomStatus(roomNum, cleanBox.isSelected(), stockedBox.isSelected(),
                                poolBox.isSelected(), gymBox.isSelected(), restBox.isSelected());
                        updateScreen();
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Please enter a valid room number.");
                }
            }
        });

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        updateScreen();
    }

    private void updateScreen() {
        displayArea.setText(system.getFullDashboardText());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new HotelGUI());
    }
}
