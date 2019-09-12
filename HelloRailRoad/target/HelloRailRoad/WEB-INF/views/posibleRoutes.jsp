<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>

<html>
<head>
    <link href="${pageContext.request.contextPath}/resources/css/login.css" rel="stylesheet">
    <title>PossibleRoutes</title>
</head>
<body>
<c:import url="employeeNav.jsp"/>
<div class="form" id="routes-form">
    <form action="routeStationSelect" method="POST">
        <table>
            <caption>Выберите подходящий маршрут:</caption>
            <c:forEach var="route" items="${routes}">
                <tr>
                    <td>  <input class="radio" type="radio" name="route" value="${route}"/> </td>
                    <td>
                        <c:forEach var="station" items="${route}">
                            <c:out value="${station.name}"/>
                            <c:if test="${route.indexOf(station) != route.size()-1}">
                                <c:out value=">>>"/>
                            </c:if>
                        </c:forEach>
                    </td>
                </tr>


            </c:forEach>
        </table>

        <button class="route-select-button" type="submit">Далее</button>
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
    </form>
</div>
</body>
</html>
