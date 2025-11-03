<%--
  Created by IntelliJ IDEA.
  User: alexd
  Date: 11/3/2025
  Time: 10:08 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head><meta charset="UTF-8"><title>Login</title></head>
<body>
<h2>Login</h2>

<% String err = (String) request.getAttribute("error");
    String ok  = (String) request.getAttribute("success");
    if (err != null) { %><p style="color:red;"><%= err %></p><% }
    if (ok  != null) { %><p style="color:green;"><%= ok %></p><% } %>

<form method="post" action="${pageContext.request.contextPath}/login">
    <label>Email:    <input name="email" type="email" required></label><br>
    <label>Password: <input name="password" type="password" required></label><br>
    <button type="submit">Sign in</button>
</form>

<p><a href="${pageContext.request.contextPath}/register">Create an account</a></p>
</body>
</html>
