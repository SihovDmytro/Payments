<%@include file="/WEB-INF/jspf/page.jspf"%>
<%@include file="/WEB-INF/jspf/tags.jspf"%>
<%@ page import="com.my.payment.db.Status" %>
<html>
<c:set var="title" value="Users" scope="page"/>
<%@ include file="/WEB-INF/jspf/head.jspf"%>
<body>
    <table id="main-container">
        <%@include file="/WEB-INF/jspf/header.jspf"%>
        <tr>
            <td class="content">
                <table class="js-sort-table">
                    <thead>
                    <tr>
                        <td class="js-sort-string">Login</td>
                        <td class="js-sort-string">Role</td>
                        <td class="js-sort-string">Email</td>
                        <td class="js-sort-string">Status</td>
                        <td></td>
                    </tr>
                    </thead>
                    <c:forEach var="item" items="${requestScope.users}">
                        <tr>
                            <td>${item.login}</td>
                            <td>${item.role.toString()}</td>
                            <td>${item.email}</td>
                            <td>${item.status.toString()}</td>
                            <td>
                                <c:if test="${item.role == Role.USER}">
                                        <form action="controller" method="post">
                                            <input type="hidden" name="command" value="changeUserStatus">
                                            <input type="hidden" name="userID" value="${item.userID}">
                                            <c:choose>
                                                <c:when test="${item.status == Status.ACTIVE}">
                                                    <input type="hidden" name="newStatus" value="${Status.BLOCKED}">
                                                    <button type="submit">Block user</button>
                                                </c:when>
                                                <c:otherwise>
                                                    <input type="hidden" name="newStatus" value="${Status.ACTIVE}">
                                                    <button type="submit">Unblock user</button>
                                                </c:otherwise>
                                            </c:choose>
                                        </form>

                                </c:if>
                            </td>
                        </tr>
                    </c:forEach>
                </table>
            </td>
        </tr>
        <script src="${pageContext.request.contextPath}/sort-table.js"></script>

    </table>
    <hr>
</body>
</html>