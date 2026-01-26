package org.informatics.web.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.informatics.entity.Client;
import org.informatics.entity.Employee;
import org.informatics.entity.Office;
import org.informatics.entity.enums.Role;
import org.informatics.service.ClientService;
import org.informatics.service.EmployeeService;
import org.informatics.service.OfficeService;
import org.informatics.service.ShipmentService;

import java.io.IOException;
import java.util.List;

@WebServlet("/shipment-register")
public class ShipmentRegisterServlet extends HttpServlet {

    private final ShipmentService shipmentService = new ShipmentService();
    private final ClientService clientService = new ClientService();
    private final EmployeeService employeeService = new EmployeeService();
    private final OfficeService officeService = new OfficeService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("userRole") != Role.EMPLOYEE) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        try {
            List<Client> clients = clientService.getAllClients();
            List<Office> offices = officeService.getAllOffices();

            request.setAttribute("clients", clients);
            request.setAttribute("offices", offices);

            request.getRequestDispatcher("/WEB-INF/views/shipment-register.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Грешка при зареждане на формата: " + e.getMessage());
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

        try {
            Long userId = (Long) session.getAttribute("userId");
            Long senderId = Long.parseLong(request.getParameter("senderId"));
            Long receiverId = Long.parseLong(request.getParameter("receiverId"));
            double weight = Double.parseDouble(request.getParameter("weight"));

            String deliveryType = request.getParameter("deliveryType");
            boolean deliveryToOffice = "office".equals(deliveryType);

            Office deliveryOffice = null;
            String deliveryAddress = null;

            if (deliveryToOffice) {
                Long officeId = Long.parseLong(request.getParameter("officeId"));
                deliveryOffice = officeService.getOfficeById(officeId);
            } else {
                deliveryAddress = request.getParameter("deliveryAddress");
            }

            Employee employee = employeeService.getEmployeeByUserId(userId);
            if (employee == null) {
                throw new RuntimeException("Employee not found for user ID: " + userId);
            }

            Client sender = clientService.getClientById(senderId);
            Client receiver = clientService.getClientById(receiverId);

            if (sender == null || receiver == null) {
                throw new RuntimeException("Подателят или получателят не са намерени");
            }

            shipmentService.registerShipment(
                    sender,
                    receiver,
                    employee,
                    weight,
                    deliveryToOffice,
                    deliveryOffice,
                    deliveryAddress
            );

            response.sendRedirect(request.getContextPath() + "/employee-shipments?success=Пратката е регистрирана успешно!");

        } catch (NumberFormatException e) {
            e.printStackTrace();
            request.setAttribute("error", "Невалидни данни: " + e.getMessage());

            List<Client> clients = clientService.getAllClients();
            List<Office> offices = officeService.getAllOffices();
            request.setAttribute("clients", clients);
            request.setAttribute("offices", offices);

            request.getRequestDispatcher("/WEB-INF/views/shipment-register.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Грешка при регистриране на пратка: " + e.getMessage());

            List<Client> clients = clientService.getAllClients();
            List<Office> offices = officeService.getAllOffices();
            request.setAttribute("clients", clients);
            request.setAttribute("offices", offices);

            request.getRequestDispatcher("/WEB-INF/views/shipment-register.jsp").forward(request, response);
        }
    }
}