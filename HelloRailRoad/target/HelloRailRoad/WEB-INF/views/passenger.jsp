<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
    <title>Клиентам</title>
    <link href="${pageContext.request.contextPath}/resources/css/login.css" rel="stylesheet">

</head>
<body>
<c:import url="nav.jsp"/>
<c:if test="${message.length()>0}">
    <div id="error" class="msg">
        <span>Внимание!</span> ${message}
    </div>
</c:if>
<div id="find-train" class="form">
    <form action="findTrains" method="post">
        <table>
            <caption>Поиск поездов</caption>
            <tr>
                <td>Станция отправления:</td>
                <td><input type='text' list="stationList" name='departure' required value=''></td>
            </tr>
            <tr>
                <td>Станция назначения:</td>
                <td><input type='text' list="stationList" name='destination' required value=''><br></td>
            </tr>
            <tr>
                <td>В период с:</td>
                <td><input type="date" required name="after"/></td>

            </tr>
            <tr>
                <td>По:</td>
                <td><input type="date" required name="before"/></td>

            </tr>
        </table>
        <button type="submit">Найти поезда</button>
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
    </form>
</div>

</div>
<form class="form" id="schedule" action="getSchedule" method="POST">
    <label>Расписание</label><br/>
    <input type="text" name="station" required list="stationList" placeholder="Название станции"/>
    <input type="date" name="date" required placeholder="Дата">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
    <button type="submit">Посмотреть расписание</button>
</form>

<datalist id="stationList" >
    <c:forEach var="station" items="${stations}">
        <option value="${station.name}"/>
    </c:forEach>
</datalist>
</body>
</html>