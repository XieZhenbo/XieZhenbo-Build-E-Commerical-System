<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<jsp:include page="banner.jsp"/>
	<h3>Your Cart:</h3>
	
	<table>
		<tr>
			<th>Item</th>
			<th>Quantity</th>
			<th>Price</th>
		</tr>
		<c:forEach items="${cart}" var="item">
			<tr>
				<td>${item.getFilmName()}</td>
				<td>${item.getQuantity()}</td>
				<td>${item.getPrice()}</td>
			</tr>
		</c:forEach>
	</table>
	
	
</body>
</html>