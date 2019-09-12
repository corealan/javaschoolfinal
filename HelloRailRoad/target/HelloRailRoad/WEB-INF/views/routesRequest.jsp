<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
    <title>Выбор маршрута</title>
    <link href="${pageContext.request.contextPath}/resources/css/login.css" rel="stylesheet">
</head>
<body>
<c:import url="employeeNav.jsp"/>
<c:if test="${message.length()>0}">
    <div id="error" class="msg">
        <span>Внимание!</span> ${message}
    </div>
</c:if>
<div class="form" id="route-request">
    <form action="getRoutes" method="POST">
        <table>
            <caption>Введите станции отправления и назначения:</caption>
            <tr>
                <td>Станция отправления:</td>
                <td><input type='text' name='departure' list="stationList" value=''></td>
            </tr>
            <tr>
                <td>Станция назначения:</td>
                <td><input type='text' name='destination' list="stationList" value=''><br></td>
            </tr>
        </table>
        <button type="submit">Показать возможные маршруты</button>
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
    </form>
</div>

<datalist id="stationList" >
    <c:forEach var="station" items="${stations}">
        <option value="${station.name}"/>
    </c:forEach>
</datalist>

</body>
</html>
