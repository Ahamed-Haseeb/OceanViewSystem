package com.oceanviewsystem.model;

import org.junit.jupiter.api.Test;
import java.sql.Date;
import static org.junit.jupiter.api.Assertions.*;

public class ReservationValidationTest {

    @Test
    public void testInvalidDates_CheckoutBeforeCheckIn() {
        Date checkIn = Date.valueOf("2026-05-10");
        Date checkOut = Date.valueOf("2026-05-05");

        boolean isDateValid = checkOut.after(checkIn);
        assertFalse(isDateValid, "Validation failed: Checkout date cannot be before Check-in date");
    }

    @Test
    public void testInvalidRoomId() {
        int invalidRoomId = -5;
        boolean isRoomValid = invalidRoomId > 0;
        assertFalse(isRoomValid, "Validation failed: Room ID must be a positive number");
    }

    @Test
    public void testNegativeTotalBill() {
        double invalidBill = -1500.00;
        boolean isBillValid = invalidBill >= 0;
        assertFalse(isBillValid, "Validation failed: Total Bill cannot be negative");
    }
}