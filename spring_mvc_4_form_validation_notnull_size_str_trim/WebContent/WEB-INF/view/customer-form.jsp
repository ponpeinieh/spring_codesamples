
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html>
<head>
<title>Customer - registration form</title>
<style>
.error {
	color: red
}
</style>
</head>
<body>
	<i>Fill out the form please, asterisk(*) is required</i>
	<form:form action="processForm" modelAttribute="customer">
 First name: <form:input path="firstName" />
		<br />
 Last name(*): <form:input path="lastName" />
		<form:errors path="lastName" cssClass="error"></form:errors>
		<br />

		<input type="submit" />
	</form:form>
</body>
</html>