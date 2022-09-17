<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<title>Student - confirmation</title>
</head>
<body>

	First name: ${student.firstName}
	<br /> Last name: ${student.lastName}
	<br /> Country: ${student.country}
	<br /> Favorite language: ${student.favoriteLanguage}
	<br /> Operation Systems:

	<ul>
		<c:forEach var="temp" items="${student.operatingSystems}">
			<li>${temp}</li>
		</c:forEach>
	</ul>

</body>
</html>