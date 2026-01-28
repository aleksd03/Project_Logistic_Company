package org.informatics.web.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.informatics.entity.Client;
import org.informatics.entity.Employee;
import org.informatics.entity.Shipment;
import org.informatics.entity.enums.Role;
import org.informatics.entity.enums.ShipmentStatus;
import org.informatics.service.ClientService;
import org.informatics.service.EmployeeService;
import org.informatics.service.ShipmentService;

import java.io.IOException;
import java.util.List;

/**
 * Generates global system reports and statistics.
 * Accessible only by EMPLOYEE users.
 */
@WebServlet("/reports")
public class ReportsServlet extends HttpServlet {

    // Services used to retrieve domain data
    private final ShipmentService shipmentService = new ShipmentService();
    private final ClientService clientService = new ClientService();
    private final EmployeeService employeeService = new EmployeeService();

    /**
     * Displays the reports page with aggregated statistics.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Get existing session (do not create new)
        HttpSession session = request.getSession(false);

        // Access control: only EMPLOYEE users are allowed
        if (session == null || session.getAttribute("userRole") != Role.EMPLOYEE) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
            // =======================
            // LOAD ALL DATA
            // =======================
            List<Shipment> allShipments = shipmentService.getAllShipments();
            List<Client> allClients = clientService.getAllClients();
            List<Employee> allEmployees = employeeService.getAllEmployees();

            // =======================
            // CALCULATE STATISTICS
            // =======================
            int totalShipments = allShipments.size();

            // Shipments that are sent but not yet received
            long activeDeliveries = allShipments.stream()
                    .filter(s -> s.getStatus() == ShipmentStatus.SENT)
                    .count();

            // Shipments that are already delivered
            long completedDeliveries = allShipments.stream()
                    .filter(s -> s.getStatus() == ShipmentStatus.RECEIVED)
                    .count();

            // Sum of prices of all shipments
            double totalRevenue = allShipments.stream()
                    .mapToDouble(Shipment::getPrice)
                    .sum();

            int totalClients = allClients.size();
            int totalEmployees = allEmployees.size();

            // =======================
            // SET VIEW ATTRIBUTES
            // =======================
            request.setAttribute("totalShipments", totalShipments);
            request.setAttribute("activeDeliveries", activeDeliveries);
            request.setAttribute("completedDeliveries", completedDeliveries);
            request.setAttribute("totalRevenue", totalRevenue);
            request.setAttribute("totalClients", totalClients);
            request.setAttribute("totalEmployees", totalEmployees);

            // Forward to reports JSP
            request.getRequestDispatcher("/WEB-INF/views/reports.jsp")
                   .forward(request, response);

        } catch (Exception e) {
            // Handle unexpected errors gracefully
            e.printStackTrace();
            request.setAttribute(
                    "error",
                    "Грешка при зареждане на справки: " + e.getMessage()
            );
            request.getRequestDispatcher("/WEB-INF/views/error.jsp")
                   .forward(request, response);
        }
    }
}
