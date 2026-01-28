package org.informatics.web.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;
import org.informatics.entity.enums.Role;

import java.io.IOException;

/**
 * Authorization filter that restricts access to certain routes
 * based on the user's role.
 *
 * Only users with EMPLOYEE role are allowed to access the
 * specified URLs.
 */
@WebFilter({
        "/employee-shipments",
        "/clients",
        "/employees",
        "/companies",
        "/offices",
        "/reports",
        "/shipment-register"
})
public class RoleFilter implements Filter {

    /**
     * Intercepts requests to protected URLs and checks
     * whether the user has the required role.
     */
    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain
    ) throws IOException, ServletException {

        // Cast to HTTP-specific request/response
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        // Retrieve existing session without creating a new one
        HttpSession session = req.getSession(false);

        System.out.println("=== ROLE FILTER ===");

        // If no session exists, redirect to login
        if (session == null) {
            System.out.println("No session - redirecting to login");
            res.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        // Extract user role from session
        Role userRole = (Role) session.getAttribute("userRole");

        System.out.println("User role: " + userRole);

        // Allow access only to EMPLOYEE role
        if (userRole != Role.EMPLOYEE) {
            System.out.println("Not EMPLOYEE - forbidden");
            res.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        // Authorized user — allow request to continue
        System.out.println("EMPLOYEE - allowing");
        chain.doFilter(request, response);
    }
}
