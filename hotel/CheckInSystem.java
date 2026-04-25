package hotel;

public class CheckInSystem {
    private Room room = new Room();
    private CleaningTeam cleaners = new CleaningTeam();
    private SupplyTracker supplyTracker = new SupplyTracker();
    private AmenitiesInfo amenities = new AmenitiesInfo();

    public void processCheckIn() {
        System.out.println("[System] Checking room status...");

        if (!room.isClean) {
            cleaners.clean(room);
        }

        if (!room.suppliesStocked) {
            supplyTracker.restock(room);
        }

        System.out.println("[System] Room ready. Amenities: " + amenities.getStatus());
        System.out.println("[System] Room assigned to Guest.");
    }
}
