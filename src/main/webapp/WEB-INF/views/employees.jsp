<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="org.informatics.entity.Employee" %>
<%@ page import="org.informatics.entity.enums.Role" %>
<%
    String userEmail = (String) session.getAttribute("userEmail");
    String firstName = (String) session.getAttribute("firstName");
    String lastName = (String) session.getAttribute("lastName");
    Role userRole = (Role) session.getAttribute("userRole");

    List<Employee> employees = (List<Employee>) request.getAttribute("employees");
    String success = request.getParameter("success");
    String error = (String) request.getAttribute("error");
%>
<!DOCTYPE html>
<html lang="bg">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Служители - ALVAS Logistics</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body>
<div class="container">
    <header>
        <div class="header-content">
            <a href="${pageContext.request.contextPath}/" class="logo">ALVAS Logistics</a>
            <nav>
                <ul>
                    <li><a href="${pageContext.request.contextPath}/">Начало</a></li>
                    <li><a href="${pageContext.request.contextPath}/employee-shipments">Пратки</a></li>
                    <li>
                        <div class="user-info">
                            👤 <%= firstName + " " + lastName %>
                            <span class="user-role">СЛУЖИТЕЛ</span>
                        </div>
                    </li>
                    <li><a href="${pageContext.request.contextPath}/logout">Изход</a></li>
                </ul>
            </nav>
        </div>
    </header>

    <main>
        <div class="page-header">
            <h1>👔 Управление на служители</h1>
            <p>Преглед на всички служители в системата</p>
        </div>

        <% if (success != null) { %>
        <div class="alert alert-success"><%= success %></div>
        <% } %>

        <% if (error != null) { %>
        <div class="alert alert-error"><%= error %></div>
        <% } %>

        <div class="card">
            <div class="table-container">
                <table>
                    <thead>
                    <tr>
                        <th>ID</th>
                        <th>Име</th>
                        <th>Имейл</th>
                        <th>Компания</th>
                        <th>Офис</th>
                        <th>Дата на регистрация</th>
                    </tr>
                    </thead>
                    <tbody>
                    <% if (employees != null && !employees.isEmpty()) { %>
                    <% for (Employee employee : employees) { %>
                    <tr>
                        <td><%= employee.getId() %></td>
                        <td><%= employee.getUser().getFirstName() + " " + employee.getUser().getLastName() %></td>
                        <td><%= employee.getUser().getEmail() %></td>
                        <td><%= employee.getCompany() != null ? employee.getCompany().getName() : "Без компания" %></td>
                        <td><%= employee.getOffice() != null ? employee.getOffice().getAddress() : "Без офис" %></td>
                        <td><%= employee.getUser().getCreatedAt() %></td>
                    </tr>
                    <% } %>
                    <% } else { %>
                    <tr>
                        <td colspan="6" class="text-center">Няма регистрирани служители</td>
                    </tr>
                    <% } %>
                    </tbody>
                </table>
            </div>
        </div>

        <a href="${pageContext.request.contextPath}/" class="btn btn-outline">← Обратно към началото</a>
    </main>

    <footer>
        <p>&copy; 2025 ALVAS Logistics. Всички права запазени.</p>
    </footer>
</div>
</body>
</html>