<%@ include file="/WEB-INF/jspf/page.jspf" %>
<%@ include file="/WEB-INF/jspf/tags.jspf" %>

<html>
<c:set var="title" value="Your cabinet" scope="page" />
<%@ include file="/WEB-INF/jspf/head.jspf"%>
<body>
<table id="main-container">
    <%@include file="/WEB-INF/jspf/header.jspf"%>
    <tr>
        <td class="content">
            <p>
            <span><fmt:message key="label.login" /></span>:
            ${sessionScope.currUser.login}
            <hr>
            <span ><fmt:message key="label.email"/></span>:
            ${sessionScope.currUser.email}
            <hr>
            <span ><fmt:message key="label.role"/></span>:
            ${sessionScope.userRole.toString()}
            <hr>
            <span ><fmt:message key="label.status"/></span>:
            ${sessionScope.currUser.status}
            </p>
        </td>
    </tr>

</table>
</body>
</html>