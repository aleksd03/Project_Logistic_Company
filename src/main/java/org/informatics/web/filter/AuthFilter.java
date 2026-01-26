package org.informatics.web.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@WebFilter("/*")
public class AuthFilter implements Filter {
    private static final List<String> PUBLIC_URLS = Arrays.asList(
            "/login",
            "/register",
            "/assets"
    );

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String uri = req.getRequestURI();
        String contextPath = req.getContextPath();
        String path = uri.substring(contextPath.length());

        System.out.println("=== AUTH FILTER ===");
        System.out.println("Path: " + path);

        if (path.equals("/") || path.isEmpty()) {
            System.out.println("Home page - allowing");
            chain.doFilter(request, response);
            return;
        }

        boolean isPublic = PUBLIC_URLS.stream().anyMatch(path::startsWith);

        if (isPublic) {
            System.out.println("Public URL - allowing");
            chain.doFilter(request, response);
            return;
        }

        HttpSession session = req.getSession(false);

        if (session == null) {
            System.out.println("No session - redirecting to login");
            res.sendRedirect(contextPath + "/login");
            return;
        }

        String userEmail = (String) session.getAttribute("userEmail");
        Object userRole = session.getAttribute("userRole");

        System.out.println("Session userEmail: " + userEmail);
        System.out.println("Session userRole: " + userRole);

        if (userEmail == null || userRole == null) {
            System.out.println("Session invalid - redirecting to login");
            session.invalidate();
            res.sendRedirect(contextPath + "/login");
            return;
        }

        System.out.println("Authenticated - allowing");
        chain.doFilter(request, response);
    }
}