<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
    <title>Покупка успешна!</title>
    <link href="${pageContext.request.contextPath}/resources/css/login.css" rel="stylesheet">
</head>
<body>
<c:import url="nav.jsp"/>

<c:if test="${message.equals('wrong destination')}">
    <div id="error" class="msg">
        <span>Внимание!</span> Введенная станция назначения отсутствует в маршруте поезда!
    </div>
</c:if>

<c:if test="${message.equals('late')}">
    <div id="error" class="msg">
        <span>Внимание!</span> Нельзя приобрести билет менее чем за 10 минут до отправления!
    </div>
</c:if>

<c:if test="${message.equals('duplicate')}">
    <div id="error" class="msg">
        <span>Внимание!</span> Пассажир с такими данными уже зарегистрирован!
    </div>
</c:if>

<c:if test="${message.equals('success')}">
    <div id="success" class="msg">
        <span>Поздравляем!</span> Билет успешно приобретен!
    </div>
</c:if>


</body>
</html>
