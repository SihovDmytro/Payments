<%@ include file="/WEB-INF/jspf/page.jspf" %>
<%@ include file="/WEB-INF/jspf/tags.jspf" %>
<%@ page import="com.my.payment.db.Status" %>
<html>
<c:set var="title" value="User cabinet" scope="page"/>
<%@ include file="/WEB-INF/jspf/head.jspf" %>
<body>
<table id="main-container">
    <%@ include file="/WEB-INF/jspf/changeLocale.jspf" %>
    <%@include file="/WEB-INF/jspf/header.jspf" %>
    <tr>
        <td class="content">
            <p>
                <span><fmt:message key="label.login"/></span>:
                ${requestScope.user.login}
            <hr>
            <span><fmt:message key="label.fullName"/></span>:
            ${requestScope.user.fullName}
            <hr>
            <span><fmt:message key="label.birth"/></span>:
            ${requestScope.user.textDate}
            <hr>
            <span><fmt:message key="label.email"/></span>:
            ${requestScope.user.email}
            <hr>
            <span><fmt:message key="label.role"/></span>:
            <c:if test="${requestScope.user.role==Role.USER}">
                <fmt:message key='role.user'/>
            </c:if>
            <c:if test="${requestScope.user.role==Role.ADMIN}">
                <fmt:message key='role.admin'/>
            </c:if>
            <hr>
            <span><fmt:message key="label.status"/></span>:
            <c:if test="${requestScope.user.status==Status.BLOCKED}">
                <fmt:message key='user.status.blocked'/>
            </c:if>
            <c:if test="${requestScope.user.status==Status.ACTIVE}">
                <fmt:message key='user.status.active'/>
            </c:if>
            </p>
        </td>
    </tr>

</table>
</body>
</html>