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

/**
 * Controller to handle all reservation-related API requests
 */
// Added "/all-reservations" to the URL patterns
@WebServlet(name = "ReservationController", urlPatterns = {"/reserve", "/bill", "/all-reservations"})
public class ReservationController extends HttpServlet {

    private ReservationService reservationService = new ReservationService();
    private ReservationDAO reservationDAO = new ReservationDAO();
    private Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();

        if (path.equals("/reserve")) {
            String name = request.getParameter("guestName");
            String address = request.getParameter("address");
            String contact = request.getParameter("contactNumber");
            int roomId = Integer.parseInt(request.getParameter("roomId"));
            Date checkIn = Date.valueOf(request.getParameter("checkInDate"));
            Date checkOut = Date.valueOf(request.getParameter("checkOutDate"));

            Reservation reservation = new Reservation(name, address, contact, roomId, checkIn, checkOut);
            boolean success = reservationDAO.addReservation(reservation);

            if (success) {
                response.sendRedirect("index.jsp?success=1");
            } else {
                response.sendRedirect("index.jsp?error=1");
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
        }
        // NEW ENDPOINT: Return all reservations as JSON
        else if (path.equals("/all-reservations")) {
            List<Reservation> list = reservationDAO.getAllReservations();
            out.print(gson.toJson(list));
        }
        out.flush();
    }
}