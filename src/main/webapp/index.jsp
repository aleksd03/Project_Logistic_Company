<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Logistic Company</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body>

<h2>Welcome to ALVAS Logistics</h2>

<p>This is our main page.</p>

<%
    String email = (String) session.getAttribute("email");
    String role  = (String) session.getAttribute("role");
%>

<p>
    <% if (email == null) { %>
    <a href="${pageContext.request.contextPath}/register">Register</a> |
    <a href="${pageContext.request.contextPath}/login">Login</a>
    <% } else { %>
    <a href="${pageContext.request.contextPath}/logout">Logout</a>
    <% } %>
</p>

<% if (email != null) {
    String r = (role != null) ? role : "UNKNOWN";
    String roleStr = r.charAt(0) + r.substring(1).toLowerCase();
%>
<p>You are signed in as <b><%= email %></b> (role: <%= roleStr %>)</p>

<p>
    <% if ("EMPLOYEE".equals(role)) { %>
    <a href="${pageContext.request.contextPath}/employee-shipments">All Shipments</a>
    <% } else if ("CLIENT".equals(role)) { %>
    <a href="${pageContext.request.contextPath}/client-shipments">My Shipments</a>
    <% } %>
</p>

<% } else { %>
<p>You are not signed in.</p>
<% } %>

</body>
</html>
