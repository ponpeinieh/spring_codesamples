 <%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
 <html>
<head>
<title>Student - registration form</title>
</head>
<body>
 <form:form action="processForm" modelAttribute="student">
 First name: <form:input path="firstName"/>
 <br/>
 Last name: <form:input path="lastName"/>
 <br/>
 Country: <form:select path="country">
 <form:options items="${student.countryOptions}"/>
 <!--
 <form:option value="China" label="China"/>
 <form:option value="Malaysia" label="Malaysia"/>
 <form:option value="Indonisia" label="Indonisia"/>
   -->
 </form:select>
 <br/>
 <input type="submit"/>
 
 </form:form>
</body></html>