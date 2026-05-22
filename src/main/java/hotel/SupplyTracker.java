package hotel;

class SupplyTracker {
    public void restock(Room room) {
        System.out.println("[Supply Tracker] Restocking supplies...");
        room.setSuppliesStocked(true);
    }
}
