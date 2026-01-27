<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="org.informatics.entity.Shipment" %>
<%@ page import="org.informatics.entity.enums.Role" %>
<%@ page import="org.informatics.entity.enums.ShipmentStatus" %>
<%
  String userEmail = (String) session.getAttribute("userEmail");
  String firstName = (String) session.getAttribute("firstName");
  String lastName = (String) session.getAttribute("lastName");
  Role userRole = (Role) session.getAttribute("userRole");

  String currentMonth = (String) request.getAttribute("currentMonth");
  Integer currentYear = (Integer) request.getAttribute("currentYear");
  Integer totalShipments = (Integer) request.getAttribute("totalShipments");
  Long sentShipments = (Long) request.getAttribute("sentShipments");
  Long receivedShipments = (Long) request.getAttribute("receivedShipments");
  Double totalRevenue = (Double) request.getAttribute("totalRevenue");
  Double averagePrice = (Double) request.getAttribute("averagePrice");
  Integer totalClients = (Integer) request.getAttribute("totalClients");
  List<Shipment> shipments = (List<Shipment>) request.getAttribute("shipments");

  String monthBG = currentMonth;
  switch(currentMonth) {
    case "JANUARY": monthBG = "Януари"; break;
    case "FEBRUARY": monthBG = "Февруари"; break;
    case "MARCH": monthBG = "Март"; break;
    case "APRIL": monthBG = "Април"; break;
    case "MAY": monthBG = "Май"; break;
    case "JUNE": monthBG = "Юни"; break;
    case "JULY": monthBG = "Юли"; break;
    case "AUGUST": monthBG = "Август"; break;
    case "SEPTEMBER": monthBG = "Септември"; break;
    case "OCTOBER": monthBG = "Октомври"; break;
    case "NOVEMBER": monthBG = "Ноември"; break;
    case "DECEMBER": monthBG = "Декември"; break;
  }
%>
<!DOCTYPE html>
<html lang="bg">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Месечна статистика - ALVAS Logistics</title>
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
      <h1>📊 Месечна статистика</h1>
      <p>Преглед на статистиката за <%= monthBG %> <%= currentYear %></p>
    </div>

    <!-- MAIN STATS -->
    <div class="stats-grid" style="grid-template-columns: repeat(3, 1fr); margin-bottom: 2rem;">
      <div class="stat-card blue">
        <div class="stat-card-icon">📦</div>
        <div class="stat-card-value blue"><%= totalShipments != null ? totalShipments : 0 %></div>
        <div class="stat-card-label">Общо пратки този месец</div>
      </div>

      <div class="stat-card orange">
        <div class="stat-card-icon">🚚</div>
        <div class="stat-card-value orange"><%= sentShipments != null ? sentShipments : 0 %></div>
        <div class="stat-card-label">В процес на доставка</div>
      </div>

      <div class="stat-card green">
        <div class="stat-card-icon">✅</div>
        <div class="stat-card-value green"><%= receivedShipments != null ? receivedShipments : 0 %></div>
        <div class="stat-card-label">Доставени</div>
      </div>
    </div>

    <!-- FINANCIAL STATS -->
    <div class="quick-stats-grid" style="margin-bottom: 2rem;">
      <div class="quick-stat-card">
        <h3 class="quick-stat-header">
          <span>💰</span> Финансови показатели
        </h3>
        <div class="quick-stat-content">
          <div class="quick-stat-item">
            <div class="quick-stat-value green"><%= totalRevenue != null ? String.format("%.2f", totalRevenue) : "0.00" %>€</div>
            <div class="quick-stat-label">Общи приходи</div>
          </div>
          <div class="quick-stat-item">
            <div class="quick-stat-value blue"><%= averagePrice != null ? String.format("%.2f", averagePrice) : "0.00" %>€</div>
            <div class="quick-stat-label">Средна цена</div>
          </div>
        </div>
      </div>

      <div class="quick-stat-card">
        <h3 class="quick-stat-header">
          <span>📈</span> Ефективност
        </h3>
        <div class="quick-stat-content">
          <div class="quick-stat-item">
            <div class="quick-stat-value purple">
              <% if (totalShipments != null && totalShipments > 0 && receivedShipments != null) { %>
              <%= String.format("%.1f", (receivedShipments * 100.0 / totalShipments)) %>%
              <% } else { %>
              0.0%
              <% } %>
            </div>
            <div class="quick-stat-label">Процент доставени</div>
          </div>
          <div class="quick-stat-item">
            <div class="quick-stat-value blue"><%= totalClients != null ? totalClients : 0 %></div>
            <div class="quick-stat-label">Активни клиенти</div>
          </div>
        </div>
      </div>
    </div>

    <!-- SHIPMENTS TABLE -->
    <% if (shipments != null && !shipments.isEmpty()) { %>
    <div class="card">
      <div style="padding: 0.75rem 1rem; border-bottom: 2px solid #e0e0e0; background: #f8f9fa;">
        <strong style="color: #333;">📋 Пратки за <%= monthBG %> <%= currentYear %></strong>
      </div>

      <div class="table-container">
        <table>
          <thead>
          <tr>
            <th>ID</th>
            <th>Подател</th>
            <th>Получател</th>
            <th>Тегло</th>
            <th>Цена</th>
            <th>Статус</th>
            <th>Дата</th>
          </tr>
          </thead>
          <tbody>
          <% for (Shipment s : shipments) { %>
          <tr>
            <td><%= s.getId() %></td>
            <td>
              <%= s.getSender() != null && s.getSender().getUser() != null
                      ? s.getSender().getUser().getFirstName() + " " + s.getSender().getUser().getLastName()
                      : "N/A" %>
            </td>
            <td>
              <%= s.getReceiver() != null && s.getReceiver().getUser() != null
                      ? s.getReceiver().getUser().getFirstName() + " " + s.getReceiver().getUser().getLastName()
                      : "N/A" %>
            </td>
            <td><%= String.format("%.2f", s.getWeight()) %> kg</td>
            <td style="font-weight: bold; color: #28a745;"><%= String.format("%.2f", s.getPrice()) %>€</td>
            <td>
              <% if (s.getStatus() == ShipmentStatus.SENT) { %>
              <span class="status status-sent">📦 Изпратена</span>
              <% } else if (s.getStatus() == ShipmentStatus.RECEIVED) { %>
              <span class="status status-received">✅ Получена</span>
              <% } %>
            </td>
            <td><%= s.getRegistrationDate().toString().substring(0, 16).replace("T", " ") %></td>
          </tr>
          <% } %>
          </tbody>
        </table>
      </div>
    </div>
    <% } else { %>
    <div class="card">
      <div style="padding: 2rem; text-align: center; color: #666;">
        <p style="font-size: 1.1rem; margin-bottom: 0.5rem;">📭 Няма пратки за този месец</p>
      </div>
    </div>
    <% } %>

    <div style="margin-top: 1.5rem;">
      <a href="${pageContext.request.contextPath}/reports" class="btn btn-primary">← Обратно към справки</a>
      <a href="${pageContext.request.contextPath}/" class="btn btn-outline">← Обратно към началото</a>
    </div>
  </main>

  <footer>
    <p>&copy; 2025 ALVAS Logistics. Всички права запазени.</p>
  </footer>
</div>
</body>
</html>
