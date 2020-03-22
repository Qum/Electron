<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
	<title>FFFF</title>
</head>
<body>
<li><p>Greetings </p>   <br><br>  </li>
<br>
<br>
<ul>
	<c:forEach var="re" items="${res}">
		<li>${re}</li>
<%--		<c:out value="${item}" />--%>
	</c:forEach>
</ul>
</body>
</html>
