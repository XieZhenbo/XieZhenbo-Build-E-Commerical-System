<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>${requestScope.item_focus.getName()}</title>
<link rel="stylesheet" type="text/css" href="css/styling.css" />
</head>
<body>
<form>
	<jsp:include page="banner.jsp"/>
	<div class="item_focus">
		<img src="images/${requestScope.item_focus.getItemID()}.jpeg" class="film_large"/>
	</div>
	<input type="submit" name="action" value="Add ${requestScope.item_focus.getName()} to Cart" />
		<p><b>Title: </b>${requestScope.item_focus.getName()}</p>
			<p><b>Brand: </b>${requestScope.item_focus.getBrand()}</p>
			<p><b>Category: </b>${requestScope.item_focus.getCategory()}</p>
			<p><b>Stock: </b>${requestScope.item_focus.getQuantity()}</p>
			<p><b>Price: $</b>${requestScope.item_focus.getPrice()}</p>
			<p><b>Description: </b>${requestScope.item_focus.getDescription()}</p>
</form>
</body>
</html>