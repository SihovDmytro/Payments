<%@include file="/WEB-INF/jspf/page.jspf"%>
<%@include file="/WEB-INF/jspf/tags.jspf"%>
<%@ page import="com.my.payment.db.PaymentStatus" %>
<%@ page import="com.my.payment.constants.Path" %>
<html>
<c:set var="title" value="Your card" scope="page"/>
<%@ include file="/WEB-INF/jspf/head.jspf"%>
<body>
    <table id="main-container">
        <%@include file="/WEB-INF/jspf/header.jspf"%>
        <tr>
            <td class="content">
                <span>Card name: </span>
                ${sessionScope.currCard.name}
                <hr>
                <span>Card number: </span>
                ${sessionScope.currCard.number}
                <hr>
                <span>Expiration date: </span>
                ${sessionScope.currCard.textDate}
                 <hr>
                <span>Balance: </span>
                ${sessionScope.currCard.balance}
                <c:if test="${sessionScope.userRole==Role.USER}">
                    <br>
                    <form action="controller" method="post">
                        <input type="hidden" name="command" value="topUp">
                        <div id="okno" >
                            <label for="topUp" >Amount</label>
                            <input type ="number" placeholder="Enter amount" id="topUp" name = "topUp" required step="0.01" min="1" max="999999999"/>
                            <button type="submit">Top up</button>
                            <br>
                            <c:if test="${not empty requestScope.amountLimit}">
                                ${requestScope.amountLimit}
                            </c:if>
                        </div>
                    </form>
                    <a href="#okno">Top up balance</a>
                </c:if>
                <hr>
                <span>Status: </span>
                ${sessionScope.currCard.status.toString()}
                <hr>
                <span>CVV: </span>
                ${sessionScope.currCard.cvv}
                <hr>
                <table id = "mydatatable" class="table table-striped table-bordered">
                <thead>
                    <tr>
                        <th>In/Out</th>
                        <th>Number from</th>
                        <th>Number to</th>
                        <th>Amount</th>
                        <th>Date</th>
                        <th></th>
                    </tr>
                </thead>
                    <tbody>
                    <c:forEach var="item" items="${requestScope.payments}">
                            <c:if test="${item.from.cardID==sessionScope.currCard.cardID}">
                                <tr class="outgoing">
                                    <td>outgoing</td>
                                    <td>${item.from.number}</td>
                                    <td>${item.to.number}</td>
                                    <td>${item.amount}</td>
                                    <td>${item.textDateTime}</td>
                                    <td>
                                            ${item.status.toString()}
                                        <c:if test="${item.status == PaymentStatus.PREPARED and item.from.balance>=item.amount and sessionScope.userRole==Role.USER}">
                                            <br>
                                            <form action="controller" method="post">
                                                <input type="hidden" name="command" value="commitPayment">
                                                <input type="hidden" name="paymentID" value="${item.id}">
                                                <button type="submit">Commit</button>
                                            </form>
                                        </c:if>
                                    </td>
                                </tr>
                            </c:if>
                            <c:if test="${item.to.cardID==sessionScope.currCard.cardID}">
                                <tr class="incoming" >
                                    <td>incoming</td>
                                    <td>${item.from.number}</td>
                                    <td>${item.to.number}</td>
                                    <td>${item.amount}</td>
                                    <td>${item.textDateTime}</td>
                                    <td></td>
                                </tr>
                            </c:if>
                    </c:forEach>
                    </tbody>
                </table>
                <c:if test="${sessionScope.userRole==Role.USER}">
                    <a href="${Path.MAKE_PAYMENT_PAGE}">Make a payment</a>
                </c:if>
            </td>
        </tr>
        <%@ include file="/WEB-INF/jspf/scripts.jspf"%>

    </table>
    <hr>
</body>
</html>