<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="org.informatics.entity.enums.Role" %>
<%
    String userEmail = (String) session.getAttribute("userEmail");
    String firstName = (String) session.getAttribute("firstName");
    String lastName = (String) session.getAttribute("lastName");
    Role userRole = (Role) session.getAttribute("userRole");

    Integer totalShipments = (Integer) request.getAttribute("totalShipments");
    String error = (String) request.getAttribute("error");
%>
<!DOCTYPE html>
<html lang="bg">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Справки - ALVAS Logistics</title>
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
            <h1>📊 Справки и статистика</h1>
            <p>Преглед на различни отчети и статистики за системата</p>
        </div>

        <% if (error != null) { %>
        <div class="alert alert-error"><%= error %></div>
        <% } %>

        <div class="stats-grid">
            <div class="stat-card">
                <div class="stat-icon">📦</div>
                <div class="stat-info">
                    <div class="stat-value"><%= totalShipments != null ? totalShipments : 0 %></div>
                    <div class="stat-label">Общо пратки</div>
                </div>
            </div>

            <div class="stat-card">
                <div class="stat-icon">🚚</div>
                <div class="stat-info">
                    <div class="stat-value">В разработка</div>
                    <div class="stat-label">Активни доставки</div>
                </div>
            </div>

            <div class="stat-card">
                <div class="stat-icon">✅</div>
                <div class="stat-info">
                    <div class="stat-value">В разработка</div>
                    <div class="stat-label">Завършени доставки</div>
                </div>
            </div>

            <div class="stat-card">
                <div class="stat-icon">💰</div>
                <div class="stat-info">
                    <div class="stat-value">В разработка</div>
                    <div class="stat-label">Общ приход</div>
                </div>
            </div>
        </div>

        <div class="card" style="margin-top: 2rem;">
            <h3>Налични справки</h3>
            <div class="reports-list">
                <div class="report-item">
                    <div>
                        <strong>📋 Справка за всички пратки</strong>
                        <p>Детайлен списък с всички регистрирани пратки</p>
                    </div>
                    <button class="btn btn-outline" disabled>Скоро</button>
                </div>

                <div class="report-item">
                    <div>
                        <strong>👥 Справка за клиенти</strong>
                        <p>Списък на всички регистрирани клиенти</p>
                    </div>
                    <a href="${pageContext.request.contextPath}/clients" class="btn btn-primary">Виж</a>
                </div>

                <div class="report-item">
                    <div>
                        <strong>📊 Месечна статистика</strong>
                        <p>Статистика за текущия месец</p>
                    </div>
                    <button class="btn btn-outline" disabled>Скоро</button>
                </div>

                <div class="report-item">
                    <div>
                        <strong>💵 Финансов отчет</strong>
                        <p>Приходи и разходи за избран период</p>
                    </div>
                    <button class="btn btn-outline" disabled>Скоро</button>
                </div>
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