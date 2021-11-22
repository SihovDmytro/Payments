<%@include file="/WEB-INF/jspf/page.jspf"%>
<%@include file="/WEB-INF/jspf/tags.jspf"%>
<%@ page import="com.my.payment.db.Status" %>
<fmt:setBundle basename="localization_en_US"/>
<html>
<c:set var="title" value="Your cards" scope="page"/>
<%@ include file="/WEB-INF/jspf/head.jspf"%>
<body>
    <table id="main-container">
        <%@include file="/WEB-INF/jspf/header.jspf"%>
        <tr>
            <td class="content">
                <table class="js-sort-table">
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
                                <form action="controller" method="post">
                                    <input type="hidden" name="command" value="changeCardStatus">
                                    <input type="hidden" name="cardID" value="${item.cardID}">
                                    <c:choose>
                                        <c:when test="${item.status == Status.ACTIVE}">
                                            <input type="hidden" name="newStatus" value="${Status.BLOCKED}">
                                            <button type="submit">Block the card</button>
                                        </c:when>
                                        <c:when test="${item.status == Status.BLOCKED}">
                                            <c:if test="${sessionScope.userRole == Role.USER}">
                                                <input type="hidden" name="newStatus" value="${Status.UNBLOCK_REQUEST}">
                                                <button type="submit">Unblock card</button>
                                            </c:if>
                                            <c:if test="${sessionScope.userRole == Role.ADMIN}">
                                                <input type="hidden" name="newStatus" value="${Status.ACTIVE}">
                                                <button type="submit">Unblock card</button>
                                            </c:if>
                                        </c:when>
                                        <c:when test="${item.status == Status.UNBLOCK_REQUEST}">
                                            <c:if test="${sessionScope.userRole == Role.USER}">
                                                Unblock request is sent
                                            </c:if>
                                            <c:if test="${sessionScope.userRole == Role.ADMIN}">
                                                <input type="hidden" name="newStatus" value="${Status.ACTIVE}">
                                                <button type="submit">Unblock card</button>
                                            </c:if>
                                        </c:when>
                                    </c:choose>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                </table>
                <c:if test="${sessionScope.userRole == Role.USER}">
                    <a href="controller?command=getNewCardPage">Add new card</a>
                </c:if>
            </td>
        </tr>
        <script src="${pageContext.request.contextPath}/sort-table.js"></script>

    </table>
    <hr>
</body>
</html>