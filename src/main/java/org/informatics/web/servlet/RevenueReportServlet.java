package org.informatics.web.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.informatics.entity.Shipment;
import org.informatics.entity.enums.Role;
import org.informatics.service.ShipmentService;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@WebServlet("/revenue-report")
public class RevenueReportServlet extends HttpServlet {

    private final ShipmentService shipmentService = new ShipmentService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("userRole") != Role.EMPLOYEE) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        String startDateStr = request.getParameter("startDate");
        String endDateStr = request.getParameter("endDate");

        Double totalRevenue = null;
        List<Shipment> shipments = null;
        LocalDate startDate = null;
        LocalDate endDate = null;

        if (startDateStr != null && !startDateStr.isEmpty() &&
                endDateStr != null && !endDateStr.isEmpty()) {

            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                startDate = LocalDate.parse(startDateStr, formatter);
                endDate = LocalDate.parse(endDateStr, formatter);

                if (startDate.isAfter(endDate)) {
                    request.setAttribute("error", "Началната дата не може да е след крайната дата!");
                } else {
                    LocalDateTime startDateTime = startDate.atStartOfDay();
                    LocalDateTime endDateTime = endDate.atTime(23, 59, 59);

                    totalRevenue = shipmentService.calculateRevenueForPeriod(startDateTime, endDateTime);
                    shipments = shipmentService.getShipmentsForPeriod(startDateTime, endDateTime);

                    request.setAttribute("totalRevenue", totalRevenue);
                    request.setAttribute("shipments", shipments);
                }

            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("error", "Грешка при изчисляване на приходите: " + e.getMessage());
            }
        }

        request.setAttribute("startDate", startDateStr);
        request.setAttribute("endDate", endDateStr);

        request.getRequestDispatcher("/WEB-INF/views/revenue-report.jsp").forward(request, response);
    }
}
