<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setBundle basename="localization_en_US"/>
<html>
<head>
    <link href="myStyle.css" rel = "stylesheet" type = "">
    <title>Your cabinet</title>
</head>
<body>
    <form>
        <div class="my-menu">
            <a href="account.jsp" class="active" >
                <fmt:message key="menu.cabinet"/>
            </a>
            <a href="cards.jsp">
                <fmt:message key="menu.cards"/>
            </a>
            <a href="loginPage.jsp">
                <fmt:message key="menu.leave"/>
            </a>
        </div>
        <div class="my-info">
            <p>
            <span style="font-weight: 600"><fmt:message key="label.login" /></span>:
            ${sessionScope.currUser.login}
            <hr>
            <span style="font-weight: 600"><fmt:message key="label.email"/></span>:
            ${sessionScope.currUser.email}
            <hr>
            <span style="font-weight: 600"><fmt:message key="label.status"/></span>:
            ${sessionScope.currUser.status}
            </p>
        </div>
    </form>
</body>
</html>