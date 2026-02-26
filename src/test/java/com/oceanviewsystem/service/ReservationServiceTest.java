package com.oceanviewsystem.service;

import com.oceanviewsystem.dao.ReservationDAO;
import com.oceanviewsystem.model.Reservation;
import org.junit.jupiter.api.Test;
import java.sql.Date;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class ReservationServiceTest {

    @Test
    public void testCalculateAndPrintBillSuccess() {
        ReservationService service = new ReservationService();
        ReservationDAO dao = new ReservationDAO();
        Reservation tempRes = new Reservation("Bill Test Guest", "Colombo", "0777777777", 1, Date.valueOf("2026-03-01"), Date.valueOf("2026-03-05"));
        boolean isAdded = dao.addReservation(tempRes);
        assertTrue(isAdded, "Temporary reservation setup failed");
        List<Reservation> list = dao.getAllReservations();
        assertNotNull(list);
        assertTrue(list.size() > 0);
        int testReservationNo = list.get(0).getReservationNo();
        Reservation billedReservation = service.calculateAndPrintBill(testReservationNo);
        assertNotNull(billedReservation, "Failed to retrieve the reservation details.");
        assertTrue(billedReservation.getTotalBill() > 0, "Bill calculation failed. Total is 0.");
        System.out.println("Test Passed: Bill successfully calculated and printed for ID: " + testReservationNo);
        dao.deleteReservation(testReservationNo);
    }

    @Test
    public void testCalculateAndPrintBillFailure() {
        ReservationService service = new ReservationService();
        Reservation billedReservation = service.calculateAndPrintBill(-999);
        assertNull(billedReservation, "Bill calculation should return null for an invalid reservation ID");
    }
}