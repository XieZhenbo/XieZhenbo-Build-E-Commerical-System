<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Banner</title>
<link rel="stylesheet" type="text/css" href="css/styling.css" />
</head>
<body>
<div class="banner">
	<a href="http://localhost:8080/Projectv4">Home</a>
	<c:if test="${not empty sessionScope.account}">
		<p>Account: ${sessionScope.account.getFirstName()} ${sessionScope.account.getLastName()} </p>
	</c:if>
	<c:if test="${empty sessionScope.account}">
		<p>Account: none</p>
	</c:if>
	<input type="submit" name="action" value="Register an account" />
	<input type="submit" name="action" value="Sign-in to your account" />
	<input type="submit" name="action" value="Sign-out of your account" />
	<input type="submit" name="action" value="View Cart" />
	<input type="submit" name="action" value="Sort items by prices ${priceSort}" />
	<input type="submit" name="action" value="Sort items by names ${nameSort}" />
</div>
</body>
</html>