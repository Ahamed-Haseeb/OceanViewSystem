package com.oceanviewsystem.service;

import com.oceanviewsystem.dao.ReservationDAO;
import com.oceanviewsystem.model.Reservation;
import org.junit.jupiter.api.Test;
import java.sql.Date;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class ReservationServiceTest {

    @Test
    public void testCalculateAndPrintBill() {
        ReservationService service = new ReservationService();
        ReservationDAO dao = new ReservationDAO();

        // 1. Create a temporary reservation for the test
        Reservation tempRes = new Reservation("Bill Test Guest", "Colombo", "0777777777", 1, Date.valueOf("2026-03-01"), Date.valueOf("2026-03-05"));
        boolean isAdded = dao.addReservation(tempRes);
        assertTrue(isAdded, "Temporary reservation setup failed");

        // 2. Fetch the latest reservation to get its auto-generated ID
        List<Reservation> list = dao.getAllReservations();
        assertNotNull(list);
        assertTrue(list.size() > 0);
        int testReservationNo = list.get(0).getReservationNo(); // Get the ID

        // 3. Test the Bill Calculation using the valid ID
        Reservation billedReservation = service.calculateAndPrintBill(testReservationNo);

        // 4. Verify that the bill was successfully retrieved and calculated
        assertNotNull(billedReservation, "Failed to retrieve the reservation details.");
        assertTrue(billedReservation.getTotalBill() > 0, "Bill calculation failed. Total is 0.");

        System.out.println("Test Passed: Bill successfully calculated and printed for ID: " + testReservationNo);

        // 5. Clean up (Delete the temporary reservation so DB stays clean)
        dao.deleteReservation(testReservationNo);
    }
}