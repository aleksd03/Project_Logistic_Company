<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="bg">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>404 - Страницата не е намерена</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body>
<div class="container error-container">
    <main>
        <h1 class="error-code">404</h1>
        <h2 class="error-title">Страницата не е намерена</h2>

        <div class="error-message">
            ✗ Съжаляваме, но търсената от вас страница не съществува!
        </div>

        <div class="error-actions">
            <a href="${pageContext.request.contextPath}/" class="btn-back">
                Към началото
            </a>
        </div>

        <div class="error-help">
            <h3>Какво можете да направите:</h3>
            <ul>
                <li>Проверете дали URL адресът е написан правилно</li>
                <li>Използвайте навигацията, за да намерите търсеното</li>
                <li>Върнете се към началната страница</li>
            </ul>
        </div>
    </main>

    <footer>
        <p>&copy; 2025 ALVAS Logistics. Всички права запазени.</p>
    </footer>
</div>
</body>
</html>