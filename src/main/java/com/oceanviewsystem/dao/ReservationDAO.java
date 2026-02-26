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
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReservationDAO {

    private static final Logger LOGGER = Logger.getLogger(ReservationDAO.class.getName());

    // 1. Add a new reservation (Now tracks Staff Member)
    public boolean addReservation(Reservation reservation) {
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "INSERT INTO reservations (guest_name, address, contact_number, room_id, check_in_date, check_out_date, created_by) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, reservation.getGuestName());
            pstmt.setString(2, reservation.getAddress());
            pstmt.setString(3, reservation.getContactNumber());
            pstmt.setInt(4, reservation.getRoomId());
            pstmt.setDate(5, reservation.getCheckInDate());
            pstmt.setDate(6, reservation.getCheckOutDate());
            pstmt.setString(7, reservation.getCreatedBy()); // Save Staff Username
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error adding reservation", e);
        }
        return false;
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
                res.setCreatedBy(rs.getString("created_by")); // Get Staff Username
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching reservation ID: " + reservationNo, e);
        }
        return res;
    }

    // 3. Call Stored Procedure to Calculate Bill
    public boolean calculateBillUsingProcedure(int reservationNo) {
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "{CALL CalculateBill(?, ?)}";
        try {
            CallableStatement stmt = conn.prepareCall(sql);
            stmt.setInt(1, reservationNo);
            stmt.registerOutParameter(2, Types.DECIMAL);
            stmt.execute();
            return true;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error calculating bill for ID: " + reservationNo, e);
        }
        return false;
    }

    // 4. Fetch ALL Reservations
    public List<Reservation> getAllReservations() {
        List<Reservation> list = new ArrayList<>();
        Connection conn = DBConnection.getInstance().getConnection();
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
                res.setCreatedBy(rs.getString("created_by")); // Get Staff Username
                list.add(res);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching all reservations", e);
        }
        return list;
    }

    // 5. Update Existing Reservation
    public boolean updateReservation(Reservation reservation) {
        Connection conn = DBConnection.getInstance().getConnection();
        // Automatically updates 'created_by' to the staff member who last edited it
        String sql = "UPDATE reservations SET guest_name=?, address=?, contact_number=?, room_id=?, check_in_date=?, check_out_date=?, created_by=? WHERE reservation_no=?";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, reservation.getGuestName());
            pstmt.setString(2, reservation.getAddress());
            pstmt.setString(3, reservation.getContactNumber());
            pstmt.setInt(4, reservation.getRoomId());
            pstmt.setDate(5, reservation.getCheckInDate());
            pstmt.setDate(6, reservation.getCheckOutDate());
            pstmt.setString(7, reservation.getCreatedBy());
            pstmt.setInt(8, reservation.getReservationNo());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating reservation ID: " + reservation.getReservationNo(), e);
        }
        return false;
    }

    // 6. Delete Reservation
    public boolean deleteReservation(int reservationNo) {
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "DELETE FROM reservations WHERE reservation_no=?";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, reservationNo);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deleting reservation ID: " + reservationNo, e);
        }
        return false;
    }
}