package org.informatics.web.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.informatics.entity.Client;
import org.informatics.entity.Shipment;
import org.informatics.entity.enums.Role;
import org.informatics.service.ClientService;
import org.informatics.service.ShipmentService;

import java.io.IOException;
import java.util.List;

@WebServlet("/client-shipments")
public class ClientShipmentsServlet extends HttpServlet {

    private final ClientService clientService = new ClientService();
    private final ShipmentService shipmentService = new ShipmentService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        System.out.println("=== CLIENT SHIPMENTS DEBUG ===");

        if (session == null) {
            System.out.println("No session - redirecting to login");
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        Long userId = (Long) session.getAttribute("userId");
        Role userRole = (Role) session.getAttribute("userRole");

        System.out.println("User ID: " + userId);
        System.out.println("User Role: " + userRole);

        if (userId == null || userRole == null) {
            System.out.println("Session invalid - redirecting to login");
            session.invalidate();
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        if (userRole != Role.CLIENT) {
            System.out.println("Not a CLIENT - forbidden");
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        try {
            Client client = clientService.getClientByUserId(userId);

            if (client == null) {
                System.out.println("Client not found for user ID: " + userId);
                request.setAttribute("error", "Клиентски профил не е намерен!");
                request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
                return;
            }

            System.out.println("Client found: ID=" + client.getId());

            List<Shipment> sentShipments = shipmentService.getShipmentsBySender(client.getId());
            List<Shipment> receivedShipments = shipmentService.getShipmentsByReceiver(client.getId());

            System.out.println("Sent shipments: " + sentShipments.size());
            System.out.println("Received shipments: " + receivedShipments.size());
            System.out.println("==============================");

            request.setAttribute("sentShipments", sentShipments);
            request.setAttribute("receivedShipments", receivedShipments);
            request.getRequestDispatcher("/WEB-INF/views/client-shipments.jsp").forward(request, response);

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "Грешка при зареждане на пратки: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
        }
    }
}