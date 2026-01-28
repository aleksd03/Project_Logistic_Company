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

/**
 * Servlet responsible for managing shipments from the employee perspective.
 * Allows EMPLOYEE users to view, filter, update, mark as received, and delete shipments.
 */
@WebServlet("/employee-shipments")
public class EmployeeShipmentsServlet extends HttpServlet {

    // Services used for shipment, office, and employee operations
    private final ShipmentService shipmentService = new ShipmentService();
    private final OfficeService officeService = new OfficeService();
    private final EmployeeService employeeService = new EmployeeService();

    /**
     * Handles GET requests:
     * - Lists shipments (optionally filtered by employee)
     * - Processes shipment deletion
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Retrieve existing session (do not create a new one)
        HttpSession session = request.getSession(false);

        // Allow access only to EMPLOYEE users
        if (session == null || session.getAttribute("userRole") != Role.EMPLOYEE) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        // Determine requested action
        String action = request.getParameter("action");

        // =======================
        // DELETE SHIPMENT ACTION
        // =======================
        if ("delete".equals(action)) {
            try {
                Long id = Long.parseLong(request.getParameter("id"));
                System.out.println("🗑️ Attempting to delete shipment with ID: " + id);

                shipmentService.deleteShipment(id);

                // Redirect with success message
                String successMsg = java.net.URLEncoder.encode(
                        "Пратката е изтрита успешно!",
                        "UTF-8"
                );
                response.sendRedirect(
                        request.getContextPath() + "/employee-shipments?success=" + successMsg
                );
                return;

            } catch (Exception e) {
                // Handle deletion errors
                e.printStackTrace();
                System.err.println("❌ Error deleting shipment: " + e.getMessage());

                String errorMsg = java.net.URLEncoder.encode(
                        "Грешка при изтриване: " + e.getMessage(),
                        "UTF-8"
                );
                response.sendRedirect(
                        request.getContextPath() + "/employee-shipments?error=" + errorMsg
                );
                return;
            }
        }

        // =======================
        // LIST SHIPMENTS (OPTIONAL FILTER)
        // =======================
        try {
            String filterEmployeeIdStr = request.getParameter("filterEmployeeId");
            List<Shipment> shipments;

            // Filter shipments by employee if filter is provided
            if (filterEmployeeIdStr != null
                    && !filterEmployeeIdStr.isEmpty()
                    && !"all".equals(filterEmployeeIdStr)) {

                Long filterEmployeeId = Long.parseLong(filterEmployeeIdStr);
                shipments = shipmentService.getShipmentsByEmployee(filterEmployeeId);

                System.out.println(
                        "📊 Filtering shipments by employee ID: " + filterEmployeeId
                );
            }
            // Otherwise, load all shipments
            else {
                shipments = shipmentService.getAllShipments();
            }

            // Load reference data for the view
            List<Office> offices = officeService.getAllOffices();
            List<Employee> employees = employeeService.getAllEmployees();

            // Pass data to JSP
            request.setAttribute("shipments", shipments);
            request.setAttribute("offices", offices);
            request.setAttribute("employees", employees);
            request.setAttribute("selectedEmployeeId", filterEmployeeIdStr);

            // Forward to employee shipments view
            request.getRequestDispatcher(
                    "/WEB-INF/views/employee-shipments.jsp"
            ).forward(request, response);

        } catch (Exception e) {
            // Handle loading errors
            e.printStackTrace();
            request.setAttribute(
                    "error",
                    "Грешка при зареждане на пратки: " + e.getMessage()
            );
            request.getRequestDispatcher(
                    "/WEB-INF/views/error.jsp"
            ).forward(request, response);
        }
    }

    /**
     * Handles POST requests:
     * - Updates shipment details
     * - Marks shipment as received
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Retrieve existing session (do not create a new one)
        HttpSession session = request.getSession(false);

        // Allow access only to EMPLOYEE users
        if (session == null || session.getAttribute("userRole") != Role.EMPLOYEE) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        String action = request.getParameter("action");

        try {
            // =======================
            // EDIT SHIPMENT ACTION
            // =======================
            if ("edit".equals(action)) {

                Long id = Long.parseLong(request.getParameter("id"));
                double weight = Double.parseDouble(request.getParameter("weight"));
                String deliveryType = request.getParameter("deliveryType");

                boolean deliveryToOffice = "office".equals(deliveryType);
                Office deliveryOffice = null;
                String deliveryAddress = null;

                // Delivery to office
                if (deliveryToOffice) {
                    String officeIdStr = request.getParameter("officeId");
                    if (officeIdStr != null && !officeIdStr.isEmpty()) {
                        Long officeId = Long.parseLong(officeIdStr);
                        deliveryOffice = officeService.getOfficeById(officeId);
                    }
                }
                // Delivery to address
                else {
                    deliveryAddress = request.getParameter("deliveryAddress");
                }

                // Update shipment
                shipmentService.updateShipment(
                        id,
                        weight,
                        deliveryToOffice,
                        deliveryOffice,
                        deliveryAddress
                );

                String successMsg = java.net.URLEncoder.encode(
                        "Пратката е актуализирана успешно!",
                        "UTF-8"
                );
                response.sendRedirect(
                        request.getContextPath() + "/employee-shipments?success=" + successMsg
                );
            }

            // =======================
            // MARK AS RECEIVED ACTION
            // =======================
            else if ("markReceived".equals(action)) {

                Long id = Long.parseLong(request.getParameter("id"));
                shipmentService.markAsReceived(id);

                String successMsg = java.net.URLEncoder.encode(
                        "Пратката е маркирана като получена!",
                        "UTF-8"
                );
                response.sendRedirect(
                        request.getContextPath() + "/employee-shipments?success=" + successMsg
                );
            }

        } catch (Exception e) {
            // Handle update errors
            e.printStackTrace();
            String errorMsg = java.net.URLEncoder.encode(
                    "Грешка: " + e.getMessage(),
                    "UTF-8"
            );
            response.sendRedirect(
                    request.getContextPath() + "/employee-shipments?error=" + errorMsg
            );
        }
    }
}
