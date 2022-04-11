<%@include file="/WEB-INF/jspf/page.jspf"%>
<%@include file="/WEB-INF/jspf/tags.jspf"%>
<%@ page import="com.my.payment.db.Status" %>
<%@ page import="com.my.payment.constants.Path" %>
<html>
<c:set var="title" value="Your cards" scope="page"/>
<%@ include file="/WEB-INF/jspf/head.jspf"%>
<body>
    <table id="main-container">
        <%@ include file="/WEB-INF/jspf/changeLocale.jspf"%>
        <%@include file="/WEB-INF/jspf/header.jspf"%>
        <tr>
            <td class="content">
                <table class="table table-striped table-bordered" id = "mydatatable" >
                    <thead>
                    <tr>
                        <th><fmt:message key='card.name'/></th>
                        <th><fmt:message key='card.number'/></th>
                        <th><fmt:message key='card.balance'/></th>
                        <th><fmt:message key='card.date'/></th>
                        <th>CVV</th>
                        <th><fmt:message key='label.status'/></th>
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
                            <td><nf:numberFormat number="3">${item.cvv}</nf:numberFormat></td>
                            <td>
                               <c:if test="${item.status==Status.BLOCKED}">
                                   <fmt:message key='card.status.blocked'/>
                               </c:if>
                                <c:if test="${item.status==Status.ACTIVE}">
                                    <fmt:message key='card.status.active'/>
                                </c:if>
                                <c:if test="${item.status==Status.UNBLOCK_REQUEST}">
                                    <fmt:message key='card.status.unblockRequest'/>
                                </c:if>
                            </td>
                            <td>
                                <form action="controller" method="post">
                                    <input type="hidden" name="command" value="changeCardStatus">
                                    <input type="hidden" name="cardID" value="${item.cardID}">
                                    <c:choose>
                                        <c:when test="${item.status == Status.ACTIVE}">
                                            <input type="hidden" name="newStatus" value="${Status.BLOCKED}">
                                            <button type="submit"><fmt:message key='card.status.block'/></button>
                                        </c:when>
                                        <c:when test="${item.status == Status.BLOCKED}">
                                            <c:if test="${sessionScope.userRole == Role.USER}">
                                                <input type="hidden" name="newStatus" value="${Status.UNBLOCK_REQUEST}">
                                                <button type="submit"><fmt:message key='card.status.unblock'/></button>
                                            </c:if>
                                            <c:if test="${sessionScope.userRole == Role.ADMIN}">
                                                <input type="hidden" name="newStatus" value="${Status.ACTIVE}">
                                                <button type="submit"><fmt:message key='card.status.unblock'/></button>
                                            </c:if>
                                        </c:when>
                                        <c:when test="${item.status == Status.UNBLOCK_REQUEST}">
                                            <c:if test="${sessionScope.userRole == Role.ADMIN}">
                                                <input type="hidden" name="newStatus" value="${Status.ACTIVE}">
                                                <button type="submit"><fmt:message key='card.status.unblock'/></button>
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
                    <a href="${Path.NEW_CARD_PAGE}"><fmt:message key='card.add'/></a><br>
                    <a href="${Path.CREATE_CARD_PAGE}"><fmt:message key='card.create'/></a>
                </c:if>
                <c:if test="${sessionScope.userRole==Role.USER}">
                    <hr>
                    <table id = "mydatatable2" class="table table-striped table-bordered">
                        <thead>
                        <tr>
                            <th><fmt:message key='header.from'/></th>
                            <th><fmt:message key='header.to'/></th>
                            <th><fmt:message key='card.amount'/></th>
                            <th><fmt:message key='header.date'/></th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="item" items="${requestScope.allPayments}">
                            <tr>
                                <td>${item.from.number}</td>
                                <td>${item.to.number}</td>
                                <td>${item.amount}</td>
                                <td>${item.textDateTime}</td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </c:if>
            </td>
        </tr>
    </table>
    <hr>
    <%@ include file="/WEB-INF/jspf/scripts.jspf"%>
</body>
</html>