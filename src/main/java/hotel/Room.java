package hotel;

public class Room {
    public int roomNumber;
    public String roomType;
    public boolean isAvailable;
    public boolean isClean;
    public boolean suppliesStocked;
    public AmenitiesInfo amenities;
    public String currentGuestName; // null when room is vacant

    public Room(int roomNumber, String roomType) {
        this.roomNumber = roomNumber;
        this.roomType = roomType.toLowerCase();
        this.isAvailable = true;
        this.isClean = false;
        this.suppliesStocked = false;
        this.currentGuestName = null;

        // Auto-assign amenities based on the type of room
        this.amenities = new AmenitiesInfo();

        if (this.roomType.equals("suite")) {
            this.amenities.poolAccess = true;
            this.amenities.gymAccess = true;
            this.amenities.restaurantAccess = true;
        } else if (this.roomType.equals("deluxe")) {
            this.amenities.poolAccess = true;
            this.amenities.gymAccess = true;
            this.amenities.restaurantAccess = false;
        } else {
            // standard room
            this.amenities.poolAccess = false;
            this.amenities.gymAccess = false;
            this.amenities.restaurantAccess = false;
        }
    }
}
