package hotel;

public class SuiteRoom extends Room {
    public SuiteRoom(int roomNumber) {
        super(roomNumber, "suite", 5500.0); // ₱5500 per night
    }

    @Override
    protected void setupAmenities() {
        getAmenities().updateAmenities(true, true, true);
    }
}
