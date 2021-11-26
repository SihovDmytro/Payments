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
            ${requestScope.errorMessage}<br>
            <a href="${Path.LOGIN_PAGE}"><fmt:message key='page.login'/></a>
            <c:if test="${not empty sessionScope.currUser}">
                <a href="controller?command=getUserInfo"><fmt:message key='page.Cabinet'/></a>
                <c:if test="${not empty sessionScope.currCard}">
                    <a href="controller?command=getPayments&cardItem=${sessionScope.currCard.cardID}"><fmt:message key='page.card'/></a>
                </c:if>
            </c:if>
        </td>
    </tr>
</table>
</body>
</html>