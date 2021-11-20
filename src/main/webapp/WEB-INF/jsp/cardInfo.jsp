<%@include file="/WEB-INF/jspf/page.jspf"%>
<%@include file="/WEB-INF/jspf/tags.jspf"%>
<fmt:setBundle basename="localization_en_US"/>
<html>
<c:set var="title" value="Your cards" scope="page"/>
<%@ include file="/WEB-INF/jspf/head.jspf"%>
<body>
    <table id="main-container">
        <%@include file="/WEB-INF/jspf/header.jspf"%>
        <tr>
            <td class="content">
                <p>
                    <span>Card name: </span>
                    ${requestScope.currCard.name}
                    <hr>
                    <span>Card number: </span>
                    ${requestScope.currCard.number}
                    <hr>
                    <span>Expiration date: </span>
                    ${requestScope.currCard.textDate}
                     <hr>
                    <span>Balance: </span>
                    ${requestScope.currCard.textBalance}
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
                    <hr>
                    <span>Status: </span>
                    ${requestScope.currCard.status.toString()}
                    <hr>
                    <span>CVV: </span>
                    ${requestScope.currCard.cvv}
                    <hr>
                </p>
                <table id="cards_table" class="js-sort-table" >
                <thead>
                    <tr>
                        <th class="js-sort-string">In/Out</th>
                        <th class="js-sort-number">Number</th>
                        <th class="js-sort-number">Amount</th>
                        <th class="js-sort-date">Date</th>
                        <th class="js-sort-string">Status</th>
                    </tr>
                </thead>
                    <c:forEach var="item" items="${requestScope.payments}">
                        <tr>
                                <c:if test="${item.from.cardID==requestScope.currCard.cardID}">
                                    <td>outgoing</td>
                                    <td>
                                            ${item.to.number}
                                    </td>
                                    <td>${item.amount}</td>
                                    <td>${item.textDateTime}</td>
                                    <td>${item.status.toString()}</td>
                                </c:if>
                                <c:if test="${item.to.cardID==requestScope.currCard.cardID}">
                                    <td>incoming</td>
                                    <td>
                                        ${item.from.number}
                                    </td>
                                    <td>${item.amount}</td>
                                    <td>${item.textDateTime}</td>
                                    <td>${item.status.toString()}</td>
                                </c:if>
                        </tr>
                    </c:forEach>
                </table>
                <a href="controller?command=makePayment&fromID=${requestScope.currCard.cardID}">Make a payment</a>
            </td>
        </tr>
        <script src="${pageContext.request.contextPath}/sort-table.js"></script>
        <%@ include file="/WEB-INF/jspf/footer.jspf" %>
    </table>
    <hr>
</body>
</html>