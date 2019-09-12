<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>

<html>
<head>
    <title>Регистрация</title>
    <link href="${pageContext.request.contextPath}/resources/css/login.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/css/topnav.css" rel="stylesheet">
</head>
<body>
<c:import url="nav.jsp"/>
<c:if test="${message.length()>0}">
    <div id="error" class="msg">
        <span>Внимание!</span> ${message}
    </div>
</c:if>
    <div class="form" id="reg-form">
        <form action="<c:url value='registration' />" method='POST'>
            <input type="text" name="username" required placeholder="Логин"/>
            <input type="password" name="password" required placeholder="Пароль"/>
            <input type="password" name="passwordConfirm" required placeholder="Подтвердите пароль"/>
            <input type="text" name="firstName" required placeholder="Имя"/>
            <input type="text" name="lastName" required placeholder="Фамилия"/>
            <input type="date" name="DOB" required placeholder="Дата рождения" />
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
            <button type="submit">Зарегистрироваться</button>
            <p class="message">Уже зарегистрированы? <a href="/HelloRailRoad/login">Войти</a></p>
        </form>
    </div>

</form>
</body>
</html>
