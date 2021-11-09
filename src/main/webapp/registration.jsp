<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
	<title>Sign up</title>
	<link href="myStyle.css" rel = "stylesheet" type = "">
</head>
<body>
	<form action="register" method="post">
		<div>
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
		<h1 >Register</h1>
		<hr>
		<label for="email"><b>Email</b></label>
		<input type="text" placeholder="Enter email" name="email" id="email" required>

		<label for="Login"><b>Login</b></label>
		<input type="text" placeholder="Enter login" name="login" id="login" required>

		<label for="pass"><b>Password</b></label>
		<input type="password" placeholder="Enter password" name="pass" id="pass" required>

		<label for="pass-repeat"><b>Repeat Password</b></label>
		<input type="password" placeholder="Repeat password" name="pass-repeat" id="pass-repeat" required>
		<hr>

		<button type="submit" class="registerbtn">Register</button>
	  </div>

	  <div class="container signup">
		<p>Already have an account? <a href="loginPage.jsp">Sign up</a>.</p>
	  </div>
    </form>
</body>
</html>