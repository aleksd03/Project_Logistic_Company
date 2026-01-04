package org.informatics.web.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.informatics.service.ShipmentQueryService;

import java.io.IOException;

@WebServlet("/employee-shipments")
public class EmployeeShipmentsServlet extends HttpServlet {

    private final ShipmentQueryService shipments = new ShipmentQueryService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setAttribute("shipments", shipments.getAllForEmployee());
        request.getRequestDispatcher("/WEB-INF/views/employee-shipments.jsp").forward(request, response);
    }
}
