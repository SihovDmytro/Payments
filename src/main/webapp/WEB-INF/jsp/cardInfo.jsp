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
                        <td>Number</td>
                        <td>Amount</td>
                        <td>Date</td>
                        <td>Status</td>
                    </tr>
                    </thead>
                    <c:forEach var="item" items="${requestScope.payments}">
                        <tr>
                                <c:if test="${item.from.cardID==requestScope.currCard.cardID}">
                                    <td>outgoing</td>
                                    <td>
                                        <p>to</p>
                                            ${item.to.number}
                                    </td>
                                    <td>${item.amount}</td>
                                    <td>${item.textDateTime}</td>
                                    <td>${item.to.status.toString()}</td>
                                </c:if>
                                <c:if test="${item.to.cardID==requestScope.currCard.cardID}">
                                    <td>incoming</td>
                                    <td>
                                        <p>from</p>
                                            ${item.from.number}
                                    </td>
                                    <td>${item.amount}</td>
                                    <td>${item.textDateTime}</td>
                                    <td>${item.from.status.toString()}</td>
                                </c:if>
                        </tr>
                    </c:forEach>
                </table>
                <a href="controller?command=makePayment&fromID=${requestScope.currCard.cardID}">Make a payment</a>
            </td>
        </tr>
        <%@ include file="/WEB-INF/jspf/footer.jspf" %>
    </table>
    <hr>
</form>
</body>
</html>