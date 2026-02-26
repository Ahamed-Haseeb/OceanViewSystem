package com.oceanviewsystem.service;

import com.oceanviewsystem.model.Reservation;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ReservationServiceTest {

    @Test
    public void testCalculateAndPrintBill() {
        ReservationService service = new ReservationService();

        // Assuming Reservation No 1 is the one we just added for "Kamal Perera"
        // It will calculate the bill for 4 days (March 1 to March 5)
        // Room 1 rate is 5000.00, so total should be 20000.00
        int testReservationNo = 1;

        Reservation billedReservation = service.calculateAndPrintBill(testReservationNo);

        // Verify that the bill was successfully retrieved and calculated
        assertNotNull(billedReservation, "Failed to retrieve the reservation details.");
        assertTrue(billedReservation.getTotalBill() > 0, "Bill calculation failed. Total is 0.");

        System.out.println("Test Passed: Bill successfully calculated and printed!");
    }
}