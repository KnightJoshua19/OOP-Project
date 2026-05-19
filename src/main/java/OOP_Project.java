
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

import hotel.CheckInSystem;
import java.util.Scanner;
import java.util.ArrayList;

public class OOP_Project {

    public String showAllAvailableRooms(String theHotelRoomDatabase) {

    }

    public static void main(String[] args) {
        // CheckInSystem hotelService = new CheckInSystem();
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        //Database for Hotel Rooms
        ArrayList<String> Hotel_Room_Database = new ArrayList<String>();


        System.out.println("Welcome to the Hotel Management Terminal");

        while (running) {
            System.out.println("\n=========================");
            System.out.println("View All Rooms [1]");
            System.out.println("Check-in (Specific Room [2]");
            System.out.println("Check-in (Any Available Room) [3]");
            System.out.println("Check-out [4]");
            System.out.println("Update Room Status (Cleaning/Supplies) [5]");
            System.out.println("Exit [6]");
            System.out.print("Select an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:// hotelService.displayRooms();
                    System.out.println("Viewing all hotel rooms...");
                    break;
                case 2:
                    System.out.print("Enter guest name: ");
                    String Guest_Name = scanner.nextLine();
                    Hotel_Room_Database.add("name" + Guest_Name);

                    System.out.print("Enter room number (e.g., 101): ");
                    int Room_Number = scanner.nextInt();
                    Hotel_Room_Database.add("roomNumber" + Room_Number);

                    // hotelService.processCheckInSpecificRoom(Guest_Name, Room_Number);
                    break;
                case 3:
                    System.out.print("Enter guest name: ");
                    String Random_Name = scanner.nextLine();
                    // hotelService.processCheckInRandomRoom(Random_Name);
                    break;
                case 4:
                    System.out.print("Enter room number to check out: ");
                    int Checkout_Number = scanner.nextInt();
                    // hotelService.processCheckout(Checkout_Number);
                    break;
                case 5:
                    System.out.print("Enter room number to update: ");
                    int Update_Number = scanner.nextInt();
                    scanner.nextLine();

                    System.out.print("Is the room clean? (y/n): ");
                    boolean isClean = scanner.nextLine().equalsIgnoreCase("y");

                    System.out.print("Are supplies fully stocked? (y/n): ");
                    boolean isStocked = scanner.nextLine().equalsIgnoreCase("y");

                    // hotelService.updateRoomStatus(updateNum, isClean, isStocked);
                    break;
                case 6:
                    running = false;
                    System.out.println("Shutting down terminal...");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }

        scanner.close();
    }
}
