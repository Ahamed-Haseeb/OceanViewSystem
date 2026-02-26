package com.oceanviewsystem.dao;

import com.oceanviewsystem.model.Reservation;
import org.junit.jupiter.api.Test;
import java.sql.Date;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class ReservationDAOTest {

    private final ReservationDAO reservationDAO = new ReservationDAO();

    @Test
    public void testReservationLifecycle() {
        // 1. Test ADD Reservation
        Reservation newRes = new Reservation("Test Guest", "Test Address", "0711111111", 1, Date.valueOf("2026-05-01"), Date.valueOf("2026-05-05"));
        boolean isAdded = reservationDAO.addReservation(newRes);
        assertTrue(isAdded, "Reservation should be added successfully");

        // Fetch all reservations to get the ID of the newly added one
        List<Reservation> list = reservationDAO.getAllReservations();
        assertNotNull(list);
        assertTrue(list.size() > 0);

        // Get the most recent reservation ID
        int latestResId = list.get(0).getReservationNo();

        // 2. Test UPDATE Reservation
        Reservation resToUpdate = reservationDAO.getReservationById(latestResId);
        assertNotNull(resToUpdate);
        resToUpdate.setGuestName("Updated Test Guest");
        boolean isUpdated = reservationDAO.updateReservation(resToUpdate);
        assertTrue(isUpdated, "Reservation should be updated successfully");

        // Verify update
        Reservation verifyRes = reservationDAO.getReservationById(latestResId);
        assertEquals("Updated Test Guest", verifyRes.getGuestName(), "Guest name should match updated name");

        // 3. Test DELETE Reservation (Cleanup)
        boolean isDeleted = reservationDAO.deleteReservation(latestResId);
        assertTrue(isDeleted, "Reservation should be deleted successfully");
    }
}