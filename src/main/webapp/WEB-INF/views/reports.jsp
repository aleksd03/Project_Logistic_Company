<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<html>
<head>
    <title>Reports</title>
</head>
<body>

<h2>Reports</h2>

<h3>Employees</h3>
<ul>
    <c:forEach items="${employees}" var="e">
        <li>${e.user.email}</li>
    </c:forEach>
</ul>

<h3>Clients</h3>
<ul>
    <c:forEach items="${clients}" var="c">
        <li>${c.user.email}</li>
    </c:forEach>
</ul>

<h3>All Shipments</h3>
<ul>
    <c:forEach items="${shipments}" var="s">
        <li>ID ${s.id} — ${s.status} — ${s.price}</li>
    </c:forEach>
</ul>

<h3>Sent but not received</h3>
<ul>
    <c:forEach items="${sent}" var="s">
        <li>ID ${s.id}</li>
    </c:forEach>
</ul>

<h3>Total Revenue</h3>
<p>${revenue}</p>

</body>
</html>
