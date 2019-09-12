<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
    <title>Покупка билета</title>
    <link href="${pageContext.request.contextPath}/resources/css/topnav.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/css/login.css" rel="stylesheet">
</head>
<body>
<c:import url="nav.jsp"/>

<c:if test="${numOfTickets < 1}">
    <div id="error" class="msg">
        <span>Внимание!</span> На этот маршрут билеты закончились!
    </div>
</c:if>

<c:if test="${numOfTickets > 0}">
    <div class="form">
        <form action="${pageContext.request.contextPath}/passenger/purchaseTicket" method="POST">
            <h4>На данный маршрут доступно ${numOfTickets} билетов.</h4>
            <table>
                <tr>
                    <td>Имя: </td> <td><input type="text" name="firstName" value="${passenger.firstName}" required></td>
                    <td>Фамилия: </td> <td><input type="text" name="lastName" value="${passenger.lastName}" required></td>
                    <td>Дата рождения: </td> <td><input type="date" name="DOB" value="${passenger.dateOfBirth}" required></td>
                </tr>
                <tr>
                    <td>Станция отправления: </td><td> <input type="text" name="departureStation" list="stationList" value="${departure.name}" required></td>
                    <td>Станция назначения: </td><td><input type="text" name="destinationStation" list="stationList" value="${destination.name}" required></td>
                </tr>
            </table>
            <button type="submit"> Купить билет </button>
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
            <input type="hidden" value="${train.id}" name="trainId">
            <input type="hidden" value="${ticketId}" name="ticketId">
            <input type="hidden" value="${ticketId}" name="ticketId">
            <input type="hidden" name="subroute" value="${route.subList(route.indexOf(departure)+1,route.size())}">
        </form>
    </div>
</c:if>

<datalist id="stationList" >
    <c:forEach var="station" items="${route.subList(route.indexOf(departure)+1,route.size())}">
        <option value="${station.name}"/>
    </c:forEach>
</datalist>

</body>
</html>
