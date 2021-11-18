<%@ include file="/WEB-INF/jspf/page.jspf" %>
<%@ include file="/WEB-INF/jspf/tags.jspf"%>
<!DOCTYPE html>
<html>
<c:set var="title" value="Make payment" scope="page" />
<%@include file="/WEB-INF/jspf/head.jspf"%>
<body>
<table id ="main-container">
    <%@include file="/WEB-INF/jspf/header.jspf"%>
    <tr>
        <td class="content">
            <form action="controller" method="post">

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
                <input type="hidden" name="command" value="commitPayment"/>
                <input type="hidden" name="fromID" value="${requestScope.fromCard.cardID}"/>
                <div class="container">
                    <h1 >Make payment</h1>
                    <hr>
                    <label for="cardNumberFrom"><b>From*</b></label>
                    <input type="text" readonly name="cardNumberFrom" id="cardNumberFrom" required maxlength="16" minlength="16" value="${requestScope.fromCard.number}">

                    <script src="${pageContext.request.contextPath}/myScript.js"></script>
                    <label for="cardNumberTo"><b>To*</b></label>
                    <input type="text" placeholder="Enter card number" name="cardNumberTo" id="cardNumberTo" required maxlength="16" minlength="16" onkeypress="return onlyNumberKey(event)">

                    <label for="amount"><b>Amount*</b></label>
                    <input type="number" name="amount" id="amount" required step="0.01" min="1" max="999999999">

                    <hr>
                    <button type="submit" class="mybtn">Commit</button>
                </div>
            </form>
        </td>
    </tr>
    <%@include file="/WEB-INF/jspf/footer.jspf"%>
</table>
</body>
</html>