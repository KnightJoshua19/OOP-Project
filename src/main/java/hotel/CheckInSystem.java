package hotel;

import java.util.ArrayList;

public class CheckInSystem {
    private ArrayList<Room> rooms = new ArrayList<>();
    private CleaningTeam cleaningTeam = new CleaningTeam();
    private SupplyTracker supplyTracker = new SupplyTracker();

    public CheckInSystem() {
        // Generate 10 Standard Rooms (101-110)
        for (int i = 101; i <= 110; i++) {
            rooms.add(new Room(i, "standard"));
        }

        // Generate 6 Deluxe Rooms (201-206)
        for (int i = 201; i <= 206; i++) {
            rooms.add(new Room(i, "deluxe"));
        }

        // Generate 4 Suite Rooms (301-304)
        for (int i = 301; i <= 304; i++) {
            rooms.add(new Room(i, "suite"));
        }
    }

    public String getFullDashboardText() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== ROOM STATUS & AMENITIES ===\n\n");
        for (Room r : rooms) {
            String status = r.isAvailable ? "Available" : "Occupied";
            String cleanStatus = r.isClean ? "Clean" : "Dirty";
            String stockStatus = r.suppliesStocked ? "Stocked" : "Needs Restock";
            sb.append("Room ").append(r.roomNumber)
                    .append(" [").append(r.roomType).append("] | ")
                    .append(status).append(" | ").append(cleanStatus).append(" | ").append(stockStatus).append("\n")
                    .append("  └ Amenities Access -> ").append(r.amenities.getStatus()).append("\n\n");
        }
        return sb.toString();
    }

    public Room getRoom(int roomNumber) {
        for (Room room : rooms) {
            if (room.roomNumber == roomNumber)
                return room;
        }
        return null;
    }

    public void updateRoomStatus(int roomNum, boolean isClean, boolean isStocked) {
        Room room = getRoom(roomNum);
        if (room != null) {
            room.isClean = isClean;
            room.suppliesStocked = isStocked;
        }
    }

    public Room findAvailableRoom() {
        for (Room r : rooms) {
            if (r.isAvailable)
                return r;
        }
        return null;
    }

    public Room processCheckInRandomRoom(String guestName) {
        Room room = findAvailableRoom();
        if (room != null && checkAndPrepareRoom(room, guestName)) {
            return room;
        }
        return null;
    }

    public Room processCheckInSpecificRoom(String guestName, int roomNumber) {
        Room targetRoom = getRoom(roomNumber);
        if (targetRoom != null && checkAndPrepareRoom(targetRoom, guestName)) {
            return targetRoom;
        }
        return null;
    }

    public void processCheckout(int roomNumber) {
        Room room = getRoom(roomNumber);
        if (room != null && !room.isAvailable) {
            room.isAvailable = true;
            room.isClean = false;
            room.suppliesStocked = false;
            room.currentGuestName = null; // Clear guest on checkout
        }
    }

    private boolean checkAndPrepareRoom(Room targetRoom, String guestName) {
        if (targetRoom == null || !targetRoom.isAvailable)
            return false;
        if (!targetRoom.isClean)
            cleaningTeam.clean(targetRoom);
        if (!targetRoom.suppliesStocked)
            supplyTracker.restock(targetRoom);
        targetRoom.isAvailable = false;
        targetRoom.currentGuestName = guestName; // Store guest on check-in
        return true;
    }

    public ArrayList<Room> getAllRooms() {
        return rooms;
    }
}
