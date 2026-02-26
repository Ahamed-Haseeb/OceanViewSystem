package com.oceanviewsystem.dao;

import com.oceanviewsystem.model.User;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class UserDAOTest {

    private final UserDAO userDAO = new UserDAO();

    @Test
    public void testAuthenticateAdminSuccess() {
        // Assuming admin with password 'admin123' exists in DB and is properly hashed
        User user = userDAO.authenticateUser("admin", "admin123");
        assertNotNull(user, "Admin user should be successfully authenticated");
        assertEquals("ADMIN", user.getRole(), "Role should be ADMIN");
    }

    @Test
    public void testAuthenticateUserFailure() {
        User user = userDAO.authenticateUser("wronguser", "wrongpass");
        assertNull(user, "User should be null for invalid credentials");
    }

    @Test
    public void testAddAndFetchStaff() {
        // Generate a unique test username
        String testUsername = "teststaff_" + System.currentTimeMillis();

        // Test Add Staff
        boolean isAdded = userDAO.addStaff(testUsername, "testpass123");
        assertTrue(isAdded, "New staff should be added successfully");

        // Test Fetch Staff List
        List<User> staffList = userDAO.getAllStaff();
        assertNotNull(staffList, "Staff list should not be null");
        assertTrue(staffList.size() > 0, "Staff list should contain at least one staff member");
    }

    @Test
    public void testDeleteStaffSuccess() {
        String testUsername = "todelete_" + System.currentTimeMillis();
        userDAO.addStaff(testUsername, "pass123");

        List<User> staffList = userDAO.getAllStaff();
        int idToDelete = -1;
        for(User u : staffList) {
            if(u.getUsername().equals(testUsername)) {
                idToDelete = u.getId();
                break;
            }
        }
        boolean isDeleted = userDAO.deleteStaff(idToDelete);
        assertTrue(isDeleted, "Staff should be deleted successfully");
    }

    @Test
    public void testDeleteStaffFailure() {
        boolean isDeleted = userDAO.deleteStaff(-999);
        assertFalse(isDeleted, "Deleting non-existent staff should fail");
    }

    @Test
    public void testGetAllStaff() {
        List<User> staffList = userDAO.getAllStaff();
        assertNotNull(staffList, "Staff list should not be null");
    }
}