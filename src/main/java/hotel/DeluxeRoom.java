package hotel;

public class DeluxeRoom extends Room {
    public DeluxeRoom(int roomNumber) {
        super(roomNumber, "deluxe", 3000.0); // ₱3000 per night
    }

    @Override
    protected void setupAmenities() {
        getAmenities().updateAmenities(true, true, false);
    }
}
