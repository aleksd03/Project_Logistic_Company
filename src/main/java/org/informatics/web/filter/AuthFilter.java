package org.informatics.web.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebFilter("/*")
public class AuthFilter extends HttpFilter {

    @Override
    protected void doFilter(HttpServletRequest request,
                            HttpServletResponse response,
                            FilterChain chain) throws IOException, ServletException {

        String path = request.getRequestURI().substring(request.getContextPath().length());

        // Public
        if (path.equals("/") ||
                path.equals("/index.jsp") ||
                path.equals("/login") ||
                path.equals("/register") ||
                path.equals("/logout") ||
                path.startsWith("/assets/")) {

            chain.doFilter(request, response);
            return;
        }

        var session = request.getSession(false);
        boolean signedIn = session != null && session.getAttribute("userId") != null;

        if (!signedIn) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        chain.doFilter(request, response);
    }
}
