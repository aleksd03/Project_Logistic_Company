package org.informatics.web.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.informatics.entity.User;
import org.informatics.service.AuthService;

import java.io.IOException;
import java.util.Optional;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private final AuthService auth = new AuthService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        System.out.println("=== LOGIN DEBUG ===");
        System.out.println("Email: " + email);

        Optional<User> u = auth.login(email, password);

        if (u.isEmpty()) {
            System.out.println("Login failed - user not found or wrong password");
            request.setAttribute("error", "Невалиден имейл или парола!");
            request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
            return;
        }

        User user = u.get();
        System.out.println("User found: " + user.getEmail());
        System.out.println("User role: " + user.getRole());

        HttpSession session = request.getSession(true);
        System.out.println("Session ID: " + session.getId());

        session.setAttribute("userId", user.getId());
        session.setAttribute("userEmail", user.getEmail());
        session.setAttribute("userRole", user.getRole());
        session.setAttribute("firstName", user.getFirstName());
        session.setAttribute("lastName", user.getLastName());

        System.out.println("Session userId: " + session.getAttribute("userId"));
        System.out.println("Session userEmail: " + session.getAttribute("userEmail"));
        System.out.println("Session userRole: " + session.getAttribute("userRole"));
        System.out.println("Session firstName: " + session.getAttribute("firstName"));
        System.out.println("Session lastName: " + session.getAttribute("lastName"));
        System.out.println("===================");

        response.sendRedirect(request.getContextPath() + "/");
    }
}
