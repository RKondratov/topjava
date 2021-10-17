<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://example.com/functions" prefix="f" %>
<html lang="ru">

<head>
    <title>Edit Meal</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Edit Meal</h2>

<form method="POST" action='meals' id="frmCreateMeal">
    Date Time : &nbsp;
    <input type="datetime-local" name="dateTime"
           value="<c:out value="${meal.dateTime}" />"/> <br/><br/>

    Description :
    <input type="text" name="description"
           value="<c:out value="${meal.description}" />"/> <br/><br/>

    Calories : &nbsp;&nbsp;&nbsp;&nbsp;
    <input type="text" name="calories"
           value="<c:out value="${meal.calories}" />"/> <br/><br/>

    <input type="hidden" name="id" value="<c:out value="${meal.id}" />"/>

    <button type="submit" form="frmCreateMeal">Save</button>
    <button type="reset" form="frmCreateMeal">Cancel</button>
</form>
</body>
</html>