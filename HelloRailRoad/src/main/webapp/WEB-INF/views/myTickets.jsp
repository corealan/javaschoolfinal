<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Мои билеты</title>
    <link href="${pageContext.request.contextPath}/resources/css/topnav.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/css/login.css" rel="stylesheet">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>

</head>
<body>
<script>
    $(document).ready(function(){

        $("#ticketList li").slice(4).hide();

        var mincount = 3;
        var maxcount = 10;


        $(window).scroll(function()
        {
            if($(window).scrollTop() + $(window).height() >= $(document).height() - 10) {
                $("#ticketList li").slice(mincount,maxcount).fadeIn(5000);

                mincount = mincount+5;
                maxcount = maxcount+5

            }
        });



    });
</script>
<c:import url="nav.jsp"/>
<c:forEach items="${tickets}" var="ticket">

        <ul id="ticketList" style="list-style: none">
            <li>
                <div class="form">
                <table class="ticket">
                    <tr>
                        <td>Имя: </td> <td> ${ticket.passenger.firstName}</td>
                        <td>Фамилия: </td> <td> ${ticket.passenger.lastName}</td>
                        <td>Дата рождения: </td> <td>${ticket.passenger.dateOfBirth}</td>
                    </tr>
                    <tr>
                        <td>Станция отправления: </td><td> ${ticket.departure.name}</td> <td>Время отправления:</td><td><fmt:formatDate type="both" timeStyle="short" dateStyle="short" value="${ticket.departureTime}"/></td>
                    </tr>
                    <tr>
                        <td>Станция назначения: </td><td>${ticket.destination.name}</td> <td>Время прибытия:</td><td><fmt:formatDate type="both" timeStyle="short" dateStyle="short" value="${ticket.arrivalTime}"/></td>
                    </tr>
                </table>
                </div>
            </li>
        </ul>


</c:forEach>

</body>
</html>
