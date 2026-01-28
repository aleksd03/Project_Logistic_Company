package org.informatics.web.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.informatics.entity.Company;
import org.informatics.entity.Office;
import org.informatics.entity.enums.Role;
import org.informatics.service.CompanyService;
import org.informatics.service.OfficeService;

import java.io.IOException;
import java.util.List;

/**
 * Servlet responsible for managing offices.
 * Allows EMPLOYEE users to view, create, update, and delete offices.
 */
@WebServlet("/offices")
public class OfficesServlet extends HttpServlet {

    // Services used for office and company operations
    private final OfficeService officeService = new OfficeService();
    private final CompanyService companyService = new CompanyService();

    /**
     * Handles GET requests:
     * - Displays the offices list
     * - Processes delete and edit actions
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

        // Check for optional action parameter
        String action = request.getParameter("action");

        // =======================
        // DELETE OFFICE ACTION
        // =======================
        if ("delete".equals(action)) {
            try {
                Long id = Long.parseLong(request.getParameter("id"));
                System.out.println("🗑️ Attempting to delete office with ID: " + id);

                officeService.deleteOffice(id);

                // Redirect with success message
                String successMsg = java.net.URLEncoder.encode(
                        "Офисът е изтрит успешно!",
                        "UTF-8"
                );
                response.sendRedirect(
                        request.getContextPath() + "/offices?success=" + successMsg
                );
                return;

            } catch (Exception e) {
                // Handle deletion errors
                e.printStackTrace();
                System.err.println("❌ Error deleting office: " + e.getMessage());

                String errorMsg = java.net.URLEncoder.encode(
                        "Грешка при изтриване: " + e.getMessage(),
                        "UTF-8"
                );
                response.sendRedirect(
                        request.getContextPath() + "/offices?error=" + errorMsg
                );
                return;
            }
        }

        // =======================
        // EDIT OFFICE ACTION
        // =======================
        if ("edit".equals(action)) {
            try {
                Long id = Long.parseLong(request.getParameter("id"));
                Office office = officeService.getOfficeById(id);

                // Pass office to the view for editing
                request.setAttribute("editOffice", office);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // =======================
        // LIST ALL OFFICES
        // =======================
        try {
            List<Office> offices = officeService.getAllOffices();
            List<Company> companies = companyService.getAllCompanies();

            // Pass data to the JSP view
            request.setAttribute("offices", offices);
            request.setAttribute("companies", companies);

            // Forward to offices view
            request.getRequestDispatcher(
                    "/WEB-INF/views/offices.jsp"
            ).forward(request, response);

        } catch (Exception e) {
            // Handle loading errors
            e.printStackTrace();
            request.setAttribute(
                    "error",
                    "Грешка при зареждане на офиси: " + e.getMessage()
            );
            request.getRequestDispatcher(
                    "/WEB-INF/views/error.jsp"
            ).forward(request, response);
        }
    }

    /**
     * Handles POST requests:
     * - Creates a new office
     * - Updates an existing office
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

        try {
            // Extract request parameters
            String idParam = request.getParameter("id");
            String address = request.getParameter("address");
            Long companyId = Long.parseLong(request.getParameter("companyId"));

            // Retrieve associated company
            Company company = companyService.getCompanyById(companyId);

            // Update existing office
            if (idParam != null && !idParam.isEmpty()) {
                Long id = Long.parseLong(idParam);
                Office office = officeService.getOfficeById(id);

                if (office != null) {
                    office.setAddress(address);
                    office.setCompany(company);
                    officeService.updateOffice(office);
                }
            }
            // Create new office
            else {
                officeService.createOffice(address, company);
            }

            // Redirect back to offices list
            response.sendRedirect(request.getContextPath() + "/offices");

        } catch (Exception e) {
            // Handle create/update errors
            e.printStackTrace();
            String errorMsg = java.net.URLEncoder.encode(
                    e.getMessage(),
                    "UTF-8"
            );
            response.sendRedirect(
                    request.getContextPath() + "/offices?error=" + errorMsg
            );
        }
    }
}
