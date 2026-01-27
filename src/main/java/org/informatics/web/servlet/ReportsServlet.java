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

@WebServlet("/reports")
public class ReportsServlet extends HttpServlet {

    private final ShipmentService shipmentService = new ShipmentService();
    private final ClientService clientService = new ClientService();
    private final EmployeeService employeeService = new EmployeeService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("userRole") != Role.EMPLOYEE) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
            // Get all data
            List<Shipment> allShipments = shipmentService.getAllShipments();
            List<Client> allClients = clientService.getAllClients();
            List<Employee> allEmployees = employeeService.getAllEmployees();

            // Calculate statistics
            int totalShipments = allShipments.size();

            long activeDeliveries = allShipments.stream()
                    .filter(s -> s.getStatus() == ShipmentStatus.SENT)
                    .count();

            long completedDeliveries = allShipments.stream()
                    .filter(s -> s.getStatus() == ShipmentStatus.RECEIVED)
                    .count();

            double totalRevenue = allShipments.stream()
                    .mapToDouble(Shipment::getPrice)
                    .sum();

            int totalClients = allClients.size();
            int totalEmployees = allEmployees.size();

            // Set attributes
            request.setAttribute("totalShipments", totalShipments);
            request.setAttribute("activeDeliveries", activeDeliveries);
            request.setAttribute("completedDeliveries", completedDeliveries);
            request.setAttribute("totalRevenue", totalRevenue);
            request.setAttribute("totalClients", totalClients);
            request.setAttribute("totalEmployees", totalEmployees);

            request.getRequestDispatcher("/WEB-INF/views/reports.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Грешка при зареждане на справки: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
        }
    }
}