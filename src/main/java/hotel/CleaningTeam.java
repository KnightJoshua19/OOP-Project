package hotel;

class CleaningTeam {
    public void clean(Room room) {
        System.out.println("[Cleaning Team] Cleaning the room...");
        room.setClean(true);
    }
}
