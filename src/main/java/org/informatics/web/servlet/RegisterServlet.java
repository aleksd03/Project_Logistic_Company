package org.informatics.web.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.informatics.entity.Role;
import org.informatics.entity.User;
import org.informatics.service.AuthService;

import java.io.IOException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    private final AuthService auth = new AuthService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            String firstName = request.getParameter("firstName");
            String lastName  = request.getParameter("lastName");
            String email     = request.getParameter("email");
            String password  = request.getParameter("password");
            String roleStr   = request.getParameter("role");

            Role role = Role.valueOf(roleStr);

            User u = auth.register(firstName, lastName, email, password, role);

            // auto login after register
            HttpSession session = request.getSession(true);
            session.setAttribute("userId", u.getId());
            session.setAttribute("email", u.getEmail());
            session.setAttribute("role", u.getRole().name());

            response.sendRedirect(request.getContextPath() + "/");
        } catch (IllegalArgumentException ex) {
            request.setAttribute("error", ex.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
        }
    }
}
