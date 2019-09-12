<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
    <title>Title</title>
    <link href="${pageContext.request.contextPath}/resources/css/topnav.css" rel="stylesheet">
</head>
<body>
<div class="topnav">
    <a class="active" href="/HelloRailRoad">Главная</a>

    <sec:authorize access="hasRole('USER')">
        <a href="/HelloRailRoad/myTickets">Мои билеты</a>
    </sec:authorize>

    <div class="topnav-right">
        <sec:authorize access="!isAuthenticated()">
            <a href="/HelloRailRoad/login">Вход</a>
        </sec:authorize>

        <sec:authorize access="isAuthenticated()">
            <form id="logout" action="/HelloRailRoad/logout" method="post">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                <a href="#" onclick=document.getElementById("logout").submit(); return false;>Выход</a>
            </form>
        </sec:authorize>
    </div>
</div>
</body>
</html>
