<%@ include file="/WEB-INF/jspf/page.jspf" %>
<%@ include file="/WEB-INF/jspf/tags.jspf"%>
<%@ page import="com.my.payment.db.PaymentStatus" %>
<!DOCTYPE html>
<html>
<c:set var="title" value="Make payment" scope="page" />
<%@include file="/WEB-INF/jspf/head.jspf"%>
<body>
<table id ="main-container">
    <%@include file="/WEB-INF/jspf/header.jspf"%>
    <tr>
        <td class="content">
            <c:if test="${not empty requestScope.invalidCardToNumber}">
                ${requestScope.invalidCardToNumber}
                <br>
            </c:if>
            <c:if test="${not empty requestScope.invalidAmount}">
                ${requestScope.invalidAmount}
                <br>
            </c:if>
            <c:if test="${not empty requestScope.anotherCard}">
                ${requestScope.anotherCard}
                <br>
            </c:if>
            <form action="controller" method="post">
                <input type="hidden" name="fromID" value="${sessionScope.currCard.cardID}"/>
                <input type="hidden" name="command" value="makePayment"/>
                <div class="container">
                    <h1 >Make payment</h1>
                    <hr>
                    <label for="cardNumberFrom"><b>From*</b></label>
                    <input type="text" readonly name="cardNumberFrom" id="cardNumberFrom" required maxlength="16" minlength="16" value="${sessionScope.currCard.number}">

                    <script src="myScript.js"></script>
                    <label for="cardNumberTo"><b>To*</b></label>
                    <input type="text" placeholder="Enter card number" name="cardNumberTo" id="cardNumberTo" required maxlength="16" minlength="16" onkeypress="return onlyNumberKey(event)">

                    <label for="amount"><b>Amount*</b></label>
                    <input type="number" name="amount" id="amount" required step="0.01" min="1" max="999999999">

                    <input type="radio" name="prepareOrSend" id="prepare" value="${PaymentStatus.PREPARED}" checked>
                    <label for="prepare">Prepare payment</label>
                    <input type="radio" name="prepareOrSend" id="send" value="${PaymentStatus.SENT}">
                    <label for="send">Send payment</label>

                    <hr>
                    <button type="submit" class="mybtn">Commit</button>
                </div>
            </form>
        </td>
    </tr>

</table>
</body>
</html>