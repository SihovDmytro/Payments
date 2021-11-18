<%@include file="/WEB-INF/jspf/page.jspf"%>
<%@include file="/WEB-INF/jspf/tags.jspf"%>
<%@ page import="com.my.payment.db.Status" %>
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
                <table id="cards_table" class="js-sort-table">
                    <thead>
                    <tr>
                        <td></td>
                        <td class="js-sort-string">Name</td>
                        <td class="js-sort-number">Number</td>
                        <td class="js-sort-number">Balance</td>
                        <td class="js-sort-date">Expiration date</td>
                        <td class="js-sort-number">CVV</td>
                        <td class="js-sort-string">Status</td>
                        <td></td>
                    </tr>
                    </thead>
                    <c:forEach var="item" items="${requestScope.listCards}">
                        <tr>
                            <td>
                                <c:if test="${item.status == Status.ACTIVE}">
                                    <a href="controller?command=getPayments&cardItem=${item.cardID}">Card info</a>
                                </c:if>
                            </td>
                            <td>${item.name}</td>
                            <td>${item.number}</td>
                            <td>${item.balance}</td>
                            <td>${item.textDate}</td>
                            <td>${item.cvv}</td>
                            <td>${item.status.toString()}</td>
                            <td>
                                <c:if test="${item.status == Status.BLOCKED}">
                                    <a href="">Send a request to unlock</a>
                                </c:if>
                                <c:if test="${item.status == Status.ACTIVE}">
                                    <a href="controller?command=blockCard&cardItem=${item.cardID}">Block the card</a>
                                </c:if>
                            </td>
                        </tr>
                    </c:forEach>
                </table>
                <a href="controller?command=getNewCardPage">Add new card</a>
            </td>
        </tr>
        <script src="${pageContext.request.contextPath}/sort-table.js"></script>
        <%@ include file="/WEB-INF/jspf/footer.jspf" %>
    </table>
    <hr>
</form>
</body>
</html>