package hotel;

public interface Bookable {
    boolean checkAvailability();

    void checkIn(String guestName);

    void checkOut();
}
