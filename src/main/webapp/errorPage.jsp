<%@ include file="/WEB-INF/jspf/page.jspf" %>
<%@ include file="/WEB-INF/jspf/tags.jspf"%>
<%@ page import="com.my.payment.constants.Path" %>
<html>
<c:set var="title" scope="page" value="Error page"/>
<%@include file="/WEB-INF/jspf/head.jspf"%>
<body>
<table id="main-container">
    <%@ include file="/WEB-INF/jspf/changeLocale.jspf"%>
    <tr>
        <td class="content center">
            ${sessionScope.ErrorMessage}<br>
            <a href="${Path.LOGIN_PAGE}"><fmt:message key='page.login'/></a>
            <c:if test="${not empty sessionScope.currUser}">
                <a href="${Path.USER_CABINET}"><fmt:message key='page.Cabinet'/></a>
                <c:if test="${not empty sessionScope.currCard}">
                    <a href="${Path.GET_CARD_INFO_COMMAND}&cardItem=${sessionScope.currCard.cardID}"><fmt:message key='page.card'/></a>
                </c:if>
            </c:if>
            <c:remove var="ErrorMessage" scope="session"/>

        </td>
    </tr>
</table>
</body>
</html>