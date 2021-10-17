<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://example.com/functions" prefix="f" %>
<html lang="ru">
<head>
    <title>Meals</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Meals</h2>
<p><a href="meals?action=create">Add Meal</a></p>
<table border="1">
    <tr>
        <th>Date</th>
        <th>Description</th>
        <th>calories</th>
        <th></th>
        <th></th>
    </tr>
    <c:forEach var="num" items="${mealsTo}">
    <h4>
        <tr>
            <c:choose>
                <c:when test="${num.excess}">
                    <td><span style="color: red; ">${f:formatLocalDateTime(num.dateTime)}</span>
                    </td>
                    <td><span style="color: red; ">${num.description}</span></td>
                    <td><span style="color: red; ">${num.calories}</span></td>
                </c:when>
                <c:otherwise>
                    <td><span
                            style="color: darkgreen; ">${f:formatLocalDateTime(num.dateTime)}</span>
                    </td>
                    <td><span style="color: darkgreen; ">${num.description}</span></td>
                    <td><span style="color: darkgreen; ">${num.calories}</span></td>
                </c:otherwise>
            </c:choose>
            <td><a href="meals?action=update&mealId=${num.id}">Update</a></td>
            <td><a href="meals?action=delete&mealId=${num.id}">Delete</a></td>
        </tr>
    </h4>
    </c:forEach>
</body>
</html>