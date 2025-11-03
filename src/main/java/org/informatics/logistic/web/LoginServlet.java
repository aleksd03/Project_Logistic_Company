package org.informatics.logistic.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import org.informatics.logistic.service.AuthService;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private final AuthService auth = new AuthService();

    @Override protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
    }

    @Override protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String email = req.getParameter("email");
        String pass  = req.getParameter("password");

        auth.login(email, pass).ifPresentOrElse(user -> {
            HttpSession session = req.getSession(true);
            session.setAttribute("userId", user.getId());
            session.setAttribute("email",  user.getEmail());
            session.setAttribute("role",   user.getRole().name());

            try {
                resp.sendRedirect(req.getContextPath() + "/");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }, () -> {
            try {
                req.setAttribute("error", "Invalid email or password.");
                req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
}

