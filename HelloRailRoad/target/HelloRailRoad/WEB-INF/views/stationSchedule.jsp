<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
    <title>Расписание</title>
    <link href="${pageContext.request.contextPath}/resources/css/topnav.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/css/login.css" rel="stylesheet">
</head>
<body>
<c:import url="nav.jsp"/>
<c:if test="${schedules.size() == 0}">
    <div id="warning" class="msg">
        <span>Внимание!</span> По данной станции пока нет расписания!
    </div>
</c:if>
<c:if test="${schedules.size() > 0}">
    <table class="allTrains" id="schedule-table">
        <caption>Расписание движения поездов по станции ${schedules.get(0).station.name}</caption>
        <tr>
            <td>Номер поезда</td>
            <td>Станция назначения</td>
            <td>Время прибытия</td>
            <td>Время отправления</td>
            <sec:authorize access="hasAnyRole('USER','ANONYMOUS')">
                <td>Билет</td>
            </sec:authorize>
            <sec:authorize access="hasAnyRole('ADMIN')">
                <td>Изменить</td>
            </sec:authorize>
        </tr>
        <c:forEach var="schedule" items="${schedules}">
            <tr>
                <td>${schedule.train.trainNumber}</td>
                <td>${schedule.train.route.get(schedule.train.route.size()-1).name}</td>

                <td><fmt:formatDate type = "both" dateStyle = "short" timeStyle = "short" value = "${schedule.arrivalTime}" /></td>
                <td><fmt:formatDate type = "both" dateStyle = "short" timeStyle = "short" value = "${schedule.departureTime}"/></td>
                <sec:authorize access="hasAnyRole('USER','ANONYMOUS')">
                <c:choose>
                    <c:when test="${schedule.departureTime.time > currentTime+900000}">
                        <td><a href="passenger/ticketPurchase/${schedule.train.id}/${schedule.station}/${schedule.train.route.get(schedule.train.route.size()-1)}">Купить билет</a> </td>
                    </c:when>
                    <c:otherwise>
                        <td></td>
                    </c:otherwise>
                </c:choose>
                </sec:authorize>
                <sec:authorize access="hasAnyRole('ADMIN')">
                    <td><a href="/HelloRailRoad/admin/update_schedule/${schedule.id}">Изменить расписание</a> </td></td>
                </sec:authorize>

            </tr>
        </c:forEach>
    </table>
    <div class="pagination">
        <c:forEach begin="1" end="${numOfPages}" var="i">
            <c:choose>
                <c:when test="${page eq i}">
                    <td>${i}</td>
                </c:when>
                <c:otherwise>
                    <sec:authorize access="hasAnyRole('ADMIN')">
                        <td> <a href="/HelloRailRoad/admin/getSchedule/${schedules.get(0).station.id}/${date}/${i}">${i}</a></td>
                    </sec:authorize>
                    <sec:authorize access="hasAnyRole('USER','ANONYMOUS')">
                        <td> <a href="/HelloRailRoad/getSchedule/${schedules.get(0).station.id}/${date}/${i}">${i}</a></td>
                    </sec:authorize>
                </c:otherwise>
            </c:choose>
        </c:forEach>
    </div>
</c:if>
</body>
</html>
