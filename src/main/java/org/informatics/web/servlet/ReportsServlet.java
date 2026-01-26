package org.informatics.web.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.informatics.entity.enums.Role;
import org.informatics.service.ShipmentService;

import java.io.IOException;

@WebServlet("/reports")
public class ReportsServlet extends HttpServlet {

    private final ShipmentService shipmentService = new ShipmentService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("userRole") != Role.EMPLOYEE) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        try {
            int totalShipments = shipmentService.getAllShipments().size();

            request.setAttribute("totalShipments", totalShipments);
            request.getRequestDispatcher("/WEB-INF/views/reports.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Грешка при генериране на справки: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
        }
    }
}