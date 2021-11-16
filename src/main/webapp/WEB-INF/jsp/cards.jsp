<%@include file="/WEB-INF/jspf/page.jspf"%>
<%@include file="/WEB-INF/jspf/tags.jspf"%>
<fmt:setBundle basename="localization_en_US"/>
<html>
<c:set var="title" value="Your cards" scope="page"/>
<%@ include file="/WEB-INF/jspf/head.jspf"%>
<body>
<form>
    <table id="main-container">
        <%@include file="/WEB-INF/jspf/header.jspf"%>
        <tr>
            <td class="content">
                <table id="cards_table">
                    <thead>
                    <tr>
                        <td></td>
                        <td>Name</td>
                        <td>Number</td>
                        <td>Balance</td>
                        <td>Expiration date</td>
                        <td>CVV</td>
                        <td>Status</td>
                    </tr>
                    </thead>
                    <c:forEach var="item" items="${requestScope.listCards}">
                        <tr>
                            <td>
                                <a href="controller?command=getPayments&cardItem=${item.cardID}">History</a>
                            </td>
                            <td>${item.name}</td>
                            <td>${item.number}</td>
                            <td>${item.balance}</td>
                            <td>${item.textDate}</td>
                            <td>${item.cvv}</td>
                            <td>${item.status.toString()}</td>
                        </tr>
                    </c:forEach>
                </table>
                <a href="controller?command=getNewCardPage">Add new card</a>
            </td>
        </tr>
        <%@ include file="/WEB-INF/jspf/footer.jspf" %>
    </table>
    <hr>
</form>
</body>
</html>