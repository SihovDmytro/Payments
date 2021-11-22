<%@include file="/WEB-INF/jspf/page.jspf"%>
<%@include file="/WEB-INF/jspf/tags.jspf"%>
<fmt:setBundle basename="localization_en_US"/>
<html>
<c:set var="title" value="Your card" scope="page"/>
<%@ include file="/WEB-INF/jspf/head.jspf"%>
<body>
    <table id="main-container">
        <%@include file="/WEB-INF/jspf/header.jspf"%>
        <tr>
            <td class="content">
                <p>
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
                    ${sessionScope.currCard.textBalance}
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
                    ${sessionScope.currCard.status.toString()}
                    <hr>
                    <span>CVV: </span>
                    ${sessionScope.currCard.cvv}
                    <hr>
                </p>

                <div class="center">
                    Page ${requestScope.currentPage} of ${requestScope.pageCount}
                    <br>

                    <c:choose>
                        <c:when test="${requestScope.currentPage - 1 > 0}">
                            <a href="controller?command=getPayments&cardItem=${sessionScope.currCard.cardID}&currentPage=${requestScope.currentPage-1}&recordsPerPage=${requestScope.recordsPerPage}&sort=${requestScope.sortType.toString()}&sortOrder=${requestScope.sortOrder.toString()}">Previous</a>
                        </c:when>
                        <c:otherwise>
                            Previous
                        </c:otherwise>
                    </c:choose>

                    <c:forEach var="p" begin="1" end="${requestScope.pageCount}">
                        <c:choose>
                            <c:when test="${requestScope.currentPage == p}">${p}</c:when>
                            <c:otherwise>
                                <a href="controller?command=getPayments&cardItem=${sessionScope.currCard.cardID}&currentPage=${p}&recordsPerPage=${requestScope.recordsPerPage}&sort=${requestScope.sortType.toString()}&sortOrder=${requestScope.sortOrder.toString()}">${p}</a>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>

                    <c:choose>
                        <c:when test="${requestScope.currentPage + 1 <= requestScope.pageCount}">
                            <a href="controller?command=getPayments&cardItem=${sessionScope.currCard.cardID}&currentPage=${requestScope.currentPage+1}&recordsPerPage=${requestScope.recordsPerPage}&sort=${requestScope.sortType.toString()}&sortOrder=${requestScope.sortOrder.toString()}">Next</a>
                        </c:when>
                        <c:otherwise>
                            Next
                        </c:otherwise>
                    </c:choose>
                </div>
                <form action="controller" >
                    <table class="settings-table center">
                        <tr>
                            <td>Records per page</td>
                            <td>Sort</td>
                        </tr>
                        <tr>
                            <td>
                                <input type="hidden" name="command" value="getPayments">
                                <input type="hidden" name="cardItem" value="${sessionScope.currCard.cardID}">

                                <br>
                                <select name="recordsPerPage" id = "recordsPerPage" onchange="this.form.submit()">
                                        <option value="10" ${10 == requestScope.recordsPerPage ? 'selected' : ''}>10</option>
                                        <option value="20" ${20 == requestScope.recordsPerPage ? 'selected' : ''}>20</option>
                                        <option value="100" ${100 == requestScope.recordsPerPage ? 'selected' : ''}>100</option>
                                </select>
                            </td>
                            <td>
                                <input type="radio" id="byNumberFrom" name="sort" value="BY_NUMBER_FROM" ${"BY_NUMBER_FROM" == requestScope.sortType.toString() ? 'checked' : ''}>
                                <label for="byNumberFrom">By number from</label>

                                <input type="radio" id="byNumberTo" name="sort" value="BY_NUMBER_TO" ${"BY_NUMBER_TO" == requestScope.sortType.toString() ? 'checked' : ''}>
                                <label for="byNumberTo">By number to</label>

                                <input type="radio" id="byDate" name="sort" value="BY_DATE" ${"BY_DATE" == requestScope.sortType.toString() ? 'checked' : ''}>
                                <label for="byDate">By date</label>
                                <br>
                                <input type="radio" id="ascOrder" name="sortOrder" value="ASCENDING" ${"ASCENDING" == requestScope.sortOrder.toString() ? 'checked' : ''}>
                                <label for="ascOrder">Ascending</label>

                                <input type="radio" id="descOrder" name="sortOrder" value="DESCENDING" ${"DESCENDING" == requestScope.sortOrder.toString() ? 'checked' : ''}>
                                <label for="descOrder">Descending</label>
                            </td>
                        </tr>
                        <tr>
                            <td></td>
                            <td>
                                <button class="center" type="submit" >Submit</button>
                            </td>
                        </tr>
                    </table>

                </form>

                <table  class="js-sort-table" >
                <thead>
                    <tr>
                        <th class="js-sort-string">In/Out</th>
                        <th class="js-sort-number">Number from</th>
                        <th class="js-sort-number">Number to</th>
                        <th class="js-sort-number">Amount</th>
                        <th class="js-sort-date">Date</th>
                        <th class="js-sort-string">Status</th>
                    </tr>
                </thead>
                    <c:forEach var="item" items="${requestScope.payments}">
                            <c:if test="${item.from.cardID==sessionScope.currCard.cardID}">
                                <tr class="outgoing center">
                                    <td>outgoing</td>
                                    <td>${item.from.number}</td>
                                    <td>${item.to.number}</td>
                                    <td>${item.amount}</td>
                                    <td>${item.textDateTime}</td>
                                    <td>${item.status.toString()}</td>
                                </tr>
                            </c:if>
                            <c:if test="${item.to.cardID==sessionScope.currCard.cardID}">
                                <tr class="incoming center" >
                                    <td>incoming</td>
                                    <td>${item.from.number}</td>
                                    <td>${item.to.number}</td>
                                    <td>${item.amount}</td>
                                    <td>${item.textDateTime}</td>
                                    <td>${item.status.toString()}</td>
                                </tr>
                            </c:if>
                    </c:forEach>
                </table>
                <a href="controller?command=makePayment">Make a payment</a>
            </td>
        </tr>
        <script src="${pageContext.request.contextPath}/sort-table.js"></script>

    </table>
    <hr>
</body>
</html>