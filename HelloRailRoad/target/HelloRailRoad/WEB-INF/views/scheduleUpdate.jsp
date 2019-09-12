<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html>
<head>
    <title>Изменение расписания</title>
    <link href="${pageContext.request.contextPath}/resources/css/login.css" rel="stylesheet">

</head>
<body>
<c:import url="employeeNav.jsp"/>

<c:if test="${message.length()>0}">
    <div id="error" class="msg">
        <span>Внимание!</span> ${message}
    </div>
</c:if>

<div class="form" id="schedule-form">
    <form action="/HelloRailRoad/admin/update_schedule" method="post">
        <label>Изменение расписания</label>
        <table>
            <tr><td>Номер поезда</td> <td colspan="2">${schedule.train.trainNumber}</td></tr>
            <tr><td>Станция</td> <td colspan="2">${schedule.station.name}</td></tr>
            <tr>
                <c:if test="${schedule.arrivalTime!=null}">
                    <td>Время прибытия</td>
                    <td>До изменеий: ${schedule.arrivalTime.toLocaleString()}</td>
                    <td><input type="datetime-local" placeholder="${schedule.arrivalTime}" name="arrivalTime"/></td>
                </c:if>
            </tr>
            <tr>
                <c:if test="${schedule.departureTime!=null}">
                    <td>Время отправления</td>
                    <td>До изменений: ${schedule.departureTime.toLocaleString()}</td>
                    <td><input type="datetime-local" placeholder="${schedule.departureTime}" name="departureTime"/></td>
                </c:if>
            </tr>
        </table>
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
        <input type="hidden" name="id" value="${schedule.id}">
        <button type="submit" value="Изменить">Изменить расписание</button>
    </form>
</div>
</body>
</html>
