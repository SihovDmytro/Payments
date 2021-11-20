<%@ include file="/WEB-INF/jspf/tags.jspf"%>
<%@ include file="/WEB-INF/jspf/page.jspf"%>
<!DOCTYPE html>
<html>
<c:set var="title" value="${sessionScope.resultTitle}" scope="page" />
<%@ include file="/WEB-INF/jspf/head.jspf"%>

<body>
<table id="main-container">
    <tr >
        <td class="content center">
            <h1>${sessionScope.resultMessage}</h1>
            <br>
            <a href="loginPage.jsp">Login Page</a>
            <c:if test="${not empty sessionScope.currUser}">
                <a href="controller?command=getUserInfo">User Page</a>
                <a href="controller?command=getPayments&cardItem=${sessionScope.currCardID}">Card Page</a>
            </c:if>
        </td>
    </tr>
    <%@include file="/WEB-INF/jspf/footer.jspf"%>
</table>
</body>
</html>