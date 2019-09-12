<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
    <title>Пассажиры поезда</title>
    <link href="${pageContext.request.contextPath}/resources/css/login.css" rel="stylesheet">
</head>
<body>
<c:import url="employeeNav.jsp"/>
<c:if test="${tickets.size()==0}">
    <div id="error" class="msg">
        <span>Внимание!</span> На данный поезд нет зарегистрированных пассажиров!
    </div>
</c:if>
<c:if test="${tickets.size()>0}">
    <div>
        <table class="allTrains">
            <caption>Пассжиры зарегистрированные на поезд № ${tickets.get(0).train.trainNumber} отправляющийся <fmt:formatDate type="date" value="${tickets.get(0).departureTime}"/></caption>
            <tr>
                <td>Имя пассажира</td>
                <td>Фамилия пассажира</td>
                <td>Дата рождения пассажира</td>
                <td>Станция посадки</td>
                <td>Стация высадки</td>
            </tr>
            <c:forEach items="${tickets}" var="ticket">
                <tr>
                    <td>${ticket.passenger.firstName}</td>
                    <td>${ticket.passenger.lastName}</td>
                    <td><fmt:formatDate type = "date"
                                        value = "${ticket.passenger.dateOfBirth}" /></td>
                    <td>${ticket.departure.name}</td>
                    <td>${ticket.destination.name}</td>
                </tr>
            </c:forEach>
        </table>
    </div>
</c:if>


</body>
</html>
