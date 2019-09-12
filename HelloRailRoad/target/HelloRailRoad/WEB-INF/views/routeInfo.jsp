<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Информация о маршруте</title>
    <sec:authorize access="hasAnyRole('USER', 'ANONYMOUS')">
        <c:import url="nav.jsp"/>
    </sec:authorize>
    <sec:authorize access="hasAnyRole('ADMIN')">
        <c:import url="employeeNav.jsp"/>
    </sec:authorize>

    <link href="${pageContext.request.contextPath}/resources/css/login.css" rel="stylesheet">

</head>
<body>
<table class="allTrains">
    <caption>Информация о маршруте поезда № ${train.trainNumber}</caption>
    <tr>
        <td>Станция</td>
        <td>Время прибытия</td>
        <td>Время отправления</td>
    </tr>

        <c:forEach items="${fullRoute}" var="station" varStatus="id">
            <tr>
                <td>${station.name}</td>
                <c:choose>
                    <c:when test="${route.contains(station)}">
                        <td>
                            <c:if test="${train.schedules.get(route.indexOf(station)).arrivalTime==null}">-</c:if>
                            <fmt:formatDate value="${train.schedules.get(route.indexOf(station)).arrivalTime}" type="both" dateStyle="short" timeStyle="short"/>
                        </td>
                    </c:when>
                    <c:otherwise><td>-</td></c:otherwise>
                </c:choose>
                <c:choose>
                    <c:when test="${route.contains(station)}">
                        <td>
                            <c:if test="${train.schedules.get(route.indexOf(station)).departureTime==null}">-</c:if>
                            <fmt:formatDate value="${train.schedules.get(route.indexOf(station)).departureTime}" type="both" dateStyle="short" timeStyle="short"/>
                        </td></c:when>
                    <c:otherwise><td>-</td></c:otherwise>
                </c:choose>
            </tr>
        </c:forEach>
</table>
</body>
</html>
