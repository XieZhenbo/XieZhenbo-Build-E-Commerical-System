<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<form>
	<jsp:include page="banner.jsp"/>
	<h3>Sign-in:</h3>
	<br>
	<p>E-mail</p>
	<input type="text" name="email"/>
	<br>
	<p>Password</p>
	<input type="password" name="password"/>
	<br>
	<input type="submit" name="action" value="Sign-in">
</form>
</body>
</html>