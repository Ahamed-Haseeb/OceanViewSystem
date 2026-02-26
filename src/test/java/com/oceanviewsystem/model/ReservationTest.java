package com.oceanviewsystem.model;

import org.junit.jupiter.api.Test;
import java.sql.Date;
import static org.junit.jupiter.api.Assertions.*;

public class ReservationTest {

    @Test
    public void testReservationGettersAndSetters() {
        // 1. Create a new Reservation object
        Reservation reservation = new Reservation();
        reservation.setReservationNo(100);
        reservation.setGuestName("John Doe");
        reservation.setRoomId(5);
        reservation.setCheckInDate(Date.valueOf("2026-08-01"));
        reservation.setTotalBill(15000.00);

        assertEquals(100, reservation.getReservationNo(), "Reservation No should match");
        assertEquals("John Doe", reservation.getGuestName(), "Guest Name should match");
        assertEquals(5, reservation.getRoomId(), "Room ID should match");
        assertEquals(Date.valueOf("2026-08-01"), reservation.getCheckInDate(), "Check-in date should match");
        assertEquals(15000.00, reservation.getTotalBill(), "Total bill should match");
    }
}