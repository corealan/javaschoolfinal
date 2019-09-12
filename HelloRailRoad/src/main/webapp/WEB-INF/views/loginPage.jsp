<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
    <title>Login</title>
    <link href="${pageContext.request.contextPath}/resources/css/login.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/css/topnav.css" rel="stylesheet">

</head>
<body>
<c:import url="nav.jsp"/>
<c:if test="${error==true}">
    <div id="error" class="msg">
        <span>Внимание!</span> Неверно введены логин/пароль!
    </div>
</c:if>
<div class="login-page">
    <div class="form">
        <form action="<c:url value='login' />" method='POST' class="login-form">
            <input name="username" type="text" placeholder="Логин"/>
            <input name="password" type="password" placeholder="Пароль"/>
            <button type="submit">Войти</button>
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
            <p class="message">Не зарегистрированы? <a href="/HelloRailRoad/registration">Зарегистрироваться</a></p>
        </form>
    </div>
</div>
</body>
</html>