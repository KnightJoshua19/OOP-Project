
/**
 * Codespace creator:
 * 
 * @author Joshua M. Esclamado
 * 
 *         Date created:
 *         04/04/2026
 * 
 *         Co-authors:
 *         Jefel Joe V. Villacorta
 * 
 * 
 *         Program description:
 *         This codespace is prepared to set the environment for the OOP project
 *         written in Java language.
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;


class Guest {
    private String guestName;
    private int roomNumber;
    private String paymentMethod;
    private String roomStatus; // "Occupied", "Cleaning", "Needs Supplies"

    // Constructor
    public Guest(String guestName, int roomNumber, String paymentMethod) {
        this.guestName     = guestName;
        this.roomNumber    = roomNumber;
        this.paymentMethod = paymentMethod;
        this.roomStatus    = "Occupied";
    }

    // ── Getters ──────────────────────────────
    public String getGuestName()     { return guestName; }
    public int    getRoomNumber()    { return roomNumber; }
    public String getPaymentMethod() { return paymentMethod; }
    public String getRoomStatus()    { return roomStatus; }

    // ── Setters ──────────────────────────────
    public void setRoomStatus(String status) { this.roomStatus = status; }

    // ── Display ──────────────────────────────
    @Override
    public String toString() {
        return String.format(
                "| %-20s | Room: %3d | Payment: %-10s | Status: %-20s |",
                guestName, roomNumber, paymentMethod, roomStatus
        );
    }
}


public class OOP_Project {

    // ══════════════════════════════════════════
    //  TERNARY SEARCH — searches sorted list by guest name
    //  Returns the Guest object if found, null otherwise.
    //
    //  How Ternary Search works:
    //    1. Divide the sorted array into THREE parts using two mid-points.
    //    2. Compare target with mid1 and mid2.
    //    3. Recurse into the relevant third — eliminates 2/3 per step.
    //    Time complexity: O(log₃ n)  |  Requires: sorted input
    // ══════════════════════════════════════════
    public static Guest ternarySearch(ArrayList<Guest> list,
                                      String targetName,
                                      int left, int right) {
        if (left > right) return null; // base case — not found

        int rangeSize = right - left;
        int mid1 = left  + rangeSize / 3;
        int mid2 = right - rangeSize / 3;

        String name1 = list.get(mid1).getGuestName().toLowerCase();
        String name2 = list.get(mid2).getGuestName().toLowerCase();
        String target = targetName.toLowerCase();

        // ── Direct hits ──────────────────────
        if (name1.equals(target)) return list.get(mid1);
        if (name2.equals(target)) return list.get(mid2);

        // ── Recurse into the correct third ───
        if (target.compareTo(name1) < 0) {
            // Target is in the LEFT third
            return ternarySearch(list, targetName, left, mid1 - 1);
        } else if (target.compareTo(name2) > 0) {
            // Target is in the RIGHT third
            return ternarySearch(list, targetName, mid2 + 1, right);
        } else {
            // Target is in the MIDDLE third
            return ternarySearch(list, targetName, mid1 + 1, mid2 - 1);
        }
    }


    public static Guest checkHotelDatabase(String guestName,
                                           ArrayList<Guest> database) {
        if (database.isEmpty()) return null;

        // Ternary search requires a sorted list
        ArrayList<Guest> sorted = new ArrayList<>(database);
        sorted.sort(Comparator.comparing(g -> g.getGuestName().toLowerCase()));

        return ternarySearch(sorted, guestName, 0, sorted.size() - 1);
    }


    public static boolean isRoomOccupied(int roomNumber,
                                         ArrayList<Guest> database) {
        for (Guest g : database) {
            if (g.getRoomNumber() == roomNumber) return true;
        }
        return false;
    }


    public static void displayAllRooms(ArrayList<Guest> database) {
        if (database.isEmpty()) {
            System.out.println("  No guests are currently checked in.");
            return;
        }
        System.out.println("\n" + "─".repeat(80));
        System.out.printf("  %-3s | %-20s | %-5s | %-10s | %-20s%n",
                "#", "Guest Name", "Room", "Payment", "Status");
        System.out.println("─".repeat(80));
        for (int i = 0; i < database.size(); i++) {
            Guest g = database.get(i);
            System.out.printf("  %-3d | %-20s | %-5d | %-10s | %-20s%n",
                    i + 1,
                    g.getGuestName(),
                    g.getRoomNumber(),
                    g.getPaymentMethod(),
                    g.getRoomStatus());
        }
        System.out.println("─".repeat(80));
    }


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        // ── ArrayList-based guest database ───
        ArrayList<Guest> hotelDatabase = new ArrayList<>();

        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("║  Welcome to Hotel Management System  ║");
        System.out.println("╚══════════════════════════════════════╝");

        while (running) {
            System.out.println("\n===========================================");
            System.out.println("  [1] View All Rooms");
            System.out.println("  [2] Check-In Guest");
            System.out.println("  [3] Check-Out Guest");
            System.out.println("  [4] Update Room Status (Cleaning/Supplies)");
            System.out.println("  [5] Search Guest");
            System.out.println("  [6] Exit");
            System.out.println("===========================================");
            System.out.print("  Select an option: ");

            // ── Input validation for menu choice ─
            int choice = -1;
            try {
                choice = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("  [!] Invalid input. Please enter a number.");
                continue;
            }

            switch (choice) {


                case 1:
                    System.out.println("\n  --- Current Hotel Guest Database ---");
                    displayAllRooms(hotelDatabase);
                    break;


                case 2:
                    System.out.println("\n  --- Check-In ---");

                    // Guest name
                    System.out.print("  Enter guest name: ");
                    String guestName = scanner.nextLine().trim();
                    if (guestName.isEmpty()) {
                        System.out.println("  [!] Guest name cannot be empty.");
                        break;
                    }

                    // Room number with validation
                    int roomNumber = -1;
                    while (true) {
                        System.out.print("  Enter room number (1–100): ");
                        try {
                            roomNumber = Integer.parseInt(scanner.nextLine().trim());
                        } catch (NumberFormatException e) {
                            System.out.println("  [!] Please enter a valid integer.");
                            continue;
                        }
                        if (roomNumber < 1 || roomNumber > 100) {
                            System.out.println("  [!] Room number must be between 1 and 100.");
                        } else if (isRoomOccupied(roomNumber, hotelDatabase)) {
                            System.out.println("  [!] Room " + roomNumber + " is already occupied. Choose another.");
                        } else {
                            break; // valid
                        }
                    }

                    // Payment method
                    String payMethod = "";
                    while (true) {
                        System.out.print("  Payment Method? (Yama / GoGash): ");
                        payMethod = scanner.nextLine().trim();
                        if (payMethod.equalsIgnoreCase("Yama") ||
                                payMethod.equalsIgnoreCase("GoGash")) {
                            break;
                        }
                        System.out.println("  [!] Invalid payment method. Choose 'Yama' or 'GoGash'.");
                    }

                    // Confirm check-in
                    System.out.print("  Type '1' to confirm check-in, anything else to cancel: ");
                    String confirm = scanner.nextLine().trim();
                    if (confirm.equals("1")) {
                        Guest newGuest = new Guest(guestName, roomNumber, payMethod);
                        hotelDatabase.add(newGuest);
                        System.out.println("\n  ✔ Guest \"" + guestName +
                                "\" checked into Room " + roomNumber +
                                " successfully!");
                    } else {
                        System.out.println("  [!] Check-in cancelled.");
                    }
                    break;


                case 3:
                    System.out.println("\n  --- Check-Out ---");
                    if (hotelDatabase.isEmpty()) {
                        System.out.println("  [!] No guests to check out.");
                        break;
                    }

                    System.out.print("  Enter room number to check out: ");
                    int checkoutRoom = -1;
                    try {
                        checkoutRoom = Integer.parseInt(scanner.nextLine().trim());
                    } catch (NumberFormatException e) {
                        System.out.println("  [!] Invalid room number.");
                        break;
                    }

                    Guest toRemove = null;
                    for (Guest g : hotelDatabase) {
                        if (g.getRoomNumber() == checkoutRoom) {
                            toRemove = g;
                            break;
                        }
                    }

                    if (toRemove != null) {
                        hotelDatabase.remove(toRemove);
                        System.out.println("  ✔ Guest \"" + toRemove.getGuestName() +
                                "\" in Room " + checkoutRoom +
                                " has been checked out.");
                    } else {
                        System.out.println("  [!] No guest found in Room " + checkoutRoom + ".");
                    }
                    break;

                case 4:
                    System.out.println("\n  --- Update Room Status ---");
                    System.out.print("  Enter room number to update: ");
                    int updateRoom = -1;
                    try {
                        updateRoom = Integer.parseInt(scanner.nextLine().trim());
                    } catch (NumberFormatException e) {
                        System.out.println("  [!] Invalid room number.");
                        break;
                    }

                    Guest toUpdate = null;
                    for (Guest g : hotelDatabase) {
                        if (g.getRoomNumber() == updateRoom) {
                            toUpdate = g;
                            break;
                        }
                    }

                    if (toUpdate == null) {
                        System.out.println("  [!] No guest found in Room " + updateRoom + ".");
                        break;
                    }

                    System.out.print("  Is the room clean? (y/n): ");
                    boolean isClean = scanner.nextLine().trim().equalsIgnoreCase("y");

                    System.out.print("  Are supplies fully stocked? (y/n): ");
                    boolean isStocked = scanner.nextLine().trim().equalsIgnoreCase("y");

                    if (isClean && isStocked) {
                        toUpdate.setRoomStatus("Occupied");
                    } else if (!isClean && !isStocked) {
                        toUpdate.setRoomStatus("Cleaning & Restocking");
                    } else if (!isClean) {
                        toUpdate.setRoomStatus("Needs Cleaning");
                    } else {
                        toUpdate.setRoomStatus("Needs Supplies");
                    }

                    System.out.println("  ✔ Room " + updateRoom +
                            " status updated to: " + toUpdate.getRoomStatus());
                    break;

                case 5:
                    System.out.println("\n  --- Search Guest (Ternary Search) ---");
                    if (hotelDatabase.isEmpty()) {
                        System.out.println("  [!] The database is empty.");
                        break;
                    }

                    System.out.print("  Enter guest name to search: ");
                    String searchName = scanner.nextLine().trim();

                    Guest found = checkHotelDatabase(searchName, hotelDatabase);

                    if (found != null) {
                        System.out.println("\n  ✔ Guest found!");
                        System.out.println("  " + found);
                    } else {
                        System.out.println("  [!] No guest named \"" + searchName + "\" found.");
                    }
                    break;

                case 6:
                    running = false;
                    System.out.println("\n  Shutting down terminal... Goodbye!");
                    break;

                default:
                    System.out.println("  [!] Invalid option. Please choose 1–6.");
            }
        }

        scanner.close();
    }
}