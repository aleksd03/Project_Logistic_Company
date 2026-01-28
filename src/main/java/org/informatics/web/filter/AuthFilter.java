package org.informatics.web.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Authentication filter that protects application routes.
 * Allows access to public URLs and enforces authentication
 * for all other requests.
 */
@WebFilter("/*")
public class AuthFilter implements Filter {

    /**
     * List of URL paths that do not require authentication.
     */
    private static final List<String> PUBLIC_URLS = Arrays.asList(
            "/login",
            "/register",
            "/assets"
    );

    /**
     * Intercepts every incoming request and checks whether
     * the user is authenticated before allowing access.
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

        // Extract request path without context path
        String uri = req.getRequestURI();
        String contextPath = req.getContextPath();
        String path = uri.substring(contextPath.length());

        System.out.println("=== AUTH FILTER ===");
        System.out.println("Path: " + path);

        // Allow home page without authentication
        if (path.equals("/") || path.isEmpty()) {
            System.out.println("Home page - allowing");
            chain.doFilter(request, response);
            return;
        }

        // Check if the request is for a public URL
        boolean isPublic = PUBLIC_URLS.stream()
                .anyMatch(path::startsWith);

        if (isPublic) {
            System.out.println("Public URL - allowing");
            chain.doFilter(request, response);
            return;
        }

        // Attempt to retrieve existing session (do not create a new one)
        HttpSession session = req.getSession(false);

        if (session == null) {
            System.out.println("No session - redirecting to login");
            res.sendRedirect(contextPath + "/login");
            return;
        }

        // Retrieve authentication attributes from session
        String userEmail = (String) session.getAttribute("userEmail");
        Object userRole = session.getAttribute("userRole");

        System.out.println("Session userEmail: " + userEmail);
        System.out.println("Session userRole: " + userRole);

        // Validate session contents
        if (userEmail == null || userRole == null) {
            System.out.println("Session invalid - redirecting to login");
            session.invalidate();
            res.sendRedirect(contextPath + "/login");
            return;
        }

        // User is authenticated — allow request to continue
        System.out.println("Authenticated - allowing");
        chain.doFilter(request, response);
    }
}
