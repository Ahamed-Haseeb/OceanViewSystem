package com.oceanviewsystem.dao;

import com.oceanviewsystem.model.User;
import com.oceanviewsystem.util.DBConnection;
import com.oceanviewsystem.util.PasswordUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserDAO {

    // Standard Java Logger for advanced error tracking
    private static final Logger LOGGER = Logger.getLogger(UserDAO.class.getName());

    // 1. Authenticate user using SHA-256 hashed passwords
    public User authenticateUser(String username, String password) {
        User user = null;
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";

        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, PasswordUtil.hashPassword(password)); // Hash input before checking

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setRole(rs.getString("role"));
            }
        } catch (SQLException e) {
            // Replaced e.printStackTrace() with Advanced Logger
            LOGGER.log(Level.SEVERE, "Error authenticating user: " + username, e);
        }
        return user;
    }

    // --- NEW MASTER'S LEVEL FEATURES FOR ADMIN ---

    // 2. Add new staff with Hashed Password
    public boolean addStaff(String username, String rawPassword) {
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "INSERT INTO users (username, password, role) VALUES (?, ?, 'STAFF')";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, PasswordUtil.hashPassword(rawPassword)); // Save as Hash
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error adding new staff: " + username, e);
        }
        return false;
    }

    // 3. Delete existing staff
    public boolean deleteStaff(int userId) {
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "DELETE FROM users WHERE id = ? AND role = 'STAFF'";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deleting staff ID: " + userId, e);
        }
        return false;
    }

    // 4. Get list of all staff members
    public List<User> getAllStaff() {
        List<User> staffList = new ArrayList<>();
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "SELECT id, username, role FROM users WHERE role = 'STAFF'";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setRole(rs.getString("role"));
                staffList.add(user);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching staff list", e);
        }
        return staffList;
    }
}