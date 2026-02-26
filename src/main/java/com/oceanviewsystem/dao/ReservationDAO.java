package com.oceanviewsystem.dao;

import com.oceanviewsystem.model.Reservation;
import com.oceanviewsystem.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.CallableStatement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class ReservationDAO {

    // 1. Add a new reservation
    public boolean addReservation(Reservation reservation) {
        boolean isSuccess = false;
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "INSERT INTO reservations (guest_name, address, contact_number, room_id, check_in_date, check_out_date) VALUES (?, ?, ?, ?, ?, ?)";

        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, reservation.getGuestName());
            pstmt.setString(2, reservation.getAddress());
            pstmt.setString(3, reservation.getContactNumber());
            pstmt.setInt(4, reservation.getRoomId());
            pstmt.setDate(5, reservation.getCheckInDate());
            pstmt.setDate(6, reservation.getCheckOutDate());

            if (pstmt.executeUpdate() > 0) isSuccess = true;
        } catch (SQLException e) { e.printStackTrace(); }
        return isSuccess;
    }

    // 2. Fetch Reservation details by ID
    public Reservation getReservationById(int reservationNo) {
        Reservation res = null;
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM reservations WHERE reservation_no = ?";

        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, reservationNo);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                res = new Reservation();
                res.setReservationNo(rs.getInt("reservation_no"));
                res.setGuestName(rs.getString("guest_name"));
                res.setAddress(rs.getString("address"));
                res.setContactNumber(rs.getString("contact_number"));
                res.setRoomId(rs.getInt("room_id"));
                res.setCheckInDate(rs.getDate("check_in_date"));
                res.setCheckOutDate(rs.getDate("check_out_date"));
                res.setTotalBill(rs.getDouble("total_bill"));
                res.setStatus(rs.getString("status"));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return res;
    }

    // 3. Call Stored Procedure to Calculate Bill
    public boolean calculateBillUsingProcedure(int reservationNo) {
        boolean isSuccess = false;
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "{CALL CalculateBill(?, ?)}";
        try {
            CallableStatement stmt = conn.prepareCall(sql);
            stmt.setInt(1, reservationNo);
            stmt.registerOutParameter(2, Types.DECIMAL);
            stmt.execute();
            isSuccess = true;
        } catch (SQLException e) { e.printStackTrace(); }
        return isSuccess;
    }

    // 4. NEW METHOD: Fetch ALL Reservations to display in the Dashboard
    public List<Reservation> getAllReservations() {
        List<Reservation> list = new ArrayList<>();
        Connection conn = DBConnection.getInstance().getConnection();
        // Fetching all data, latest first
        String sql = "SELECT * FROM reservations ORDER BY created_at DESC";

        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Reservation res = new Reservation();
                res.setReservationNo(rs.getInt("reservation_no"));
                res.setGuestName(rs.getString("guest_name"));
                res.setAddress(rs.getString("address"));
                res.setContactNumber(rs.getString("contact_number"));
                res.setRoomId(rs.getInt("room_id"));
                res.setCheckInDate(rs.getDate("check_in_date"));
                res.setCheckOutDate(rs.getDate("check_out_date"));
                res.setTotalBill(rs.getDouble("total_bill"));
                res.setStatus(rs.getString("status"));
                list.add(res);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }
}