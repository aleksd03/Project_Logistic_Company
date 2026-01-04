<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>My Shipments</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body>
<h2>My Shipments (Client)</h2>

<table border="1" cellpadding="8">
    <tr>
        <th>ID</th>
        <th>Sender (email)</th>
        <th>Receiver (email)</th>
        <th>Status</th>
        <th>Price</th>
    </tr>

    <c:forEach var="s" items="${shipments}">
        <tr>
            <td>${s.id}</td>
            <td>${s.sender}</td>
            <td>${s.receiver}</td>
            <td>${s.status}</td>
            <td>${s.price}</td>
        </tr>
    </c:forEach>
</table>

<p><a href="${pageContext.request.contextPath}/">Back to Home</a></p>
</body>
</html>
