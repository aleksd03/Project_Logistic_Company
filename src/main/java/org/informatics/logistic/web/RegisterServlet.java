package org.informatics.logistic.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.informatics.logistic.entity.Role;
import org.informatics.logistic.service.AuthService;

import java.io.IOException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private final AuthService auth = new AuthService();

    @Override protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(req, resp);
    }

    @Override protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        String first = req.getParameter("firstName");
        String last  = req.getParameter("lastName");
        String email = req.getParameter("email");
        String pass  = req.getParameter("password");
        String conf  = req.getParameter("confirm");
        String role  = req.getParameter("role");

        if (pass == null || !pass.equals(conf)) {
            req.setAttribute("error", "Passwords do not match.");
            req.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(req, resp);
            return;
        }

        try {
            auth.register(first, last, email, pass, Role.valueOf(role));
            req.setAttribute("success", "Account created. Please sign in.");
        } catch (IllegalArgumentException ex) {
            req.setAttribute("error", ex.getMessage());
        }

        req.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(req, resp);
    }
}