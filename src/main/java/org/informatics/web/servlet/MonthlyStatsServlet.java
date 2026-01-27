package org.informatics.web.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.informatics.entity.Shipment;
import org.informatics.entity.enums.Role;
import org.informatics.entity.enums.ShipmentStatus;
import org.informatics.service.ClientService;
import org.informatics.service.ShipmentService;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

@WebServlet("/monthly-stats")
public class MonthlyStatsServlet extends HttpServlet {

    private final ShipmentService shipmentService = new ShipmentService();
    private final ClientService clientService = new ClientService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("userRole") != Role.EMPLOYEE) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        try {
            // Get current month
            YearMonth currentMonth = YearMonth.now();
            LocalDateTime startOfMonth = currentMonth.atDay(1).atStartOfDay();
            LocalDateTime endOfMonth = currentMonth.atEndOfMonth().atTime(23, 59, 59);

            // Get all shipments for current month
            List<Shipment> monthlyShipments = shipmentService.getShipmentsForPeriod(startOfMonth, endOfMonth);

            // Calculate statistics
            int totalShipments = monthlyShipments.size();

            long sentShipments = monthlyShipments.stream()
                    .filter(s -> s.getStatus() == ShipmentStatus.SENT)
                    .count();

            long receivedShipments = monthlyShipments.stream()
                    .filter(s -> s.getStatus() == ShipmentStatus.RECEIVED)
                    .count();

            double totalRevenue = monthlyShipments.stream()
                    .mapToDouble(Shipment::getPrice)
                    .sum();

            double averagePrice = totalShipments > 0 ? totalRevenue / totalShipments : 0;

            int totalClients = clientService.getAllClients().size();

            // Set attributes
            request.setAttribute("currentMonth", currentMonth.getMonth().toString());
            request.setAttribute("currentYear", currentMonth.getYear());
            request.setAttribute("totalShipments", totalShipments);
            request.setAttribute("sentShipments", sentShipments);
            request.setAttribute("receivedShipments", receivedShipments);
            request.setAttribute("totalRevenue", totalRevenue);
            request.setAttribute("averagePrice", averagePrice);
            request.setAttribute("totalClients", totalClients);
            request.setAttribute("shipments", monthlyShipments);

            request.getRequestDispatcher("/WEB-INF/views/monthly-stats.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Грешка при зареждане на месечна статистика: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
        }
    }
}
