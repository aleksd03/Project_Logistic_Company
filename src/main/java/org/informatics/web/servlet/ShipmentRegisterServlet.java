package org.informatics.web.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.informatics.entity.Client;
import org.informatics.entity.Employee;
import org.informatics.entity.Office;
import org.informatics.entity.Shipment;
import org.informatics.entity.enums.Role;
import org.informatics.service.ClientService;
import org.informatics.service.EmployeeService;
import org.informatics.service.OfficeService;
import org.informatics.service.ShipmentService;

import java.io.IOException;
import java.util.List;

/**
 * Handles shipment registration by employees.
 * Displays the registration form and processes shipment creation.
 */
@WebServlet("/shipment-register")
public class ShipmentRegisterServlet extends HttpServlet {

    // Services used for shipment creation and related entities
    private final ShipmentService shipmentService = new ShipmentService();
    private final ClientService clientService = new ClientService();
    private final EmployeeService employeeService = new EmployeeService();
    private final OfficeService officeService = new OfficeService();

    /**
     * Loads the shipment registration form.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Retrieve existing session
        HttpSession session = request.getSession(false);

        // Access control: only EMPLOYEE users may register shipments
        if (session == null || session.getAttribute("userRole") != Role.EMPLOYEE) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        try {
            System.out.println("📋 ShipmentRegisterServlet - Loading registration form");

            // Load all clients and offices for form dropdowns
            List<Client> clients = clientService.getAllClients();
            List<Office> offices = officeService.getAllOffices();

            System.out.println("✅ Loaded " + clients.size() + " clients and " + offices.size() + " offices");

            request.setAttribute("clients", clients);
            request.setAttribute("offices", offices);

            // Forward to JSP form
            request.getRequestDispatcher("/WEB-INF/views/shipment-register.jsp")
                   .forward(request, response);

        } catch (Exception e) {
            // Handle unexpected loading errors
            e.printStackTrace();
            System.err.println("❌ ERROR in ShipmentRegisterServlet doGet: " + e.getMessage());
            response.sendRedirect(
                    request.getContextPath() + "/employee-dashboard?error=Грешка+при+зареждане"
            );
        }
    }

    /**
     * Processes shipment registration form submission.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println("\n========================================");
        System.out.println("📦 SHIPMENT REGISTRATION - START");
        System.out.println("========================================");

        // Retrieve existing session
        HttpSession session = request.getSession(false);

        // Access control: EMPLOYEE role required
        if (session == null || session.getAttribute("userRole") != Role.EMPLOYEE) {
            System.err.println("❌ Session or role check failed");
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        try {
            // =======================
            // READ REQUEST PARAMETERS
            // =======================
            System.out.println("1️⃣ Getting parameters...");
            Long userId = (Long) session.getAttribute("userId");
            String senderIdStr = request.getParameter("senderId");
            String receiverIdStr = request.getParameter("receiverId");
            String weightStr = request.getParameter("weight");
            String deliveryType = request.getParameter("deliveryType");

            System.out.println("   - userId: " + userId);
            System.out.println("   - senderId: " + senderIdStr);
            System.out.println("   - receiverId: " + receiverIdStr);
            System.out.println("   - weight: " + weightStr);
            System.out.println("   - deliveryType: " + deliveryType);

            // Basic validation of required parameters
            if (senderIdStr == null || receiverIdStr == null ||
                weightStr == null || deliveryType == null) {
                throw new IllegalArgumentException("Missing required parameters");
            }

            // =======================
            // PARSE PARAMETERS
            // =======================
            System.out.println("2️⃣ Parsing parameters...");
            Long senderId = Long.parseLong(senderIdStr);
            Long receiverId = Long.parseLong(receiverIdStr);
            double weight = Double.parseDouble(weightStr);

            // =======================
            // DELIVERY TYPE HANDLING
            // =======================
            System.out.println("3️⃣ Processing delivery type...");
            boolean deliveryToOffice = "office".equals(deliveryType);

            Office deliveryOffice = null;
            String deliveryAddress = null;

            if (deliveryToOffice) {
                String officeIdStr = request.getParameter("officeId");
                System.out.println("   - Office delivery, officeId: " + officeIdStr);
                if (officeIdStr != null && !officeIdStr.isEmpty()) {
                    Long officeId = Long.parseLong(officeIdStr);
                    deliveryOffice = officeService.getOfficeById(officeId);
                    System.out.println("   - Office found: " + (deliveryOffice != null));
                }
            } else {
                deliveryAddress = request.getParameter("deliveryAddress");
                System.out.println("   - Address delivery: " + deliveryAddress);
            }

            // =======================
            // FIND EMPLOYEE
            // =======================
            System.out.println("4️⃣ Finding employee...");
            Employee employee = employeeService.getEmployeeByUserId(userId);
            if (employee == null) {
                throw new RuntimeException("Employee not found for user ID: " + userId);
            }
            System.out.println("   - Employee found: " + employee.getId());

            // =======================
            // FIND CLIENTS
            // =======================
            System.out.println("5️⃣ Finding clients...");
            Client sender = clientService.getClientById(senderId);
            Client receiver = clientService.getClientById(receiverId);

            if (sender == null || receiver == null) {
                throw new RuntimeException("Sender or receiver not found");
            }

            System.out.println("   - Sender found: " + sender.getId());
            System.out.println("   - Receiver found: " + receiver.getId());

            // =======================
            // REGISTER SHIPMENT
            // =======================
            System.out.println("6️⃣ Registering shipment...");
            Shipment shipment = shipmentService.registerShipment(
                    sender,
                    receiver,
                    employee,
                    weight,
                    deliveryToOffice,
                    deliveryOffice,
                    deliveryAddress
            );

            System.out.println("✅ Shipment registered successfully!");
            System.out.println("   - Shipment ID: " + shipment.getId());
            System.out.println("   - Price: " + shipment.getPrice() + "€");

            // =======================
            // REDIRECT ON SUCCESS
            // =======================
            System.out.println("7️⃣ Redirecting...");
            String successMessage = java.net.URLEncoder.encode(
                    "Пратката е регистрирана успешно!", "UTF-8"
            );
            String redirectUrl =
                    request.getContextPath() + "/employee-shipments?success=" + successMessage;

            System.out.println("   - Redirect URL: " + redirectUrl);
            response.sendRedirect(redirectUrl);

            System.out.println("✅ Redirect sent successfully!");
            System.out.println("========================================\n");

        } catch (NumberFormatException e) {
            // Handle invalid numeric input
            System.err.println("❌ NumberFormatException: " + e.getMessage());
            e.printStackTrace();
            try {
                String errorMsg = java.net.URLEncoder.encode("Невалидни данни", "UTF-8");
                response.sendRedirect(
                        request.getContextPath() + "/shipment-register?error=" + errorMsg
                );
            } catch (Exception ex) {
                System.err.println("❌ Failed to send error redirect: " + ex.getMessage());
            }

        } catch (Exception e) {
            // Handle all other errors
            System.err.println("❌ Exception during shipment registration: " + e.getMessage());
            e.printStackTrace();
            try {
                String errorMsg = e.getMessage() != null
                        ? java.net.URLEncoder.encode(e.getMessage(), "UTF-8")
                        : "Unknown+error";
                response.sendRedirect(
                        request.getContextPath() + "/shipment-register?error=" + errorMsg
                );
            } catch (Exception ex) {
                System.err.println("❌ Failed to send error redirect: " + ex.getMessage());
            }
        }
    }
}
