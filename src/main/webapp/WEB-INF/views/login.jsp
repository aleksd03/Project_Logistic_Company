<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="bg">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Вход - ALVAS Logistics</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>

<body>
<div class="container">

    <!-- HEADER / NAVIGATION -->
    <header>
        <div class="header-content">
            <a href="${pageContext.request.contextPath}/" class="logo">
                ALVAS Logistics
            </a>

            <nav>
                <ul>
                    <li><a href="${pageContext.request.contextPath}/">Начало</a></li>
                    <li><a href="${pageContext.request.contextPath}/login">Вход</a></li>
                    <li><a href="${pageContext.request.contextPath}/register">Регистрация</a></li>
                </ul>
            </nav>
        </div>
    </header>

    <!-- MAIN CONTENT -->
    <main class="fade-in">

        <!-- LOGIN FORM CONTAINER -->
        <div class="auth-form-container">
            <h2>Вход в системата</h2>
            <p>Добре дошли обратно! Моля влезте във вашия акаунт.</p>

            <%-- Display login error message if authentication fails --%>
            <% 
                String error = (String) request.getAttribute("error");
                if (error != null) { 
            %>
            <div class="alert alert-error">
                ⚠️ <%= error %>
            </div>
            <% } %>

            <!-- LOGIN FORM -->
            <form method="post" action="${pageContext.request.contextPath}/login">

                <!-- EMAIL INPUT -->
                <label for="email">
                    Email адрес
                    <input
                            type="email"
                            id="email"
                            name="email"
                            placeholder="vash@email.com"
                            required
                            autofocus>
                </label>

                <!-- PASSWORD INPUT -->
                <label for="password">
                    Парола
                    <input
                            type="password"
                            id="password"
                            name="password"
                            placeholder="Въведете паролата си"
                            required>
                </label>

                <!-- SUBMIT BUTTON -->
                <button type="submit" class="btn-primary">
                    Влез в системата
                </button>
            </form>

            <!-- REGISTRATION LINK -->
            <div class="text-center">
                <p>Нямате акаунт?</p>
                <a href="${pageContext.request.contextPath}/register" class="btn-outline">
                    Регистрирайте се
                </a>
            </div>
        </div>
    </main>

    <footer>
        <p>&copy; 2025 ALVAS Logistics. Всички права запазени.</p>
    </footer>

</div>
</body>
</html>

