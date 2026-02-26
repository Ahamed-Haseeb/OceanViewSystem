package com.oceanviewsystem.dao;

import com.oceanviewsystem.model.Reservation;
import org.junit.jupiter.api.Test;
import java.sql.Date;
import static org.junit.jupiter.api.Assertions.*;

public class ReservationDAOTest {

    @Test
    public void testAddReservationSuccess() {
        // 1. Create the DAO object to test
        ReservationDAO reservationDAO = new ReservationDAO();

        // 2. Setup Check-in and Check-out dates
        // Format should be "YYYY-MM-DD"
        Date checkIn = Date.valueOf("2026-03-01");
        Date checkOut = Date.valueOf("2026-03-05");

        // 3. Create a new Reservation object with dummy guest details
        // Note: roomId 1 is the 'Single' room we added in our SQL script earlier
        Reservation newReservation = new Reservation("Kamal Perera", "Galle", "0771234567", 1, checkIn, checkOut);

        // 4. Try to add the reservation to the database
        boolean isInserted = reservationDAO.addReservation(newReservation);

        // 5. Check if it returns 'true' (which means successful insert)
        assertTrue(isInserted, "Test Failed: Could not add the reservation to the database.");

        System.out.println("Test Passed: New reservation added successfully to the database!");
    }
}