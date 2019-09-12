<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
    <title>Добавить станцию</title>
    <script src="${pageContext.request.contextPath}/resources/js/jquery-3.4.1.min.js"></script>
    <link href="${pageContext.request.contextPath}/resources/css/login.css" rel="stylesheet"/>
</head>
<body>
<c:import url="employeeNav.jsp"/>
<c:if test="${error==true}">
    <div id="error" class="msg">
        <span>Внимание!</span> ${message}
    </div>
</c:if>
<c:if test="${error==false}">
    <div id="success" class="msg">
        <span>Успешно!</span> ${message}
    </div>
</c:if>
<div class="form">
    <form action="addNewStation" method='POST'>
        <table>
            <caption>Добавить новую станцию/Связать существующие</caption>
            <tr>
                <td>Название станции:</td>
                <td><input type='text' name='stationName' value=''></td>
            </tr>
            <tr>
                <td>Название смежной станции</td>
                <td><input id="test" type='text' list="stationList" name='adjacentStation' /></td>
            </tr>
            <tr>
                <td>Название 2й смежной станции<br/>
                    (если новая станция между существующими)</td>
                <td><input id="adjacent2" list="stationList" type='text' name='adjacentStation2' /></td>
            </tr>
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" /></td>
        </table>
        <button type="submit">Добавить станцию</button>
    </form>
</div>
<datalist id="stationList" >
    <c:forEach var="station" items="${stations}">
        <option value="${station.name}"/>
    </c:forEach>
</datalist>
</body>
</html>
