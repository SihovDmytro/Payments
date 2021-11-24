<%@include file="/WEB-INF/jspf/page.jspf"%>
<%@include file="/WEB-INF/jspf/tags.jspf"%>
<%@ page import="com.my.payment.db.Status" %>
<html>
<c:set var="title" value="Your cards" scope="page"/>
<%@ include file="/WEB-INF/jspf/head.jspf"%>
<body>
    <table id="main-container">
        <%@include file="/WEB-INF/jspf/header.jspf"%>
        <tr>
            <td class="content">
                <table class="table table-striped table-bordered" id = "mydatatable" >
                    <thead>
                    <tr>
                        <th>Name</th>
                        <th>Number</th>
                        <th>Balance</th>
                        <th>Expiration date</th>
                        <th >CVV</th>
                        <th >Status</th>
                        <th></th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="item" items="${requestScope.listCards}">
                        <tr>
                            <td>${item.name}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${item.status == Status.ACTIVE or sessionScope.userRole==Role.ADMIN}">
                                        <a href="controller?command=getPayments&cardItem=${item.cardID}">${item.number}</a>
                                    </c:when>
                                    <c:otherwise>
                                        ${item.number}
                                    </c:otherwise>
                                </c:choose>
                            </td>
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
                    </tbody>
                </table>
                <c:if test="${sessionScope.userRole == Role.USER}">
                    <a href="controller?command=getNewCardPage">Add new card</a>
                </c:if>
            </td>
        </tr>
    </table>
    <hr>
    <%@ include file="/WEB-INF/jspf/scripts.jspf"%>
</body>
</html>