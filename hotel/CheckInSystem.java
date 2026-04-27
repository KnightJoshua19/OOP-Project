package hotel;

import java.util.ArrayList;

public class CheckInSystem {
    private ArrayList<Room> rooms = new ArrayList<>();
    private CleaningTeam cleaningTeam = new CleaningTeam();
    private SupplyTracker supplyTracker = new SupplyTracker();

    public CheckInSystem() {
        rooms.add(new Room(101));
        rooms.add(new Room(102));
        rooms.add(new Room(103));
    }

    public void displayRooms() {
        System.out.println("\n--- Current Room Status ---");
        for (Room r : rooms) {
            String status = r.isAvailable ? "Available" : "Occupied";
            System.out.println("Room " + r.roomNumber + " | Status: " + status);
        }
    }

    public Room findAvailableRoom() {
        for (Room r : rooms) {
            if (r.isAvailable)
                return r;
        }
        return null;
    }

    public void processCheckInRandomRoom(String guestName) {
        Room room = findAvailableRoom();
        if (room == null) {
            System.out.println("Error: No rooms available in the hotel!");
            return;
        }

        if (checkAndPrepareRoom(room)) {
            System.out.println("Success: Assigned " + guestName + " to Room " + room.roomNumber);
        }
    }

    public void processCheckInSpecificRoom(String guestName, int roomNumber) {
        Room targetRoom = null;
        for (Room room : rooms) {
            if (room.roomNumber == roomNumber) {
                targetRoom = room;
                break;
            }
        }

        if (checkAndPrepareRoom(targetRoom)) {
            System.out.println("Success: Assigned " + guestName + " to Room " + targetRoom.roomNumber);
        }
    }

    // returns true if the room is successfully prepped and locked, false if it
    // fails
    private boolean checkAndPrepareRoom(Room targetRoom) {
        if (targetRoom == null) {
            System.out.println("Error: The requested room does not exist.");
            return false;
        }

        if (!targetRoom.isAvailable) {
            System.out.println("Error: Room " + targetRoom.roomNumber + " is currently occupied!");
            return false;
        }

        System.out.println("Preparing Room " + targetRoom.roomNumber + "...");

        if (!targetRoom.isClean) {
            cleaningTeam.clean(targetRoom);
        } else {
            System.out.println(" > Room is already clean.");
        }

        if (!targetRoom.suppliesStocked) {
            supplyTracker.restock(targetRoom);
        } else {
            System.out.println(" > Room is already stocked.");
        }

        // FIX: lock the room so no one else can take it
        targetRoom.isAvailable = false;
        return true;
    }
}
