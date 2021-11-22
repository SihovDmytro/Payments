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
                <c:if test="${not empty sessionScope.currCard}">
                    <a href="controller?command=getPayments&cardItem=${sessionScope.currCard.cardID}">Card Page</a>
                </c:if>
            </c:if>
            <c:remove var="${sessionScope.resultMessage}"/>
        </td>
    </tr>

</table>
</body>
</html>