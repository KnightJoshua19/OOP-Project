package hotel;

public abstract class Room implements Bookable {
    private int roomNumber;
    private String roomType;
    private boolean isAvailable;
    private boolean isClean;
    private boolean suppliesStocked;
    private AmenitiesInfo amenities;
    private String currentGuestName;

    public Room(int roomNumber, String roomType) {
        this.roomNumber = roomNumber;
        this.roomType = roomType.toLowerCase();
        this.isAvailable = true;
        this.isClean = false;
        this.suppliesStocked = false;
        this.amenities = new AmenitiesInfo();
        this.currentGuestName = null;

        setupAmenities(); // Polymorphism hook
    }

    // Subclasses must define this themselves
    protected abstract void setupAmenities();

    // --- GETTERS & SETTERS (Encapsulation) ---
    public int getRoomNumber() {
        return roomNumber;
    }

    public String getRoomType() {
        return roomType;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public boolean isClean() {
        return isClean;
    }

    public void setClean(boolean clean) {
        isClean = clean;
    }

    public boolean isSuppliesStocked() {
        return suppliesStocked;
    }

    public void setSuppliesStocked(boolean stocked) {
        suppliesStocked = stocked;
    }

    public AmenitiesInfo getAmenities() {
        return amenities;
    }

    public String getCurrentGuestName() {
        return currentGuestName;
    }

    // --- INTERFACE IMPLEMENTATION ---
    @Override
    public boolean checkAvailability() {
        return isAvailable;
    }

    @Override
    public void checkIn(String guestName) {
        this.currentGuestName = guestName;
        this.isAvailable = false;
    }

    @Override
    public void checkOut() {
        this.currentGuestName = null;
        this.isAvailable = true;
        this.isClean = false;
        this.suppliesStocked = false;
    }
}
