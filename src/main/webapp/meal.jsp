<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%--
  Created by IntelliJ IDEA.
  User: Max
  Date: 08.02.2020
  Time: 14:08
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Meal</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<form method="POST" action='meals' name="frmAddMeal">
    <input type="hidden" name="mealId" value="${meal.id}"/>
    Дата/Время : <input type="datetime-local" name="dateTime"
                        value="${meal.dateTime}"/> <br/>
    Описание : <input type="text" name="description"
                      value="${meal.description}"/> <br/>
    Калории : <input type="text" name="calories"
                     value="${meal.calories}"/> <br/>
    <input type="submit" value="Submit"/>
</form>
</body>
</html>
