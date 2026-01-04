package org.informatics.web.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.informatics.service.ShipmentQueryService;

import java.io.IOException;

@WebServlet("/client-shipments")
public class ClientShipmentsServlet extends HttpServlet {

    private final ShipmentQueryService shipments = new ShipmentQueryService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        String email = session == null ? null : (String) session.getAttribute("email");
        if (email == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        request.setAttribute("shipments", shipments.getForClientEmail(email));
        request.getRequestDispatcher("/WEB-INF/views/client-shipments.jsp").forward(request, response);
    }
}
