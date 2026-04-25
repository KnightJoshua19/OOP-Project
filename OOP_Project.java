
/**
 * Codespace creator:
 * 
 * @author Joshua M. Esclamado
 * 
 *         Date created:
 *         04/04/2026
 * 
 *         Co-authors:
 *         Jefel Joe V. Villacorta
 * 
 * 
 *         Program description:
 *         This codespace is prepared to set the environment for the OOP project
 *         written in Java language.
 */

import hotel.CheckInSystem;

public class OOP_Project {
    public static void main(String[] args) {
        System.out.println("Hello World!");
        CheckInSystem hotelService = new CheckInSystem();

        System.out.println("--- Starting Hotel Operations ---");
        hotelService.processCheckIn();

    }
}
