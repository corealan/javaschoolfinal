<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>

<html>
<head>
    <title>Доступ запрещен</title>
    <link href="${pageContext.request.contextPath}/resources/css/login.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/css/topnav.css" rel="stylesheet">

</head>
<body>
<c:import url="nav.jsp"/>
<div id="error" class="msg">
    <span>Внимание!</span> У вас нет доступа к этой странице!</br>
    Нажмите <span><a href="<c:url value="/"/> ">здесь</a></span> чтобы вернуться на главную.
</div>
</body>
</html>
