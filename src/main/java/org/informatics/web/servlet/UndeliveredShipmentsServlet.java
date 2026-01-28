package org.informatics.web.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.informatics.entity.Shipment;
import org.informatics.entity.enums.Role;
import org.informatics.service.ShipmentService;

import java.io.IOException;
import java.util.List;

/**
 * Displays all shipments that have been sent but not yet received.
 * Accessible only by EMPLOYEE users.
 */
@WebServlet("/undelivered-shipments")
public class UndeliveredShipmentsServlet extends HttpServlet {

    // Service used to retrieve shipment data
    private final ShipmentService shipmentService = new ShipmentService();

    /**
     * Handles GET requests:
     * Loads all undelivered shipments and forwards them to the view.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Retrieve existing session
        HttpSession session = request.getSession(false);

        // Access control: only EMPLOYEE users may view this page
        if (session == null || session.getAttribute("userRole") != Role.EMPLOYEE) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        try {
            // Retrieve all shipments with status SENT
            List<Shipment> undeliveredShipments =
                    shipmentService.getUndeliveredShipments();

            // Expose data to the JSP
            request.setAttribute("shipments", undeliveredShipments);

            // Forward to view
            request.getRequestDispatcher(
                    "/WEB-INF/views/undelivered-shipments.jsp"
            ).forward(request, response);

        } catch (Exception e) {
            // Handle unexpected errors
            e.printStackTrace();
            request.setAttribute(
                    "error",
                    "Грешка при зареждане на неполучени пратки: " + e.getMessage()
            );
            request.getRequestDispatcher("/WEB-INF/views/error.jsp")
                   .forward(request, response);
        }
    }
}

