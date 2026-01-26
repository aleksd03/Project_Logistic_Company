package org.informatics.web.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.informatics.entity.Client;
import org.informatics.entity.enums.Role;
import org.informatics.service.ClientService;

import java.io.IOException;
import java.util.List;

@WebServlet("/clients")
public class ClientsServlet extends HttpServlet {

    private final ClientService clientService = new ClientService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("userRole") != Role.EMPLOYEE) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        String action = request.getParameter("action");

        try {
            if ("view".equals(action)) {
                Long id = Long.parseLong(request.getParameter("id"));
                Client client = clientService.getClientById(id);
                request.setAttribute("client", client);
                request.getRequestDispatcher("/WEB-INF/views/client-view.jsp").forward(request, response);
            } else {
                List<Client> clients = clientService.getAllClients();
                request.setAttribute("clients", clients);
                request.getRequestDispatcher("/WEB-INF/views/clients.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Грешка: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
        }
    }
}