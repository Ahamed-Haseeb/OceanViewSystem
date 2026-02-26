package com.oceanviewsystem.dao;

import com.oceanviewsystem.model.User;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserDAOTest {

    @Test
    public void testAuthenticateUserSuccess() {
        UserDAO userDAO = new UserDAO();
        User loggedInUser = userDAO.authenticateUser("admin", "admin123");

        assertNotNull(loggedInUser, "Login failed! Database connection or credentials might be wrong.");
        assertEquals("admin", loggedInUser.getUsername(), "Username does not match!");
        assertEquals("ADMIN", loggedInUser.getRole(), "User role does not match!");

        System.out.println("Test Passed: Admin successfully logged in!");
    }

    @Test
    public void testAuthenticateUserFailure() {
        UserDAO userDAO = new UserDAO();
        User loggedInUser = userDAO.authenticateUser("admin", "wrongpassword");

        assertNull(loggedInUser, "Test Failed: User logged in even with a wrong password!");
        System.out.println("Test Passed: Invalid login was properly prevented.");
    }
}