<%@ include file="/WEB-INF/jspf/page.jspf" %>
<%@ include file="/WEB-INF/jspf/tags.jspf"%>
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
			<c:if test="${not empty requestScope.emailVal}">
				${requestScope.emailVal}
				<br>
			</c:if>
			<c:if test="${not empty requestScope.loginVal}">
				${requestScope.loginVal}
				<br>
			</c:if>
			<c:if test="${not empty requestScope.passVal}">
				${requestScope.passVal}
				<br>
			</c:if>
			<c:if test="${not empty requestScope.repeatPVal}">
				${requestScope.repeatPVal}
				<br>
			</c:if>
			<c:if test="${not empty requestScope.loginExist}">
				${requestScope.loginExist}
				<br>
			</c:if>
            <c:if test="${not empty requestScope.isSuccess}">
                ${requestScope.isSuccess}
                <br>
            </c:if>
		</div>
	  	<div class="container">
			<h1 >Sign up</h1>
			<hr>
			<label for="email"><b>Email*</b></label>
			<input type="text" placeholder="Enter email" name="email" id="email" required maxlength="45"
			pattern="[\w\-\.]+@([\w-]+\.)+[\w-]{2,4}$">

			<label for="Login"><b>Login*</b></label>
			<input type="text" placeholder="Enter login" name="login" id="login" required maxlength="20"
			pattern="^[\w_-]{3,20}$">
			<script src="myScript.js"></script>
			<label for="pass"><b>Password*</b></label>
			<input type="password" placeholder="Enter password" name="pass" id="pass" required minlength="6" maxlength="45" onkeyup='check();'>

			<label for="pass-repeat"><b>Repeat Password*</b></label>
			<input type="password" placeholder="Repeat password" name="pass-repeat" id="pass-repeat" required minlength="6" maxlength="45" onkeyup='check();'>
			<span id='message'></span>
			<hr>

			<button type="submit" class="mybtn">Register</button>
	 	 </div>
		<p>Already have an account? <a class="center" href="loginPage.jsp">Sign in</a>.</p>
    </form>
		</td>
	</tr>
	<%@include file="/WEB-INF/jspf/footer.jspf"%>
</table>
</body>
</html>