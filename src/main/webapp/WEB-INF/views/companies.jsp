<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="org.informatics.entity.Company" %>
<%@ page import="org.informatics.entity.enums.Role" %>
<%
    String userEmail = (String) session.getAttribute("userEmail");
    String firstName = (String) session.getAttribute("firstName");
    String lastName = (String) session.getAttribute("lastName");
    Role userRole = (Role) session.getAttribute("userRole");

    List<Company> companies = (List<Company>) request.getAttribute("companies");
    String success = request.getParameter("success");
    String error = (String) request.getAttribute("error");
%>
<!DOCTYPE html>
<html lang="bg">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Компании - ALVAS Logistics</title>
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
            <h1>🏢 Управление на компании</h1>
            <button onclick="openCreateModal()" class="btn btn-primary">➕ Добави компания</button>
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
                        <th>Име на компанията</th>
                        <th>Действия</th>
                    </tr>
                    </thead>
                    <tbody>
                    <% if (companies != null && !companies.isEmpty()) { %>
                    <% for (Company company : companies) { %>
                    <tr>
                        <td><%= company.getId() %></td>
                        <td><%= company.getName() %></td>
                        <td>
                            <button onclick="openEditModal(<%= company.getId() %>, '<%= company.getName() %>')" class="btn-small btn-primary">✏️ Редактирай</button>
                            <button onclick="confirmDelete(<%= company.getId() %>, '<%= company.getName() %>')" class="btn-small btn-danger">🗑️ Изтрий</button>
                        </td>
                    </tr>
                    <% } %>
                    <% } else { %>
                    <tr>
                        <td colspan="3" class="text-center">Няма регистрирани компании</td>
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

<!-- Create/Edit Modal -->
<div id="companyModal" class="modal">
    <div class="modal-content">
        <span class="close" onclick="closeModal()">&times;</span>
        <h2 id="modalTitle">Добави компания</h2>
        <form method="post" action="${pageContext.request.contextPath}/companies">
            <input type="hidden" id="companyId" name="id">

            <label for="companyName">Име на компанията *</label>
            <input type="text" id="companyName" name="name" required>

            <div class="modal-actions">
                <button type="button" onclick="closeModal()" class="btn btn-outline">Откажи</button>
                <button type="submit" class="btn btn-primary">Запази</button>
            </div>
        </form>
    </div>
</div>

<script>
    function openCreateModal() {
        document.getElementById('modalTitle').textContent = 'Добави компания';
        document.getElementById('companyId').value = '';
        document.getElementById('companyName').value = '';
        document.getElementById('companyModal').style.display = 'block';
    }

    function openEditModal(id, name) {
        document.getElementById('modalTitle').textContent = 'Редактирай компания';
        document.getElementById('companyId').value = id;
        document.getElementById('companyName').value = name;
        document.getElementById('companyModal').style.display = 'block';
    }

    function closeModal() {
        document.getElementById('companyModal').style.display = 'none';
    }

    function confirmDelete(id, name) {
        if (confirm('Сигурни ли сте, че искате да изтриете компанията "' + name + '"?')) {
            window.location.href = '${pageContext.request.contextPath}/companies?action=delete&id=' + id;
        }
    }

    window.onclick = function(event) {
        const modal = document.getElementById('companyModal');
        if (event.target == modal) {
            closeModal();
        }
    }
</script>
</body>
</html>
