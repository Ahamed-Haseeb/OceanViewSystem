package com.oceanviewsystem.controller;

import com.google.gson.Gson;
import com.oceanviewsystem.dao.UserDAO;
import com.oceanviewsystem.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * Controller to handle Admin's Staff Management (Add/Delete/View Staff)
 */
@WebServlet(name = "UserController", urlPatterns = {"/add-staff", "/delete-staff", "/list-staff"})
public class UserController extends HttpServlet {

    private UserDAO userDAO = new UserDAO();
    private Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Add New Staff securely with Hashed Password
        if ("/add-staff".equals(request.getServletPath())) {
            String user = request.getParameter("username");
            String pass = request.getParameter("password");

            boolean success = userDAO.addStaff(user, pass);

            if (success) {
                response.sendRedirect("index.jsp?staffAdded=1");
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

        if ("/list-staff".equals(path)) {
            // Get all staff to display in the Admin Modal table
            List<User> staffList = userDAO.getAllStaff();
            out.print(gson.toJson(staffList));

        } else if ("/delete-staff".equals(path)) {
            // Delete a staff member
            int id = Integer.parseInt(request.getParameter("id"));
            boolean success = userDAO.deleteStaff(id);
            out.print("{\"success\": " + success + "}");
        }
        out.flush();
    }
}