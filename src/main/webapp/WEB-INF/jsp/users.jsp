<%@include file="/WEB-INF/jspf/page.jspf"%>
<%@include file="/WEB-INF/jspf/tags.jspf"%>
<%@ page import="com.my.payment.db.Status" %>
<html>
<c:set var="title" value="Users" scope="page"/>
<%@ include file="/WEB-INF/jspf/head.jspf"%>
<body>
    <table id="main-container">
        <%@ include file="/WEB-INF/jspf/changeLocale.jspf"%>
        <%@include file="/WEB-INF/jspf/header.jspf"%>
        <tr>
            <td class="content">
                <table id = "mydatatable" class="table table-striped table-bordered">
                    <thead>
                    <tr>
                        <th><fmt:message key='label.login'/></th>
                        <th><fmt:message key='label.role'/></th>
                        <th><fmt:message key='label.email'/></th>
                        <th><fmt:message key='label.status'/></th>
                        <th></th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="item" items="${requestScope.users}">
                        <tr>
                            <td>${item.login}</td>
                            <td>
                                <c:if test="${item.role==Role.USER}">
                                    <fmt:message key='role.user'/>
                                </c:if>
                                <c:if test="${item.role==Role.ADMIN}">
                                    <fmt:message key='role.admin'/>
                                </c:if>
                            </td>
                            <td>${item.email}</td>
                            <td>
                                <c:if test="${item.status==Status.BLOCKED}">
                                    <fmt:message key='user.status.blocked'/>
                                </c:if>
                                <c:if test="${item.status==Status.ACTIVE}">
                                    <fmt:message key='user.status.active'/>
                                </c:if>
                            </td>
                            <td>
                                <c:if test="${item.role == Role.USER}">
                                        <form action="controller" method="post">
                                            <input type="hidden" name="command" value="changeUserStatus">
                                            <input type="hidden" name="userID" value="${item.userID}">
                                            <c:choose>
                                                <c:when test="${item.status == Status.ACTIVE}">
                                                    <input type="hidden" name="newStatus" value="${Status.BLOCKED}">
                                                    <button type="submit"><fmt:message key='user.block'/></button>
                                                </c:when>
                                                <c:otherwise>
                                                    <input type="hidden" name="newStatus" value="${Status.ACTIVE}">
                                                    <button type="submit"><fmt:message key='user.unblock'/></button>
                                                </c:otherwise>
                                            </c:choose>
                                        </form>
                                </c:if>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </td>
        </tr>
        <%@ include file="/WEB-INF/jspf/scripts.jspf"%>

    </table>
    <hr>
</body>
</html>