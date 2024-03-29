<%@ include file="/WEB-INF/jspf/page.jspf" %>
<%@ include file="/WEB-INF/jspf/tags.jspf" %>
<%@ page import="com.my.payment.constants.Path" %>
<!DOCTYPE html>
<html>
<c:set var="title" scope="page" value="Sign up"/>
<%@include file="/WEB-INF/jspf/head.jspf" %>
<body>
<div class="limit">
    <%@ include file="/WEB-INF/jspf/changeLocale.jspf" %>
    <div class="login-form">
        <form action="controller" method="post">
            <div>
                <input type="hidden" name="command" value="registration"/>
                <c:if test="${not empty sessionScope.emailVal}">
                    ${sessionScope.emailVal}
                    <br>
                </c:if>
                <c:if test="${not empty sessionScope.loginVal}">
                    ${sessionScope.loginVal}
                    <br>
                </c:if>
                <c:if test="${not empty sessionScope.passVal}">
                    ${sessionScope.passVal}
                    <br>
                </c:if>
                <c:if test="${not empty sessionScope.repeatPVal}">
                    ${sessionScope.repeatPVal}
                    <br>
                </c:if>
                <c:if test="${not empty sessionScope.loginExist}">
                    ${sessionScope.loginExist}
                    <br>
                </c:if>
                <c:if test="${not empty sessionScope.isSuccess}">
                    ${sessionScope.isSuccess}
                    <br>
                </c:if>
                <c:if test="${not empty sessionScope.invalidDate}">
                    ${sessionScope.invalidDate}
                    <br>
                </c:if>
            </div>
            <h1><fmt:message key="signUp.title"/></h1>
            <hr>
            <label for="email"><b><fmt:message key="label.email"/>*</b></label>
            <input type="text" name="email" id="email" required maxlength="45"
                   pattern="[\w\-\.]+@([\w-]+\.)+[\w-]{2,4}$">

            <label for="fullName"><b><fmt:message key="label.fullName"/>*</b></label>
            <input type="text" name="fullName" id="fullName" required maxlength="80" minlength="1">

            <p><label for="birth"><b><fmt:message key="label.birth"/>*</b></label></p>
            <p><input type="date" name="birth" id="birth" required></p>

            <label for="login"><b><fmt:message key="label.login"/>* <fmt:message
                    key="label.login.spec"/></b></label>
            <input type="text" name="login" id="login" required maxlength="20"
                   pattern="^[\w_-]{3,20}$">
            <label for="pass"><b><fmt:message key="label.password"/>* </b></label>
            <input type="password" name="pass" id="pass" required minlength="6" maxlength="45" onkeyup='check();'>

            <label for="pass-repeat"><b><fmt:message key="label.password.repeat"/>*</b></label>
            <input type="password" name="pass-repeat" id="pass-repeat" required minlength="6" maxlength="45"
                   onkeyup='check();'>
            <span id='message'></span>
            <hr>
            <div class="mybtn-wrapper">
                <button type="submit" class="mybtn"><fmt:message key="signUp.createAccount"/></button>
            </div>
            <p><fmt:message key="signUp.alreadyHaveAccount"/> <a class="center" href="${Path.LOGIN_PAGE}"><fmt:message
                    key="signUP.signIn"/></a>.</p>
        </form>
        <c:remove var="isSuccess" scope="session"/>
        <c:remove var="loginExist" scope="session"/>
        <c:remove var="repeatPVal" scope="session"/>
        <c:remove var="passVal" scope="session"/>
        <c:remove var="loginVal" scope="session"/>
        <c:remove var="emailVal" scope="session"/>
        <c:remove var="newUser" scope="session"/>
        <c:remove var="mailType" scope="session"/>
        <c:remove var="invalidDate" scope="session"/>
    </div>
</div>
<script src="myScript.js"></script>
</body>
</html>