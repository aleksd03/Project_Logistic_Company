package org.informatics.web.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;
import org.informatics.entity.enums.Role;
import java.io.IOException;

@WebFilter({"/employee-shipments", "/clients", "/employees", "/companies",
        "/offices", "/reports", "/shipment-register"})
public class RoleFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);

        System.out.println("=== ROLE FILTER ===");

        if (session == null) {
            System.out.println("No session - redirecting to login");
            res.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        Role userRole = (Role) session.getAttribute("userRole");

        System.out.println("User role: " + userRole);

        if (userRole != Role.EMPLOYEE) {
            System.out.println("Not EMPLOYEE - forbidden");
            res.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        System.out.println("EMPLOYEE - allowing");
        chain.doFilter(request, response);
    }
}