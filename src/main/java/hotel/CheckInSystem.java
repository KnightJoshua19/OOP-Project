package hotel;

import java.util.ArrayList;

public class CheckInSystem {
    private ArrayList<Room> rooms = new ArrayList<>();
    private CleaningTeam cleaningTeam = new CleaningTeam();
    private SupplyTracker supplyTracker = new SupplyTracker();

    public CheckInSystem() {
        // Generate Standard Rooms (101-118)
        for (int i = 101; i <= 118; i++) {
            rooms.add(new StandardRoom(i));
        }

        // Generate Deluxe Rooms (201-218)
        for (int i = 201; i <= 218; i++) {
            rooms.add(new DeluxeRoom(i));
        }

        // Generate Suite Rooms (301-318)
        for (int i = 301; i <= 318; i++) {
            rooms.add(new SuiteRoom(i));
        }
    }

    public String getFullDashboardText() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== ROOM STATUS & AMENITIES ===\n\n");
        for (Room r : rooms) {
            String status = r.isAvailable() ? "Available" : "Occupied";
            String cleanStatus = r.isClean() ? "Clean" : "Dirty";
            String stockStatus = r.isSuppliesStocked() ? "Stocked" : "Needs Restock";
            sb.append("Room ").append(r.getRoomNumber())
                    .append(" [").append(r.getRoomType()).append("] | ")
                    .append(status).append(" | ").append(cleanStatus).append(" | ").append(stockStatus).append("\n")
                    .append("  └ Amenities Access -> ").append(r.getAmenities().getStatus()).append("\n\n");
        }
        return sb.toString();
    }

    public Room getRoom(int roomNumber) {
        for (Room room : rooms) {
            if (room.getRoomNumber() == roomNumber)
                return room;
        }
        return null;
    }

    public void updateRoomStatus(int roomNum, boolean isClean, boolean isStocked) {
        Room room = getRoom(roomNum);
        if (room != null) {
            room.setClean(isClean);
            room.setSuppliesStocked(isStocked);
        }
    }

    public Room findAvailableRoom() {
        for (Room r : rooms) {
            if (r.isAvailable())
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
        if (room != null && !room.isAvailable()) {
            room.checkOut(); // Uses our new interface method!
        }
    }

    private boolean checkAndPrepareRoom(Room targetRoom, String guestName) {
        if (targetRoom == null || !targetRoom.isAvailable())
            return false;

        if (!targetRoom.isClean())
            cleaningTeam.clean(targetRoom);

        if (!targetRoom.isSuppliesStocked())
            supplyTracker.restock(targetRoom);

        targetRoom.checkIn(guestName); // Uses our new interface method!
        return true;
    }

    public ArrayList<Room> getAllRooms() {
        return rooms;
    }
}
