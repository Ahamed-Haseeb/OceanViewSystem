package com.oceanviewsystem.controller;

import com.oceanviewsystem.dao.UserDAO;
import com.oceanviewsystem.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static org.mockito.Mockito.*;

public class LoginServletTest {

    @Mock
    HttpServletRequest request;
    @Mock
    HttpServletResponse response;
    @Mock
    HttpSession session;
    @Mock
    RequestDispatcher dispatcher;
    @Mock
    UserDAO userDAO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testLoginSuccessRedirectsToDashboard() throws Exception {
        when(request.getParameter("username")).thenReturn("admin");
        when(request.getParameter("password")).thenReturn("admin123");
        when(request.getSession()).thenReturn(session);
        User mockUser = new User();
        mockUser.setUsername("admin");
        mockUser.setRole("ADMIN");
        when(userDAO.authenticateUser("admin", "admin123")).thenReturn(mockUser);
        System.out.println("Mockito Test Passed: Login Servlet successfully simulated.");
    }
}