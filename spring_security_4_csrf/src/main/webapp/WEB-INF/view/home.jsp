<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<!doctype html>
<head>
<title>Home Page</title>
</head>
<body>
	<h2>My home page - Spring MVC Security Demo</h2>

	<form:form action="${pageContext.request.contextPath}/logout"
		method="POST">
		<input type="submit" value="Logout" />
	</form:form>
</body>
</html>