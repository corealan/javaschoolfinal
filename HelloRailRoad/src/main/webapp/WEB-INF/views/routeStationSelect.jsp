<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>

<html>
<head>
    <link href="${pageContext.request.contextPath}/resources/css/login.css" rel="stylesheet">
    <title>RouteStationSelect</title>
</head>
<body>
<c:import url="employeeNav.jsp"/>
<c:if test="${message.length()>0}">
    <div id="error" class="msg">
        <span>Внимание!</span> ${message}
    </div>
</c:if>

<div class="form" id="route-select-form">
    <form  action="setTrainRoute" method="POST">
        <table>
            <tr>
                <td>Введите номер поезда</td><td><input class="route-select" type="text" name="trainNumber"></td>
            </tr>
            <tr>
                <td>Введите число мест в поезеде</td><td><input class="route-select" type="text" name="numberOfSeats"></td>
            </tr>
        </table>

        <input type="hidden" value="${route}" name="route">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />

        <table class="allTrains" id="route-select">
            <caption>Задайте расписание поезда</caption>
            <tr>
                <td>Название станции</td>
                <td>Остановка</td>
                <td>Время прибытия</td>
                <td>Время отправления</td>
            </tr>
            <c:forEach var="station" items="${route}">
                <tr>
                    <td>${station.name}</td>
                    <td>
                        <c:choose>
                            <c:when test="${route.indexOf(station) == 0}">
                                <input type="checkbox" checked name="${station.id}stop" value="${station.id}">
                            </c:when>
                            <c:otherwise>
                                <c:if test="${route.indexOf(station) == route.size()-1}">
                                    <input type="checkbox" checked name="${station.id}stop" value="${station.id}">
                                </c:if>
                                <c:if test="${route.indexOf(station) != route.size()-1}">
                                    <input type="checkbox" name="${station.id}stop" value="${station.id}">
                                </c:if>
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td><c:if test="${route.indexOf(station)!=0}"><input type="datetime-local" name="${station.id}arrive"/></c:if></td>
                    <td><c:if test="${route.indexOf(station)!=route.size()-1}"><input type="datetime-local" name="${station.id}departure"/></c:if></td>
                </tr>
            </c:forEach>
            <tr>
                <td colspan="4"><button type="submit">Задать маршрут</button></td>
            </tr>
        </table>
    </form>
</div>

</body>
</html>
