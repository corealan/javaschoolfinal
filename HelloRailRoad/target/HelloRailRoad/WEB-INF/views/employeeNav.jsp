<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <link href="${pageContext.request.contextPath}/resources/css/topnav.css" rel="stylesheet">
    <title></title>
</head>
<body>
<div class="topnav">
    <a class="active" href="/HelloRailRoad/admin/">Главная</a>
    <a href="/HelloRailRoad/admin/addNewStation">Добавить станцию</a>
    <a href="/HelloRailRoad/admin/routesRequest">Добавить поезд</a>
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
