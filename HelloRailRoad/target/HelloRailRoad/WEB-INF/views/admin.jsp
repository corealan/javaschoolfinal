<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>

<html>
<head>
    <title>Сотрудникам</title>
    <link href="${pageContext.request.contextPath}/resources/css/login.css" rel="stylesheet">

</head>
<body>
<c:import url="employeeNav.jsp"/>
<c:if test="${newTrainMessage.length()>0}">
    <div id="success" class="msg">
        <span>Внимание!</span> ${newTrainMessage}
    </div>
</c:if>

<c:if test="${message.length()>0}">
    <div id="error" class="msg">
        <span>Внимание!</span> ${message}
    </div>
</c:if>
<div class="form" id = "stations-filter">
    <form action="/HelloRailRoad/admin/findTrains" method="post">
        <label>Укажите станцию для поиска поездов:</label> <br/>
        Станция :<input type="text" list="stationList" name="station"> <br/>
        <button type="submit">Найти поезда</button>
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
    </form>
</div>

<div>
    <form class="form" id="schedule" action="/HelloRailRoad/admin/getSchedule" method="POST">
        <label>Укажите станцию для изменения расписания:</label><br/>
        <input type="text" name="station" list="stationList" placeholder="Название станции"/>
        <input type="date" name="date" placeholder="Дата">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
        <button type="submit">Посмотреть расписание</button>
    </form>
</div>


<datalist id="stationList" >
    <c:forEach var="station" items="${stations}">
        <option value="${station.name}"/>
    </c:forEach>
</datalist>

</body>
</html>
