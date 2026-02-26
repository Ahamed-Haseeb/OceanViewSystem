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


    @Test
    public void testGetReservationByIdSuccess() {
        Reservation tempRes = new Reservation("Get Test", "Test Address", "0711111111", 1, Date.valueOf("2026-06-01"), Date.valueOf("2026-06-05"));
        reservationDAO.addReservation(tempRes);

        List<Reservation> list = reservationDAO.getAllReservations();
        int latestResId = list.get(0).getReservationNo();

        Reservation res = reservationDAO.getReservationById(latestResId);
        assertNotNull(res, "Reservation should not be null for valid ID");

        reservationDAO.deleteReservation(latestResId);
    }

    @Test
    public void testGetReservationByIdFailure() {
        Reservation res = reservationDAO.getReservationById(-999);
        assertNull(res, "Reservation should be null for invalid ID");
    }

    @Test
    public void testCalculateBillUsingProcedure() {
        Reservation tempRes = new Reservation("Bill Test", "Test Address", "0711111111", 1, Date.valueOf("2026-07-01"), Date.valueOf("2026-07-05"));
        reservationDAO.addReservation(tempRes);

        List<Reservation> list = reservationDAO.getAllReservations();
        int latestResId = list.get(0).getReservationNo();

        boolean isCalculated = reservationDAO.calculateBillUsingProcedure(latestResId);
        assertTrue(isCalculated, "Bill calculation should complete successfully using procedure");

        reservationDAO.deleteReservation(latestResId);
    }

    @Test
    public void testGetAllReservations() {
        List<Reservation> list = reservationDAO.getAllReservations();
        assertNotNull(list, "Reservation list should not be null");
    }

    @Test
    public void testUpdateReservationFailure() {
        Reservation invalidRes = new Reservation();
        invalidRes.setReservationNo(-999);
        invalidRes.setGuestName("Ghost Guest");
        boolean isUpdated = reservationDAO.updateReservation(invalidRes);
        assertFalse(isUpdated, "Update should fail for non-existent reservation ID");
    }
}