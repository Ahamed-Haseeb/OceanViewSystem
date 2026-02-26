package com.oceanviewsystem.service;

import com.oceanviewsystem.dao.ReservationDAO;
import com.oceanviewsystem.model.Reservation;

public class ReservationService {

    private ReservationDAO reservationDAO;

    public ReservationService() {
        this.reservationDAO = new ReservationDAO();
    }

    // Method to get reservation details
    public Reservation getReservationDetails(int reservationNo) {
        return reservationDAO.getReservationById(reservationNo);
    }

    // Method to calculate and cleanly print the bill
    public Reservation calculateAndPrintBill(int reservationNo) {
        // 1. Tell the database to calculate the bill
        boolean isCalculated = reservationDAO.calculateBillUsingProcedure(reservationNo);

        // 2. Fetch the updated details with the new Total Bill
        Reservation updatedReservation = reservationDAO.getReservationById(reservationNo);

        // 3. Print the bill like a real hotel receipt
        if (isCalculated && updatedReservation != null) {
            System.out.println("\n====================================");
            System.out.println("      OCEAN VIEW RESORT - BILL      ");
            System.out.println("====================================");
            System.out.println("Reservation No : " + updatedReservation.getReservationNo());
            System.out.println("Guest Name     : " + updatedReservation.getGuestName());
            System.out.println("Check-in Date  : " + updatedReservation.getCheckInDate());
            System.out.println("Check-out Date : " + updatedReservation.getCheckOutDate());
            System.out.println("------------------------------------");
            System.out.println("TOTAL BILL     : Rs. " + updatedReservation.getTotalBill());
            System.out.println("====================================\n");
        } else {
            System.out.println("Sorry, could not generate the bill. Invalid Reservation Number.");
        }

        return updatedReservation;
    }
}