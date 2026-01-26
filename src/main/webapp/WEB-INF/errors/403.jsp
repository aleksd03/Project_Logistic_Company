<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="bg">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>403 - Достъп отказан</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body>
<div class="container error-container">
  <main>
    <h1 class="error-code">403</h1>
    <h2 class="error-title">Достъп отказан</h2>

    <div class="error-message">
      ✗ Нямате права за достъп до тази страница!
    </div>

    <div class="error-actions">
      <a href="${pageContext.request.contextPath}/" class="btn-back">
        Към началото
      </a>
    </div>

    <div class="error-help">
      <h3>Какво можете да направите:</h3>
      <ul>
        <li>Проверете дали сте влезли в системата</li>
        <li>Уверете се, че имате необходимите права за достъп</li>
        <li>Свържете се с администратор, ако смятате че това е грешка</li>
      </ul>
    </div>
  </main>

  <footer>
    <p>&copy; 2025 ALVAS Logistics. Всички права запазени.</p>
  </footer>
</div>
</body>
</html>