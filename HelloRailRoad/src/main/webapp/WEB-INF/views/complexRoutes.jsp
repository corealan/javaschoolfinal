<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Маршруты с пересадкой</title>
    <link href="${pageContext.request.contextPath}/resources/css/login.css" rel="stylesheet">
</head>
<body>
<c:import url="nav.jsp"/>
<div>
    <c:forEach items="${complexRoutes}" var="route">
        <div class="form" id="complex-route-form">
            <table class="complex-route-table">
            <c:forEach items="${route}" var="train" varStatus="id">
                <tr>
                    <td>№${train.key.trainNumber}</td>
                    <td>${train.value.get(0).name}</td>
                    <td>Отправление: ${train.key.getScheduleByStation(train.value.get(0)).departureTime.toLocaleString()}</td>
                </tr>
                <tr>
                    <td></td>
                    <td>${train.value.get(1).name}</td>
                    <td>Прибытие: ${train.key.getScheduleByStation(train.value.get(1)).arrivalTime.toLocaleString()}</td>
                </tr>
                <tr id="complexTable${id.index}" >
                    <td colspan="3"> <img width="30px" height="50px" src="resources/img/transfer.png" alt="V"> </td>
                </tr>
            </c:forEach>
            </table>

            <form action="passenger/buyComplexRouteTickets">
                <c:forEach items="${route}" var="train" varStatus="count">
                    <input type="hidden" name="${count.index}trainId" value="${train.key.id}">
                    <input type="hidden" name="${count.index}departure" value="${train.value.get(0)}">
                    <input type="hidden" name="${count.index}destination" value="${train.value.get(1)}">
                </c:forEach>
                <input type="hidden" value="${route}" name="complexRoute">
                <button type="submit">Купить билеты</button>
            </form>
        </div>
    </c:forEach>
</div>
</body>
</html>
