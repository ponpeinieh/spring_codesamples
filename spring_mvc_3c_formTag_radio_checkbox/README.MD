This is Spring demo from Udemy Spring & Hibernate course. It includes 
- spring form tag - radiobutton and checkbox

### radiobuttons
	- use form:radiobutton to specify each radio button
	- the path is the attribute to set for this radio button
```
Favorite language: 
Java <form:radiobutton path="favoriteLanguage" value="Java"/>  
C# <form:radiobutton path="favoriteLanguage" value="C#"/>  
Python <form:radiobutton path="favoriteLanguage" value="Python"/>  
C++ <form:radiobutton path="favoriteLanguage" value="C++"/> 
Ruby <form:radiobutton path="favoriteLanguage" value="Ruby"/>  
```
	- or use form:radiobuttons to create a collection of radio buttons
```
Favorite language: 
<form:radiobuttons path="favoriteLanguage" items="${student.languageOptions}"/> 
```

	- Student bean
		- getter/setter for favoriteLanguage
		- getter for languageOptions (a Map)
```
private String favoriteLanguage;
private Map<String, String> languageOptions;
public Student() {
	languageOptions = new HashMap<String, String>();
	languageOptions.put("Java", "Java");
	languageOptions.put("C++", "C++");
	languageOptions.put("Dart", "Dart");
	languageOptions.put("Lua", "Lua");
	//...
}

public Map<String, String> getLanguageOptions() {
	return languageOptions;
}

public String getFavoriteLanguage() {
	return favoriteLanguage;
}

public void setFavoriteLanguage(String favoriteLanguage) {
	this.favoriteLanguage = favoriteLanguage;
}
```

### checkboxes
	- similar to radiobutton, but allow multiple choices, so need a String[] to store the attribute
	- use <form:checkbox> for each checkbox

```
Operation Systems: 
Windows <form:checkbox path="operatingSystems" value="Windows"/> 
Linux <form:checkbox path="operatingSystems" value="Linux"/> 
Mac OS <form:checkbox path="operatingSystems" value="macOS"/> 
```
	- or use <form:checkboxes> for the collection of checkboxes
```
Operation Systems: 
<form:checkboxes items="${student.operatingSystemOptions}" path="operatingSystems"/>
```
	- Student bean
```
private Map<String, String> operatingSystemOptions;
private String[] operatingSystems;
public Student() {
	operatingSystemOptions = new HashMap<String, String>();
	operatingSystemOptions.put("Windows", "Windows");
	operatingSystemOptions.put("Linux", "Linux");
	operatingSystemOptions.put("FreeBSD", "FreeBSD");
	operatingSystemOptions.put("MacOS", "MacOS");
}

public Map<String, String> getOperatingSystemOptions() {
	return operatingSystemOptions;
}

public String[] getOperatingSystems() {
	return operatingSystems;
}

public void setOperatingSystems(String[] operatingSystems) {
	this.operatingSystems = operatingSystems;
}
```
	- result jsp page - use a forEach tag to show the checkbox selections
		- here use jstl taglib
```
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
```