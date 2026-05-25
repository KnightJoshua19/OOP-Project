package hotel;

public class StandardRoom extends Room {
    public StandardRoom(int roomNumber) {
        super(roomNumber, "standard", 1500.0); // ₱1500 per night
    }

    @Override
    protected void setupAmenities() {
        getAmenities().updateAmenities(false, false, false);
    }
}
