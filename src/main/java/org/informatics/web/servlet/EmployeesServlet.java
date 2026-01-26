package org.informatics.web.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.informatics.entity.Employee;
import org.informatics.entity.enums.Role;
import org.informatics.service.EmployeeService;

import java.io.IOException;
import java.util.List;

@WebServlet("/employees")
public class EmployeesServlet extends HttpServlet {

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

        try {
            if ("view".equals(action)) {
                Long id = Long.parseLong(request.getParameter("id"));
                Employee employee = employeeService.getEmployeeById(id);
                request.setAttribute("employee", employee);
                request.getRequestDispatcher("/WEB-INF/views/employee-view.jsp").forward(request, response);
            } else {
                List<Employee> employees = employeeService.getAllEmployees();
                request.setAttribute("employees", employees);
                request.getRequestDispatcher("/WEB-INF/views/employees.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Грешка: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
        }
    }
}