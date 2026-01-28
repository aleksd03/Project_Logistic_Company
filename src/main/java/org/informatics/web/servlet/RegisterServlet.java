package org.informatics.web.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.informatics.entity.Company;
import org.informatics.entity.Employee;
import org.informatics.entity.enums.EmployeeType;
import org.informatics.entity.enums.Role;
import org.informatics.entity.User;
import org.informatics.service.AuthService;
import org.informatics.service.ClientService;
import org.informatics.service.EmployeeService;
import org.informatics.service.CompanyService;

import java.io.IOException;

/**
 * Handles user registration for both CLIENT and EMPLOYEE roles.
 * Creates related Client / Employee records and optional Company.
 */
@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    // Authentication and domain services
    private final AuthService auth = new AuthService();
    private final ClientService clientService = new ClientService();
    private final EmployeeService employeeService = new EmployeeService();
    private final CompanyService companyService = new CompanyService();

    /**
     * Displays the registration page.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.getRequestDispatcher("/WEB-INF/views/register.jsp")
               .forward(request, response);
    }

    /**
     * Processes registration form submission.
     * - Validates input
     * - Registers user
     * - Creates Client or Employee entity
     * - Optionally creates a Company
     * - Logs the user in automatically
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            // =======================
            // READ FORM PARAMETERS
            // =======================
            String firstName = request.getParameter("firstName");
            String lastName  = request.getParameter("lastName");
            String email     = request.getParameter("email");
            String password  = request.getParameter("password");
            String roleStr   = request.getParameter("role");

            String isCompanyStr = request.getParameter("isCompany");
            String companyName = request.getParameter("companyName");
            String employeeTypeStr = request.getParameter("employeeType");

            boolean isCompany = "true".equals(isCompanyStr);

            // =======================
            // DEBUG OUTPUT
            // =======================
            System.out.println("=== REGISTER DEBUG ===");
            System.out.println("Email: " + email);
            System.out.println("Role: " + roleStr);
            System.out.println("Is Company: " + isCompany);
            System.out.println("Company Name: " + companyName);
            System.out.println("Employee Type: " + employeeTypeStr);

            // Convert role string to enum
            Role role = Role.valueOf(roleStr);

            // =======================
            // VALIDATION
            // =======================
            // Employee role requires employee type
            if (role == Role.EMPLOYEE &&
                (employeeTypeStr == null || employeeTypeStr.isEmpty())) {
                throw new IllegalArgumentException(
                        "Типът на служителя е задължителен!"
                );
            }

            // =======================
            // REGISTER USER
            // =======================
            User u = auth.register(firstName, lastName, email, password, role);

            System.out.println("User registered successfully: " + u.getEmail());
            System.out.println("User ID: " + u.getId());

            // =======================
            // OPTIONAL COMPANY CREATION
            // =======================
            Company company = null;

            if (isCompany) {
                // Auto-generate company name if empty
                if (companyName == null || companyName.trim().isEmpty()) {
                    companyName = firstName + " " + lastName + " - Фирма";
                }

                System.out.println("Creating company: " + companyName);
                company = companyService.createCompany(companyName);
                System.out.println("Company created with ID: " + company.getId());
            }

            // =======================
            // CREATE DOMAIN ENTITY
            // =======================
            if (role == Role.CLIENT) {
                // Create Client linked to User and optional Company
                System.out.println("Creating Client record...");
                clientService.createForUser(u, company);
                System.out.println("Client record created");

            } else if (role == Role.EMPLOYEE) {
                // Create Employee linked to User and optional Company
                System.out.println("Creating Employee record...");

                EmployeeType employeeType =
                        EmployeeType.valueOf(employeeTypeStr);

                System.out.println("Employee type: " + employeeType);

                Employee employee =
                        employeeService.createForUser(u, company, null);

                // Set and persist employee type
                employee.setEmployeeType(employeeType);
                employeeService.updateEmployee(employee);

                System.out.println(
                        "Employee record created with type: " + employeeType
                );
            }

            // =======================
            // AUTO LOGIN AFTER REGISTER
            // =======================
            HttpSession session = request.getSession(true);

            session.setAttribute("userId", u.getId());
            session.setAttribute("userEmail", u.getEmail());
            session.setAttribute("userRole", u.getRole());
            session.setAttribute("firstName", u.getFirstName());
            session.setAttribute("lastName", u.getLastName());

            System.out.println("======================");

            // Redirect to home page
            response.sendRedirect(request.getContextPath() + "/");

        }
        // =======================
        // VALIDATION ERROR
        // =======================
        catch (IllegalArgumentException ex) {
            System.out.println(
                    "Registration validation failed: " + ex.getMessage()
            );
            request.setAttribute("error", ex.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/register.jsp")
                   .forward(request, response);
        }
        // =======================
        // GENERAL ERROR
        // =======================
        catch (Exception ex) {
            System.out.println("Registration failed: " + ex.getMessage());
            ex.printStackTrace();
            request.setAttribute(
                    "error",
                    "Грешка при регистрация: " + ex.getMessage()
            );
            request.getRequestDispatcher("/WEB-INF/views/register.jsp")
                   .forward(request, response);
        }
    }
}
