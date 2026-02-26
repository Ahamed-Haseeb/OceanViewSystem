package com.oceanviewsystem.controller;

import com.google.gson.Gson;
import com.oceanviewsystem.model.Reservation;
import com.oceanviewsystem.service.ReservationService;
import com.oceanviewsystem.dao.ReservationDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.util.List;

// Added new endpoints for updating and deleting
@WebServlet(name = "ReservationController", urlPatterns = {"/reserve", "/bill", "/all-reservations", "/update-res", "/delete-res"})
public class ReservationController extends HttpServlet {

    private ReservationService reservationService = new ReservationService();
    private ReservationDAO reservationDAO = new ReservationDAO();
    private Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();

        if (path.equals("/reserve") || path.equals("/update-res")) {
            String name = request.getParameter("guestName");
            String address = request.getParameter("address");
            String contact = request.getParameter("contactNumber");
            int roomId = Integer.parseInt(request.getParameter("roomId"));
            Date checkIn = Date.valueOf(request.getParameter("checkInDate"));
            Date checkOut = Date.valueOf(request.getParameter("checkOutDate"));

            // --- NEW: ADVANCED DATE VALIDATION ---
            if (!checkOut.after(checkIn)) {
                // Check-out date is not after Check-in date!
                response.sendRedirect("index.jsp?error=date");
                return; // Stop the process
            }
            // -------------------------------------

            Reservation reservation = new Reservation(name, address, contact, roomId, checkIn, checkOut);

            if (path.equals("/reserve")) {
                // ADD NEW
                boolean success = reservationDAO.addReservation(reservation);
                response.sendRedirect("index.jsp?" + (success ? "success=1" : "error=1"));
            } else {
                // UPDATE EXISTING
                int resNo = Integer.parseInt(request.getParameter("reservationNo"));
                reservation.setReservationNo(resNo);
                boolean success = reservationDAO.updateReservation(reservation);
                response.sendRedirect("index.jsp?" + (success ? "updated=1" : "error=1"));
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        if (path.equals("/bill")) {
            int resNo = Integer.parseInt(request.getParameter("resNo"));
            Reservation billedInfo = reservationService.calculateAndPrintBill(resNo);
            out.print(gson.toJson(billedInfo));

        } else if (path.equals("/all-reservations")) {
            List<Reservation> list = reservationDAO.getAllReservations();
            out.print(gson.toJson(list));

        } else if (path.equals("/delete-res")) {
            // DELETE RESERVATION VIA AJAX
            int resNo = Integer.parseInt(request.getParameter("resNo"));
            boolean success = reservationDAO.deleteReservation(resNo);
            out.print("{\"success\": " + success + "}");
        }
        out.flush();
    }
}