<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<fmt:setBundle basename="localization_en_US"/>
<html>
<head>
    <link href="myStyle.css" rel = "stylesheet" type = "">
    <title>Your cards</title>
</head>
<body>
<form>
    <div class="my-menu">
        <a href="account.jsp">
            <fmt:message key="menu.cabinet"/>
        </a>
        <a href="controller?command=getCards" class="active" >
            <fmt:message key="menu.cards"/>
        </a>
        <a href="controller?command=logout">
            <fmt:message key="menu.leave"/>
        </a>
    </div>
    <table>
        <tr>
            <td class="content">
        <table id="cards_table">
            <thead>
            <tr>
                <td>Name</td>
                <td>Number</td>
                <td>Balance</td>
                <td>Expiration date</td>
                <td>CVV</td>
                <td>Status</td>
            </tr>
            </thead>


            <c:forEach var="item" items="${requestScope.listCards}">

                <tr>
                    <td>${item.name}</td>
                    <td>${item.number}</td>
                    <td>${item.balance}</td>
                    <td>${item.textDate}</td>
                    <td>${item.cvv}</td>
                    <td>${item.status.toString()}</td>
                </tr>
            </c:forEach>
        </table>
            </td>
        </tr>
    </table>
    <hr>
    <a href="">Add new card</a>
</form>
</body>
</html>