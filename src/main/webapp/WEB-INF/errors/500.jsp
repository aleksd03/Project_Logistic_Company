<%@ page contentType="text/html; charset=UTF-8" isErrorPage="true" %>
<!DOCTYPE html>
<html lang="bg">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>500 - Вътрешна грешка</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body>
<div class="container error-container">
  <main>
    <h1 class="error-code">500</h1>
    <h2 class="error-title">Вътрешна сървърна грешка</h2>

    <div class="error-message">
      ✗ Нещо се обърка от наша страна. Извиняваме се за неудобството!
    </div>

    <div class="error-actions">
      <a href="${pageContext.request.contextPath}/" class="btn-back">
        Към началото
      </a>
    </div>

    <div class="error-help">
      <h3>Какво можете да направите:</h3>
      <ul>
        <li>Опитайте да презаредите страницата</li>
        <li>Изчакайте няколко минути и опитайте отново</li>
        <li>Свържете се с технически екип, ако проблемът продължава</li>
      </ul>
    </div>
  </main>

  <footer>
    <p>&copy; 2025 ALVAS Logistics. Всички права запазени.</p>
  </footer>
</div>
</body>
</html>