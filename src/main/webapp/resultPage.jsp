<%@ include file="/WEB-INF/jspf/tags.jspf"%>
<%@ include file="/WEB-INF/jspf/page.jspf"%>
<%@ page import="com.my.payment.constants.Path" %>
<!DOCTYPE html>
<html>
<c:set var="title" value="${sessionScope.resultTitle}" scope="page" />
<%@ include file="/WEB-INF/jspf/head.jspf"%>

<body>
<table id="main-container">
    <%@ include file="/WEB-INF/jspf/changeLocale.jspf"%>
    <tr >
        <td class="content center">
            <h1>${sessionScope.resultMessage}</h1>
            <br>
            <a href="loginPage.jsp"><fmt:message key='page.login'/></a>
            <c:if test="${not empty sessionScope.currUser}">
                <a href="${Path.USER_CABINET}"><fmt:message key='page.Cabinet'/></a>
                <c:if test="${not empty sessionScope.currCard}">
                    <a href="${Path.GET_CARD_INFO_COMMAND}&cardItem=${sessionScope.currCard.cardID}"><fmt:message key='page.card'/></a>
                </c:if>
            </c:if>
            <c:remove var="resultMessage" scope="session"/>
            <c:remove var="resultTitle" scope="session"/>
        </td>
    </tr>

</table>
</body>
</html>