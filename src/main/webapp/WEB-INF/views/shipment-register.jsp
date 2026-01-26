<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="org.informatics.entity.Client" %>
<%@ page import="org.informatics.entity.Office" %>
<%@ page import="org.informatics.entity.enums.Role" %>
<%
  String userEmail = (String) session.getAttribute("userEmail");
  String firstName = (String) session.getAttribute("firstName");
  String lastName = (String) session.getAttribute("lastName");
  Role userRole = (Role) session.getAttribute("userRole");

  List<Client> clients = (List<Client>) request.getAttribute("clients");
  List<Office> offices = (List<Office>) request.getAttribute("offices");
  String error = (String) request.getAttribute("error");
%>
<!DOCTYPE html>
<html lang="bg">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Регистриране на пратка - ALVAS Logistics</title>
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
      <h1>➕ Регистриране на нова пратка</h1>
      <p>Попълнете формата за да регистрирате нова пратка в системата</p>
    </div>

    <% if (error != null) { %>
    <div class="alert alert-error"><%= error %></div>
    <% } %>

    <div class="card">
      <form method="post" action="${pageContext.request.contextPath}/shipment-register">
        <div class="form-grid">
          <div>
            <label for="senderId">Подател *</label>
            <select id="senderId" name="senderId" required>
              <option value="">Избери подател</option>
              <% if (clients != null) {
                for (Client client : clients) { %>
              <option value="<%= client.getId() %>">
                <%= client.getUser().getFirstName() + " " + client.getUser().getLastName() %>
                (<%= client.getUser().getEmail() %>)
              </option>
              <% }
              } %>
            </select>
          </div>

          <div>
            <label for="receiverId">Получател *</label>
            <select id="receiverId" name="receiverId" required>
              <option value="">Избери получател</option>
              <% if (clients != null) {
                for (Client client : clients) { %>
              <option value="<%= client.getId() %>">
                <%= client.getUser().getFirstName() + " " + client.getUser().getLastName() %>
                (<%= client.getUser().getEmail() %>)
              </option>
              <% }
              } %>
            </select>
          </div>
        </div>

        <label for="weight">Тегло (кг) *</label>
        <input type="number" id="weight" name="weight" step="0.01" min="0.01" required placeholder="0.00">

        <label for="deliveryType">Тип доставка *</label>
        <select id="deliveryType" name="deliveryType" required>
          <option value="">Избери тип доставка</option>
          <option value="office">Доставка до офис</option>
          <option value="address">Доставка до адрес</option>
        </select>

        <div id="officeField" style="display: none;">
          <label for="officeId">Офис за доставка *</label>
          <select id="officeId" name="officeId">
            <option value="">Избери офис</option>
            <% if (offices != null) {
              for (Office office : offices) { %>
            <option value="<%= office.getId() %>">
              <%= office.getAddress() %>
              <% if (office.getCompany() != null) { %>
              (<%= office.getCompany().getName() %>)
              <% } %>
            </option>
            <% }
            } %>
          </select>
        </div>

        <div id="addressField" style="display: none;">
          <label for="deliveryAddress">Адрес за доставка *</label>
          <input type="text" id="deliveryAddress" name="deliveryAddress"
                 placeholder="гр. София, ул. Витоша 15">
        </div>

        <div class="info-box">
          <strong>💡 Автоматично изчисляване на цена:</strong>
          <ul>
            <li>Доставка до офис: 1.50€ на кг</li>
            <li>Доставка до адрес: 2.50€ на кг</li>
            <li>Пратки над 10кг: +5€</li>
            <li>Пратки над 20кг: още +10€</li>
          </ul>
        </div>

        <div class="form-actions">
          <a href="${pageContext.request.contextPath}/employee-shipments" class="btn btn-outline">Откажи</a>
          <button type="submit" class="btn btn-success">Регистрирай пратка</button>
        </div>
      </form>
    </div>
  </main>

  <footer>
    <p>&copy; 2025 ALVAS Logistics. Всички права запазени.</p>
  </footer>
</div>

<script>
  // Валидация - подател и получател да са различни
  document.querySelector('form').addEventListener('submit', function(e) {
    const senderId = document.getElementById('senderId').value;
    const receiverId = document.getElementById('receiverId').value;

    if (senderId === receiverId) {
      e.preventDefault();
      alert('Подателят и получателят не могат да бъдат едно и също лице!');
      return;
    }

    const deliveryType = document.getElementById('deliveryType').value;
    if (deliveryType === 'office') {
      const officeId = document.getElementById('officeId').value;
      if (!officeId) {
        e.preventDefault();
        alert('Моля изберете офис за доставка!');
        return;
      }
    } else if (deliveryType === 'address') {
      const address = document.getElementById('deliveryAddress').value;
      if (!address || address.trim() === '') {
        e.preventDefault();
        alert('Моля въведете адрес за доставка!');
        return;
      }
    }
  });

  // Show/hide fields based on delivery type
  document.getElementById('deliveryType').addEventListener('change', function() {
    const officeField = document.getElementById('officeField');
    const addressField = document.getElementById('addressField');
    const officeSelect = document.getElementById('officeId');
    const addressInput = document.getElementById('deliveryAddress');

    if (this.value === 'office') {
      officeField.style.display = 'block';
      addressField.style.display = 'none';
      officeSelect.required = true;
      addressInput.required = false;
      addressInput.value = '';
    } else if (this.value === 'address') {
      officeField.style.display = 'none';
      addressField.style.display = 'block';
      officeSelect.required = false;
      officeSelect.value = '';
      addressInput.required = true;
    } else {
      officeField.style.display = 'none';
      addressField.style.display = 'none';
      officeSelect.required = false;
      addressInput.required = false;
    }
  });
</script>
</body>
</html>