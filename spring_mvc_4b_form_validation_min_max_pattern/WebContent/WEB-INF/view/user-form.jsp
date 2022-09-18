 <%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
 <html>
<head>
<title>User - input form</title>
</head>
<body>
 <form:form action="processForm" >
 First name: <form:input path="firstName"/>
 <br/>
 Last name: <form:input path="lastName"/>
 <br/>
 <input type="submit"/>
 
 </form:form>
</body></html>