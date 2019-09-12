<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>

<head>
    <title>TrainList</title>
    <link href="${pageContext.request.contextPath}/resources/css/login.css" rel="stylesheet">

</head>
<body>
<sec:authorize access="hasAnyRole('USER','ANONYMOUS')">
    <c:import url="nav.jsp"/>
</sec:authorize>
<sec:authorize access="hasAnyRole('ADMIN')">
    <c:import url="employeeNav.jsp"/>
</sec:authorize>

<c:choose>
    <c:when test="${message.length()>0}">
        <div id="error" class="msg">
            <span>Внимание!</span> ${message}
        </div>
    </c:when>
    <c:when test="${trains.size()==0}">
        <sec:authorize access="hasAnyRole('USER','ANONYMOUS')" >
            <div class="form">
                <form action="findComplexRoutes">
                    Прямые маршруты не найдены! Искать маршрут с пересадкой?
                    <button type="submit">Искать</button>
                    <input type="hidden" name="departure" value="${departure}">
                    <input type="hidden" name="destination" value="${destination}">
                    <input type="hidden" name="departureTime" value="${departureTime.toLocaleString()}">
                    <input type="hidden" name="arrivalTime" value="${arrivalTime.toLocaleString()}">
                </form>
            </div>
        </sec:authorize >
        <sec:authorize access="hasAnyRole('ADMIN')">
            <div id="info" class="msg">
                <span>Внимание!</span> По данной станции нет поездов!
            </div>
        </sec:authorize>
    </c:when>
    <c:otherwise>
        <table class="allTrains">
            <caption>Список поездов</caption>
            <tr>
                <td>ID</td>
                <td>Номер поезда</td>
                <td>Число мест</td>
                <td>Станция отправления</td>
                <td>Время отправления</td>
                <td>Станция назначения</td>
                <td>Время прибытия</td>
                <td>Информация о маршруте</td>
                <sec:authorize access="hasRole('ADMIN')">
                    <td>Информация о пассажирах</td>
                </sec:authorize>
                <sec:authorize access="hasRole('ADMIN')">
                    <td>Отмена поезда</td>
                </sec:authorize>
                <sec:authorize access="hasAnyRole('USER','ANONYMOUS')">
                    <td>Билет</td>
                </sec:authorize>


            </tr>
            <c:forEach var="train" items="${trains}">
                <tr>
                    <td>${train.id}</td>
                    <td>${train.trainNumber}</td>
                    <td>${train.numberOfSeats}</td>

                    <td>${train.schedules.get(0).station.name}</td>
                    <td>${train.schedules.get(0).departureTime.toLocaleString()}</td>

                    <td>${train.schedules.get(train.schedules.size()-1).station.name}</td>
                    <td>${train.schedules.get(train.schedules.size()-1).arrivalTime.toLocaleString()}</td>
                    <td><a href="/HelloRailRoad/routeInfo/${train.id}">Просмотр</a> </td>
                    <sec:authorize access="hasRole('ADMIN')">
                        <td><a href="/HelloRailRoad/admin/passengersOnTrain/${train.id}">Просмотр</a> </td>
                    </sec:authorize>
                    <sec:authorize access="hasRole('ADMIN')">
                        <td><a href="/HelloRailRoad/cancelTrain/${train.id}">Отмена</a></td>
                    </sec:authorize>
                    <sec:authorize access="hasAnyRole('USER','ANONYMOUS')">
                        <c:choose>
                            <c:when test="${train.schedules.get(train.route.indexOf(departure)).departureTime.time > currentTime+900000}">
                                <td><a href="passenger/ticketPurchase/${train.id}/${departure}/${destination}">Купить билет</a> </td>
                            </c:when>
                            <c:otherwise>
                                <td></td>
                            </c:otherwise>
                        </c:choose>
                    </sec:authorize>
                </tr>
            </c:forEach>
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
        </table>
    </c:otherwise>
</c:choose>



<sec:authorize access="hasAnyRole('USER','ANONYMOUS')">
    <div class="pagination">
        <c:forEach begin="1" end="${numOfPages}" var="i">
            <c:choose>
                <c:when test="${page eq i}">
                    <td>${i}</td>
                </c:when>
                <c:otherwise>
                    <td> <a href="/HelloRailRoad/findTrains/${departure}/${departureTime.toLocaleString()}/${destination}/${arrivalTime.toLocaleString()}/${i}">${i}</a></td>
                </c:otherwise>
            </c:choose>
        </c:forEach>
    </div>
</sec:authorize>

<sec:authorize access="hasRole('ADMIN')">
    <div class="pagination">
        <c:forEach begin="1" end="${numOfPages}" var="i">
            <c:choose>
                <c:when test="${page eq i}">
                    <td>${i}</td>
                </c:when>
                <c:otherwise>
                    <td> <a href="/HelloRailRoad/admin/findTrains/${station}/${i}/${numOfPages}">${i}</a></td>
                </c:otherwise>
            </c:choose>
        </c:forEach>
    </div>
</sec:authorize>

</body>
</html>
