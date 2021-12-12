
<%@include file="/WEB-INF/jspf/page.jspf"%>
<%@include file="/WEB-INF/jspf/tags.jspf"%>
<%@ page import="com.my.payment.db.PaymentStatus" %>
<%@ page import="com.my.payment.db.Status" %>
<%@ page import="com.my.payment.constants.Path" %>
<html>
<c:set var="title" value="Your card" scope="page"/>
<%@ include file="/WEB-INF/jspf/head.jspf"%>
<body>
    <table id="main-container">
        <%@ include file="/WEB-INF/jspf/changeLocale.jspf"%>
        <%@include file="/WEB-INF/jspf/header.jspf"%>
        <tr>
            <td class="content">
                <span><fmt:message key='card.name'/>: </span>
                ${sessionScope.currCard.name}

                <c:if test="${sessionScope.userRole==Role.USER}">
                    <br>
                    <c:if test="${not empty sessionScope.invalidName}">
                        ${sessionScope.invalidName}
                    </c:if>
                    <form action="controller" method="post">
                        <input type="hidden" name="command" value="changeCardName">
                        <div id="windowName" >
                            <label for="cardName" ><fmt:message key='label.card.newName'/></label>
                            <input type ="text" id="cardName" name = "cardName" required maxlength="45"/>
                            <button type="submit"><fmt:message key='label.change'/></button>
                            <br>
                        </div>
                    </form>
                    <a href="#windowName"><fmt:message key='card.changeName'/></a>
                </c:if>

                <hr>
                <span><fmt:message key='card.number'/>: </span>
                ${sessionScope.currCard.number}
                <hr>
                <span><fmt:message key='card.date'/>: </span>
                ${sessionScope.currCard.textDate}
                 <hr>
                <span><fmt:message key='card.balance'/>: </span>
                ${sessionScope.currCard.balance}
                <c:if test="${sessionScope.userRole==Role.USER}">
                    <br>
                    <c:if test="${not empty sessionScope.amountLimit}">
                        ${sessionScope.amountLimit}
                    </c:if>
                    <form action="controller" method="post">
                        <input type="hidden" name="command" value="topUp">
                        <div id="windowTopUp" >
                            <label for="topUp" ><fmt:message key='card.amount'/></label>
                            <input type ="number" id="topUp" name = "topUp" required step="0.01" min="1" max="999999999"/>
                            <button type="submit"><fmt:message key='card.topUp'/></button>
                            <br>
                        </div>
                    </form>
                    <a href="#windowTopUp"><fmt:message key='card.topUpBal'/></a>
                </c:if>
                <hr>
                <span><fmt:message key='label.status'/>: </span>
                <c:if test="${sessionScope.currCard.status==Status.BLOCKED}">
                    <fmt:message key='card.status.blocked'/>
                </c:if>
                <c:if test="${sessionScope.currCard.status==Status.ACTIVE}">
                    <fmt:message key='card.status.active'/>
                </c:if>
                <c:if test="${sessionScope.currCard.status==Status.UNBLOCK_REQUEST}">
                    <fmt:message key='card.status.unblockRequest'/>
                </c:if>
                <hr>
                <span>CVV: </span>
                <nf:numberFormat number="3">${sessionScope.currCard.cvv}</nf:numberFormat>
                <hr>
                <span>PIN: </span>
                <nf:numberFormat number="4">${sessionScope.currCard.pin}</nf:numberFormat>
                <hr>
                <table id = "mydatatable" class="table table-striped table-bordered">
                <thead>
                    <tr>
                        <th></th>
                        <th><fmt:message key='header.from'/></th>
                        <th><fmt:message key='header.to'/></th>
                        <th><fmt:message key='card.amount'/></th>
                        <th><fmt:message key='header.date'/></th>
                        <th></th>
                    </tr>
                </thead>
                    <tbody>
                    <c:forEach var="item" items="${requestScope.payments}">
                            <c:if test="${item.from.cardID==sessionScope.currCard.cardID}">
                                <tr class="outgoing">
                                    <td><fmt:message key='payment.out'/></td>
                                    <td>${item.from.number}</td>
                                    <td>${item.to.number}</td>
                                    <td>${item.amount}</td>
                                    <td>${item.textDateTime}</td>
                                    <td>
                                        <c:if test="${item.status==PaymentStatus.SENT}">
                                            <fmt:message key='payment.status.sent'/>
                                            <form action="controller" method="post" target="_blank">
                                                <input type="hidden" name="command" value="generateReport">
                                                <input type="hidden" name="paymentID" value="${item.id}">
                                                <button type="submit"><fmt:message key="label.payment.report"/></button>
                                            </form>
                                        </c:if>
                                        <c:if test="${item.status==PaymentStatus.PREPARED}">
                                            <fmt:message key='payment.status.prep'/>
                                        </c:if>
                                        <c:if test="${item.status == PaymentStatus.PREPARED and item.from.balance>=item.amount and sessionScope.userRole==Role.USER}">
                                            <form action="controller" method="post">
                                                <input type="hidden" name="command" value="commitPayment">
                                                <input type="hidden" name="paymentID" value="${item.id}">
                                                <button type="submit"><fmt:message key='payment.commit'/></button>
                                            </form>
                                        </c:if>
                                        <c:if test="${item.status == PaymentStatus.PREPARED and sessionScope.userRole==Role.USER}">
                                            <form action="controller" method="post">
                                                <input type="hidden" name="command" value="cancelPayment">
                                                <input type="hidden" name="paymentID" value="${item.id}">
                                                <button type="submit"><fmt:message key='payment.cancel'/></button>
                                            </form>
                                        </c:if>

                                    </td>
                                </tr>
                            </c:if>
                            <c:if test="${item.to.cardID==sessionScope.currCard.cardID}">
                                <tr class="incoming" >
                                    <td><fmt:message key='payment.in'/></td>
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
                    <a href="${Path.MAKE_PAYMENT_PAGE}"><fmt:message key='label.makePayment'/></a>
                </c:if>
                <c:remove var="amountLimit" scope="session"/>
                <c:remove var="invalidName" scope="session"/>
            </td>
        </tr>
        <%@ include file="/WEB-INF/jspf/scripts.jspf"%>

    </table>
    <hr>
</body>
</html>