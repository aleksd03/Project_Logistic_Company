<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="org.informatics.entity.Office" %>
<%@ page import="org.informatics.entity.Company" %>
<%@ page import="org.informatics.entity.enums.Role" %>

<%
    // Logged-in user session data
    String userEmail = (String) session.getAttribute("userEmail");
    String firstName = (String) session.getAttribute("firstName");
    String lastName = (String) session.getAttribute("lastName");
    Role userRole = (Role) session.getAttribute("userRole");

    // Data provided by OfficesServlet
    List<Office> offices = (List<Office>) request.getAttribute("offices");
    List<Company> companies = (List<Company>) request.getAttribute("companies");

    // Optional feedback messages
    String success = request.getParameter("success");
    String error = request.getParameter("error");
%>

<!DOCTYPE html>
<html lang="bg">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Офиси - ALVAS Logistics</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>

<body>
<div class="container">

    <!-- HEADER / NAVIGATION -->
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

        <!-- PAGE HEADER + CREATE BUTTON -->
        <div class="page-header">
            <h1>🏛️ Управление на офиси</h1>
            <button onclick="openCreateModal()" class="btn btn-primary">➕ Добави офис</button>
        </div>

        <!-- SUCCESS MESSAGE -->
        <% if (success != null) { %>
        <div class="alert alert-success"><%= success %></div>
        <% } %>

        <!-- ERROR MESSAGE -->
        <% if (error != null) { %>
        <div class="alert alert-error"><%= error %></div>
        <% } %>

        <!-- OFFICES TABLE -->
        <div class="card">
            <div class="table-container">
                <table>
                    <thead>
                    <tr>
                        <th>ID</th>
                        <th>Адрес</th>
                        <th>Компания</th>
                        <th>Действия</th>
                    </tr>
                    </thead>
                    <tbody>

                    <%-- Show offices if available --%>
                    <% if (offices != null && !offices.isEmpty()) { %>
                    <% for (Office o : offices) { %>
                    <tr>
                        <td><%= o.getId() %></td>
                        <td><%= o.getAddress() %></td>
                        <td><%= o.getCompany() != null ? o.getCompany().getName() : "N/A" %></td>
                        <td>
                            <div class="action-buttons">

                                <!-- EDIT OFFICE -->
                                <button onclick="openEditModal(<%= o.getId() %>, '<%= o.getAddress().replace("'", "\\'") %>', <%= o.getCompany() != null ? o.getCompany().getId() : "null" %>)"
                                        class="btn btn-primary">
                                    🖊️ Редактирай
                                </button>

                                <!-- DELETE OFFICE -->
                                <form action="${pageContext.request.contextPath}/offices"
                                      method="get"
                                      onsubmit="return confirm('Сигурни ли сте, че искате да изтриете офиса на адрес: <%= o.getAddress().replace("'", "\\'") %>?');">
                                    <input type="hidden" name="action" value="delete">
                                    <input type="hidden" name="id" value="<%= o.getId() %>">
                                    <button type="submit" class="btn btn-danger">
                                        🗑️ Изтрий
                                    </button>
                                </form>
                            </div>
                        </td>
                    </tr>
                    <% } %>
                    <% } else { %>

                    <!-- NO OFFICES MESSAGE -->
                    <tr>
                        <td colspan="4" class="text-center">Няма добавени офиси.</td>
                    </tr>

                    <% } %>
                    </tbody>
                </table>
            </div>
        </div>

        <!-- BACK BUTTON -->
        <a href="${pageContext.request.contextPath}/" class="btn btn-outline">← Обратно към началото</a>
    </main>

    <!-- FOOTER -->
    <footer>
        <p>&copy; 2025 ALVAS Logistics. Всички права запазени.</p>
    </footer>
</div>

<!-- CREATE / EDIT OFFICE MODAL -->
<div id="officeModal" class="modal">
    <div class="modal-content">
        <span class="close" onclick="closeModal()">&times;</span>
        <h2 id="modalTitle">Добави офис</h2>

        <form method="post" action="${pageContext.request.contextPath}/offices">
            <input type="hidden" id="officeId" name="id">

            <!-- Office address -->
            <label for="officeAddress">Адрес *</label>
            <input type="text" id="officeAddress" name="address" required placeholder="гр. София, ул. Витоша 15">

            <!-- Company selection -->
            <label for="officeCompany">Компания *</label>
            <select id="officeCompany" name="companyId" required>
                <option value="">Избери компания</option>
                <% if (companies != null) {
                    for (Company company : companies) { %>
                <option value="<%= company.getId() %>"><%= company.getName() %></option>
                <% }
                } %>
            </select>

            <div class="modal-actions">
                <button type="button" onclick="closeModal()" class="btn btn-outline">Откажи</button>
                <button type="submit" class="btn btn-primary">Запази</button>
            </div>
        </form>
    </div>
</div>

<script>
    // Open modal for creating a new office
    function openCreateModal() {
        document.getElementById('modalTitle').textContent = 'Добави офис';
        document.getElementById('officeId').value = '';
        document.getElementById('officeAddress').value = '';
        document.getElementById('officeCompany').value = '';
        document.getElementById('officeModal').style.display = 'block';
    }

    // Open modal for editing an existing office
    function openEditModal(id, address, companyId) {
        document.getElementById('modalTitle').textContent = 'Редактирай офис';
        document.getElementById('officeId').value = id;
        document.getElementById('officeAddress').value = address;
        document.getElementById('officeCompany').value = companyId || '';
        document.getElementById('officeModal').style.display = 'block';
    }

    // Close modal window
    function closeModal() {
        document.getElementById('officeModal').style.display = 'none';
    }

    // Close modal when clicking outside it
    window.onclick = function(event) {
        const modal = document.getElementById('officeModal');
        if (event.target == modal) {
            closeModal();
        }
    }
</script>

</body>
</html>
