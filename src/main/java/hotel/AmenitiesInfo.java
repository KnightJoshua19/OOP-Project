package hotel;

public class AmenitiesInfo {
    public boolean poolAccess = true;
    public boolean gymAccess = true;
    public boolean restaurantAccess = true;

    public String getStatus() {
        String pool = poolAccess ? "Yes" : "No";
        String gym = gymAccess ? "Yes" : "No";
        String rest = restaurantAccess ? "Yes" : "No";
        return "Pool: " + pool + " | Gym: " + gym + " | Rest: " + rest;
    }

    public void updateAmenities(boolean pool, boolean gym, boolean rest) {
        this.poolAccess = pool;
        this.gymAccess = gym;
        this.restaurantAccess = rest;
    }
}
