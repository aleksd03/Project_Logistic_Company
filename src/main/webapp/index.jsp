<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Logistic Company</title>
</head>
<body>

<h2>Welcome to Logistic Company</h2>

<p>This is the main page of your web application.</p>

<p>
    <a href="${pageContext.request.contextPath}/register">Register</a> |
    <a href="${pageContext.request.contextPath}/login">Login</a> |
    <a href="${pageContext.request.contextPath}/logout">Logout</a>
</p>

<%
    Object email = session.getAttribute("email");
    Object role  = session.getAttribute("role");
    if (email != null) {
%>
<p>You are signed in as <b><%= email %></b> (role: <%= role %>)</p>
<%
} else {
%>
<p>You are not signed in.</p>
<%
    }
%>

</body>
</html>
