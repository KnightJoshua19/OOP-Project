package hotel;

public class Room {
    public int roomNumber;
    public String roomType;
    public boolean isAvailable;
    public boolean isClean;
    public boolean suppliesStocked;

    public AmenitiesInfo amenities; // Each room holds its own amenities data

    public Room(int roomNumber, String roomType) {
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.isAvailable = true;
        this.isClean = false;
        this.suppliesStocked = false;

        this.amenities = new AmenitiesInfo(); // Initialize it when the room is created
    }
}
