<%@ include file="/WEB-INF/jspf/page.jspf" %>
<%@ include file="/WEB-INF/jspf/tags.jspf"%>
<%@ page import="com.my.payment.constants.Path" %>
<!DOCTYPE html>
<html>
<c:set var="title" value="Sign up" scope="page" />
<%@include file="/WEB-INF/jspf/head.jspf"%>
<body>
<table id ="main-container">
	<tr>
		<td class="content">
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
			</div>
			<div class="container">
				<h1 >Sign up</h1>
				<hr>
				<label for="email"><b>Email*</b></label>
				<input type="text" placeholder="Enter email" name="email" id="email" required maxlength="45"
				pattern="[\w\-\.]+@([\w-]+\.)+[\w-]{2,4}$">

				<label for="Login"><b>Login* (at least 3 letters of the Latin alphabet or symbols: '-', '_')</b></label>
				<input type="text" placeholder="Enter unique login" name="login" id="login" required maxlength="20"
				pattern="^[\w_-]{3,20}$">
				<script src="myScript.js"></script>
				<label for="pass"><b>Password* </b></label>
				<input type="password" placeholder="Enter password" name="pass" id="pass" required minlength="6" maxlength="45" onkeyup='check();'>

				<label for="pass-repeat"><b>Repeat Password*</b></label>
				<input type="password" placeholder="Repeat password" name="pass-repeat" id="pass-repeat" required minlength="6" maxlength="45" onkeyup='check();'>
				<span id='message'></span>
				<hr>

				<button type="submit" class="mybtn">Register</button>
			 </div>
			<p>Already have an account? <a class="center" href="${Path.LOGIN_PAGE}">Sign in</a>.</p>
		</form>
		<c:remove var="isSuccess" scope="session"/>
		<c:remove var="loginExist" scope="session"/>
		<c:remove var="repeatPVal" scope="session"/>
		<c:remove var="passVal" scope="session"/>
		<c:remove var="loginVal" scope="session"/>
		<c:remove var="emailVal" scope="session"/>
		</td>
	</tr>

</table>
</body>
</html>