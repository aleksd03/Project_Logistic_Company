package org.informatics.web.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.informatics.entity.Company;
import org.informatics.entity.enums.Role;
import org.informatics.entity.User;
import org.informatics.service.AuthService;
import org.informatics.service.ClientService;
import org.informatics.service.EmployeeService;
import org.informatics.service.CompanyService;

import java.io.IOException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    private final AuthService auth = new AuthService();
    private final ClientService clientService = new ClientService();
    private final EmployeeService employeeService = new EmployeeService();
    private final CompanyService companyService = new CompanyService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            String firstName = request.getParameter("firstName");
            String lastName  = request.getParameter("lastName");
            String email     = request.getParameter("email");
            String password  = request.getParameter("password");
            String roleStr   = request.getParameter("role");

            String isCompanyStr = request.getParameter("isCompany");
            String companyName = request.getParameter("companyName");
            boolean isCompany = "true".equals(isCompanyStr);

            System.out.println("=== REGISTER DEBUG ===");
            System.out.println("Email: " + email);
            System.out.println("Role: " + roleStr);
            System.out.println("Is Company: " + isCompany);
            System.out.println("Company Name: " + companyName);

            Role role = Role.valueOf(roleStr);

            User u = auth.register(firstName, lastName, email, password, role);

            System.out.println("User registered successfully: " + u.getEmail());
            System.out.println("User ID: " + u.getId());

            Company company = null;

            if (isCompany) {
                if (companyName == null || companyName.trim().isEmpty()) {
                    companyName = firstName + " " + lastName + " - Фирма";
                }

                System.out.println("Creating company: " + companyName);
                company = companyService.createCompany(companyName);
                System.out.println("Company created with ID: " + company.getId());
            }

            if (role == Role.CLIENT) {
                System.out.println("Creating Client record...");
                clientService.createForUser(u, company);
                System.out.println("Client record created");
            } else if (role == Role.EMPLOYEE) {
                System.out.println("Creating Employee record...");
                employeeService.createForUser(u, company, null);  // null за office
                System.out.println("Employee record created");
            }

            HttpSession session = request.getSession(true);

            session.setAttribute("userId", u.getId());
            session.setAttribute("userEmail", u.getEmail());
            session.setAttribute("userRole", u.getRole());
            session.setAttribute("firstName", u.getFirstName());
            session.setAttribute("lastName", u.getLastName());

            System.out.println("======================");

            response.sendRedirect(request.getContextPath() + "/");

        } catch (Exception ex) {
            System.out.println("Registration failed: " + ex.getMessage());
            ex.printStackTrace();
            request.setAttribute("error", ex.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
        }
    }
}