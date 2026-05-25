package hotel;

public abstract class Room implements Bookable {
    private int roomNumber;
    private String roomType;
    private double price; // <-- New variable
    private boolean isAvailable;
    private boolean isClean;
    private boolean suppliesStocked;
    private AmenitiesInfo amenities;
    private String currentGuestName;

    // Added price to the constructor
    public Room(int roomNumber, String roomType, double price) {
        this.roomNumber = roomNumber;
        this.roomType = roomType.toLowerCase();
        this.price = price; // <-- Save the price
        this.isAvailable = true;
        this.isClean = false;
        this.suppliesStocked = false;
        this.amenities = new AmenitiesInfo();
        this.currentGuestName = null;

        setupAmenities();
    }

    protected abstract void setupAmenities();

    public int getRoomNumber() {
        return roomNumber;
    }

    public String getRoomType() {
        return roomType;
    }

    public double getPrice() {
        return price;
    } // <-- New getter

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
