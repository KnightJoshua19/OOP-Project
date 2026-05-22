package hotel;

public class SuiteRoom extends Room {
    public SuiteRoom(int roomNumber) {
        super(roomNumber, "suite");
    }

    @Override
    protected void setupAmenities() {
        getAmenities().poolAccess = true;
        getAmenities().gymAccess = true;
        getAmenities().restaurantAccess = true;
    }
}
