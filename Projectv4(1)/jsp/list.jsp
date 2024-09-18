<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<head>
<script src="js/home.js"></script>
</head>
<br>
Brand:
<select id="brandSelect" name="brand">
	<option value=""></option>
	<c:forEach items="${brandList}" var="brand">
		<c:choose>
			<c:when test="${brand == selectedBrand}">
				<option value="${brand}" selected>${brand}</option>
			</c:when>
			<c:otherwise>
				<option value="${brand}">${brand}</option>
			</c:otherwise>
		</c:choose>
	</c:forEach>
</select>

<text style="margin-left: 50px;">Category:</text>
<select id="categorySelect" name="category">
	<option value=""></option>
	<c:forEach items="${categoryList}" var="category">
		<c:choose>
			<c:when test="${category == selectedCategory}">
					<option value="${category}" selected>${category}</option>
			</c:when>
			<c:otherwise>
					<option value="${category}">${category}</option>
			</c:otherwise>
		</c:choose>
	</c:forEach>
</select>
<input type="submit" name="action" value="Filter"
	style="margin-left: 20px;" />

<br>
<br>

<div class="film_table">

	<c:forEach items="${filmList}" var="item">

		<div class="film_row">

			<div class="film_cell">
				<img src="images/${item.getItemID()}.jpeg" class="film"
					name="${item.getName()}" />
				<text class="price">${item.getPrice()}</text>
				<text>${item.getName()}</text>
			</div>

		</div>
	</c:forEach>
</div>

