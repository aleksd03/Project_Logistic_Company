package org.informatics.web.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.informatics.entity.Employee;
import org.informatics.entity.Office;
import org.informatics.entity.Shipment;
import org.informatics.entity.enums.Role;
import org.informatics.service.EmployeeService;
import org.informatics.service.OfficeService;
import org.informatics.service.ShipmentService;

import java.io.IOException;
import java.util.List;

@WebServlet("/employee-shipments")
public class EmployeeShipmentsServlet extends HttpServlet {

    private final ShipmentService shipmentService = new ShipmentService();
    private final OfficeService officeService = new OfficeService();
    private final EmployeeService employeeService = new EmployeeService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("userRole") != Role.EMPLOYEE) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        String action = request.getParameter("action");

        // DELETE ACTION
        if ("delete".equals(action)) {
            try {
                Long id = Long.parseLong(request.getParameter("id"));
                System.out.println("🗑️ Attempting to delete shipment with ID: " + id);

                shipmentService.deleteShipment(id);

                String successMsg = java.net.URLEncoder.encode("Пратката е изтрита успешно!", "UTF-8");
                response.sendRedirect(request.getContextPath() + "/employee-shipments?success=" + successMsg);
                return;
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("❌ Error deleting shipment: " + e.getMessage());
                String errorMsg = java.net.URLEncoder.encode("Грешка при изтриване: " + e.getMessage(), "UTF-8");
                response.sendRedirect(request.getContextPath() + "/employee-shipments?error=" + errorMsg);
                return;
            }
        }

        // LIST ALL (with optional filter)
        try {
            String filterEmployeeIdStr = request.getParameter("filterEmployeeId");
            List<Shipment> shipments;

            if (filterEmployeeIdStr != null && !filterEmployeeIdStr.isEmpty() && !"all".equals(filterEmployeeIdStr)) {
                // FILTER BY EMPLOYEE
                Long filterEmployeeId = Long.parseLong(filterEmployeeIdStr);
                shipments = shipmentService.getShipmentsByEmployee(filterEmployeeId);
                System.out.println("📊 Filtering shipments by employee ID: " + filterEmployeeId);
            } else {
                // ALL SHIPMENTS
                shipments = shipmentService.getAllShipments();
            }

            List<Office> offices = officeService.getAllOffices();
            List<Employee> employees = employeeService.getAllEmployees();

            request.setAttribute("shipments", shipments);
            request.setAttribute("offices", offices);
            request.setAttribute("employees", employees);
            request.setAttribute("selectedEmployeeId", filterEmployeeIdStr);

            request.getRequestDispatcher("/WEB-INF/views/employee-shipments.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Грешка при зареждане на пратки: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("userRole") != Role.EMPLOYEE) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        String action = request.getParameter("action");

        try {
            if ("edit".equals(action)) {
                Long id = Long.parseLong(request.getParameter("id"));
                double weight = Double.parseDouble(request.getParameter("weight"));
                String deliveryType = request.getParameter("deliveryType");

                boolean deliveryToOffice = "office".equals(deliveryType);
                Office deliveryOffice = null;
                String deliveryAddress = null;

                if (deliveryToOffice) {
                    String officeIdStr = request.getParameter("officeId");
                    if (officeIdStr != null && !officeIdStr.isEmpty()) {
                        Long officeId = Long.parseLong(officeIdStr);
                        deliveryOffice = officeService.getOfficeById(officeId);
                    }
                } else {
                    deliveryAddress = request.getParameter("deliveryAddress");
                }

                shipmentService.updateShipment(id, weight, deliveryToOffice, deliveryOffice, deliveryAddress);

                String successMsg = java.net.URLEncoder.encode("Пратката е актуализирана успешно!", "UTF-8");
                response.sendRedirect(request.getContextPath() + "/employee-shipments?success=" + successMsg);

            } else if ("markReceived".equals(action)) {
                Long id = Long.parseLong(request.getParameter("id"));

                shipmentService.markAsReceived(id);

                String successMsg = java.net.URLEncoder.encode("Пратката е маркирана като получена!", "UTF-8");
                response.sendRedirect(request.getContextPath() + "/employee-shipments?success=" + successMsg);
            }

        } catch (Exception e) {
            e.printStackTrace();
            String errorMsg = java.net.URLEncoder.encode("Грешка: " + e.getMessage(), "UTF-8");
            response.sendRedirect(request.getContextPath() + "/employee-shipments?error=" + errorMsg);
        }
    }
}