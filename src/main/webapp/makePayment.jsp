<%@ include file="/WEB-INF/jspf/page.jspf" %>
<%@ include file="/WEB-INF/jspf/tags.jspf"%>
<%@ page import="com.my.payment.db.PaymentStatus" %>
<!DOCTYPE html>
<html>
<c:set var="title" value="Make payment" scope="page" />
<%@include file="/WEB-INF/jspf/head.jspf"%>
<body>
<table id ="main-container">
    <%@ include file="/WEB-INF/jspf/changeLocale.jspf"%>
    <%@include file="/WEB-INF/jspf/header.jspf"%>
    <tr>
        <td class="content">
            <c:if test="${not empty sessionScope.invalidCardToNumber}">
                ${sessionScope.invalidCardToNumber}
                <br>
            </c:if>
            <c:if test="${not empty sessionScope.invalidAmount}">
                ${sessionScope.invalidAmount}
                <br>
            </c:if>
            <c:if test="${not empty sessionScope.anotherCard}">
                ${sessionScope.anotherCard}
                <br>
            </c:if>
            <form action="controller" method="post">
                <input type="hidden" name="fromID" value="${sessionScope.currCard.cardID}"/>
                <input type="hidden" name="command" value="makePayment"/>
                <h1 ><fmt:message key='label.makePayment'/></h1>
                <hr>
                <label for="cardNumberFrom"><b><fmt:message key='header.from'/>*</b></label>
                <input type="text" readonly name="cardNumberFrom" id="cardNumberFrom" required maxlength="16" minlength="16" value="${sessionScope.currCard.number}">

                <script src="myScript.js"></script>
                <label for="cardNumberTo"><b><fmt:message key='header.to'/>*</b></label>
                <input type="text" name="cardNumberTo" id="cardNumberTo" required maxlength="16" minlength="16" onkeypress="return onlyNumberKey(event)">

                <label for="amount"><b><fmt:message key='card.amount'/>*</b></label>
                <input type="number" name="amount" id="amount" required step="0.01" min="1" max="999999999">

                <input type="radio" name="prepareOrSend" id="prepare" value="${PaymentStatus.PREPARED}" checked>
                <label for="prepare"><fmt:message key='payment.prepare'/></label>
                <input type="radio" name="prepareOrSend" id="send" value="${PaymentStatus.SENT}">
                <label for="send"><fmt:message key='payment.sent'/></label>
                <hr>
                <button type="submit" class="mybtn"><fmt:message key='payment.commit'/></button>
            </form>
            <c:remove var="anotherCard" scope="session"/>
            <c:remove var="invalidAmount" scope="session"/>
            <c:remove var="invalidCardToNumber" scope="session"/>
        </td>
    </tr>

</table>
</body>
</html>