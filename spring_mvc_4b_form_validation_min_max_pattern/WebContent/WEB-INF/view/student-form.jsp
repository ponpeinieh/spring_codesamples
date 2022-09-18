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
 </form:select>
 <br/>
 Favorite language: 
<form:radiobuttons path="favoriteLanguage" items="${student.languageOptions}"/>

<%--  Java <form:radiobutton path="favoriteLanguage" value="Java"/> --%>
<%--  C# <form:radiobutton path="favoriteLanguage" value="C#"/> --%>
<%--  Python <form:radiobutton path="favoriteLanguage" value="Python"/> --%>
<%--  C++ <form:radiobutton path="favoriteLanguage" value="C++"/> --%>
<%--  Ruby <form:radiobutton path="favoriteLanguage" value="Ruby"/> --%>
  <br/>
   Operation Systems: 
   <form:checkboxes items="${student.operatingSystemOptions}" path="operatingSystems"/>
<%--  Windows <form:checkbox path="operatingSystems" value="Windows"/> --%>
<%--  Linux <form:checkbox path="operatingSystems" value="Linux"/> --%>
<%--  Mac OS <form:checkbox path="operatingSystems" value="macOS"/> --%>
  
 <input type="submit"/>

 </form:form>
</body></html>