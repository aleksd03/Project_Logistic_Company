<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="org.informatics.entity.enums.Role" %>
<%
    String userEmail = (String) session.getAttribute("userEmail");
    String firstName = (String) session.getAttribute("firstName");
    String lastName = (String) session.getAttribute("lastName");
    Role userRole = (Role) session.getAttribute("userRole");

    Integer totalShipments = (Integer) request.getAttribute("totalShipments");
    Long activeDeliveries = (Long) request.getAttribute("activeDeliveries");
    Long completedDeliveries = (Long) request.getAttribute("completedDeliveries");
    Double totalRevenue = (Double) request.getAttribute("totalRevenue");
    Integer totalClients = (Integer) request.getAttribute("totalClients");
    Integer totalEmployees = (Integer) request.getAttribute("totalEmployees");
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

        <!-- STATISTICS CARDS -->
        <div class="stats-grid">
            <!-- TOTAL SHIPMENTS -->
            <a href="${pageContext.request.contextPath}/employee-shipments" class="stat-card blue clickable">
                <div class="stat-card-icon">📦</div>
                <div class="stat-card-value blue"><%= totalShipments != null ? totalShipments : 0 %></div>
                <div class="stat-card-label">Общо пратки</div>
            </a>

            <!-- ACTIVE DELIVERIES -->
            <a href="${pageContext.request.contextPath}/undelivered-shipments" class="stat-card orange clickable">
                <div class="stat-card-icon">🚚</div>
                <div class="stat-card-value orange"><%= activeDeliveries != null ? activeDeliveries : 0 %></div>
                <div class="stat-card-label">Активни доставки</div>
            </a>

            <!-- COMPLETED DELIVERIES -->
            <div class="stat-card green">
                <div class="stat-card-icon">✅</div>
                <div class="stat-card-value green"><%= completedDeliveries != null ? completedDeliveries : 0 %></div>
                <div class="stat-card-label">Завършени доставки</div>
            </div>

            <!-- TOTAL REVENUE -->
            <a href="${pageContext.request.contextPath}/revenue-report" class="stat-card green clickable">
                <div class="stat-card-icon">💰</div>
                <div class="stat-card-value green"><%= totalRevenue != null ? String.format("%.2f", totalRevenue) : "0.00" %>€</div>
                <div class="stat-card-label">Общ приход</div>
            </a>
        </div>

        <!-- AVAILABLE REPORTS -->
        <div class="card">
            <div style="padding: 1rem 1.25rem; border-bottom: 2px solid #e0e0e0; background: #f8f9fa;">
                <h2 style="margin: 0; font-size: 1.1rem; color: #333;">Налични справки</h2>
            </div>

            <div class="report-list">
                <!-- ALL SHIPMENTS -->
                <div class="report-item">
                    <div class="report-item-content">
                        <div class="report-item-icon">📋</div>
                        <div class="report-item-text">
                            <h3>Справка за всички пратки</h3>
                            <p>Детайлен списък с всички регистрирани пратки</p>
                        </div>
                    </div>
                    <a href="${pageContext.request.contextPath}/employee-shipments" class="btn btn-primary">Виж</a>
                </div>

                <!-- ALL CLIENTS -->
                <div class="report-item">
                    <div class="report-item-content">
                        <div class="report-item-icon">👥</div>
                        <div class="report-item-text">
                            <h3>Справка за клиенти</h3>
                            <p>Списък на всички регистрирани клиенти</p>
                        </div>
                    </div>
                    <a href="${pageContext.request.contextPath}/clients" class="btn btn-primary">Виж</a>
                </div>

                <!-- ALL EMPLOYEES -->
                <div class="report-item">
                    <div class="report-item-content">
                        <div class="report-item-icon">👔</div>
                        <div class="report-item-text">
                            <h3>Справка за служители</h3>
                            <p>Списък на всички служители в системата</p>
                        </div>
                    </div>
                    <a href="${pageContext.request.contextPath}/employees" class="btn btn-primary">Виж</a>
                </div>

                <!-- UNDELIVERED SHIPMENTS -->
                <div class="report-item">
                    <div class="report-item-content">
                        <div class="report-item-icon">⚠️</div>
                        <div class="report-item-text">
                            <h3>Неполучени пратки</h3>
                            <p>Пратки, които все още чакат доставка</p>
                        </div>
                    </div>
                    <a href="${pageContext.request.contextPath}/undelivered-shipments" class="btn btn-primary">Виж</a>
                </div>

                <!-- MONTHLY STATISTICS -->
                <div class="report-item">
                    <div class="report-item-content">
                        <div class="report-item-icon">📊</div>
                        <div class="report-item-text">
                            <h3>Месечна статистика</h3>
                            <p>Статистика за текущия месец</p>
                        </div>
                    </div>
                    <a href="${pageContext.request.contextPath}/monthly-stats" class="btn btn-primary">Виж</a>
                </div>

                <!-- REVENUE REPORT -->
                <div class="report-item">
                    <div class="report-item-content">
                        <div class="report-item-icon">💵</div>
                        <div class="report-item-text">
                            <h3>Финансов отчет</h3>
                            <p>Приходи и разходи за избран период</p>
                        </div>
                    </div>
                    <a href="${pageContext.request.contextPath}/revenue-report" class="btn btn-primary">Виж</a>
                </div>
            </div>
        </div>

        <!-- QUICK STATS -->
        <div class="quick-stats-grid">
            <div class="quick-stat-card">
                <h3 class="quick-stat-header">
                    <span>👥</span> Потребители в системата
                </h3>
                <div class="quick-stat-content">
                    <div class="quick-stat-item">
                        <div class="quick-stat-value blue"><%= totalClients != null ? totalClients : 0 %></div>
                        <div class="quick-stat-label">Клиенти</div>
                    </div>
                    <div class="quick-stat-item">
                        <div class="quick-stat-value purple"><%= totalEmployees != null ? totalEmployees : 0 %></div>
                        <div class="quick-stat-label">Служители</div>
                    </div>
                </div>
            </div>

            <div class="quick-stat-card">
                <h3 class="quick-stat-header">
                    <span>📈</span> Статус на доставките
                </h3>
                <div class="quick-stat-content">
                    <div class="quick-stat-item">
                        <div class="quick-stat-value orange"><%= activeDeliveries != null ? activeDeliveries : 0 %></div>
                        <div class="quick-stat-label">В процес</div>
                    </div>
                    <div class="quick-stat-item">
                        <div class="quick-stat-value green"><%= completedDeliveries != null ? completedDeliveries : 0 %></div>
                        <div class="quick-stat-label">Доставени</div>
                    </div>
                </div>
            </div>
        </div>

        <div style="margin-top: 1.5rem;">
            <a href="${pageContext.request.contextPath}/" class="btn btn-outline">← Обратно към началото</a>
        </div>
    </main>

    <footer>
        <p>&copy; 2025 ALVAS Logistics. Всички права запазени.</p>
    </footer>
</div>
</body>
</html>