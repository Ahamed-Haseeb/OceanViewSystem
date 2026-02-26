package com.oceanviewsystem.controller;

import com.oceanviewsystem.dao.UserDAO;
import com.oceanviewsystem.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Controller to handle User Login Process
 */
@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {

    private UserDAO userDAO = new UserDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String user = request.getParameter("username");
        String pass = request.getParameter("password");

        // Authenticate using DAO
        User loggedInUser = userDAO.authenticateUser(user, pass);

        if (loggedInUser != null) {
            // Login Success: Create a Session
            HttpSession session = request.getSession();
            session.setAttribute("loggedUser", loggedInUser.getUsername());
            session.setAttribute("userRole", loggedInUser.getRole());

            // Redirect to the main dashboard
            response.sendRedirect("index.jsp");
        } else {
            // Login Failed: Redirect back to login page with error
            response.sendRedirect("login.jsp?error=1");
        }
    }
}