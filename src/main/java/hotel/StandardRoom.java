package hotel;

public class StandardRoom extends Room {
    public StandardRoom(int roomNumber) {
        super(roomNumber, "standard");
    }

    @Override
    protected void setupAmenities() {
        getAmenities().poolAccess = false;
        getAmenities().gymAccess = false;
        getAmenities().restaurantAccess = false;
    }
}
