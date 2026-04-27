package hotel;

class Room {

    int roomNumber;
    boolean isAvailable;
    boolean isClean; // toggle for testing cleaning
    boolean suppliesStocked; // toggle for testing restock logic

    private String customerName;

    public Room(int roomNumber) {
        this.roomNumber = roomNumber;
        this.isAvailable = true;
        this.isClean = true;
        this.suppliesStocked = true;

        this.customerName = "";
    }

}
