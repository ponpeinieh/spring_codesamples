This is Spring demo from Udemy Spring & Hibernate course. It includes 
- Spring MVC basic
- Use static resources, eg. css, javascript, images etc
- Since our web content root directory is in `WebContent` directory, we create a `resources` subdirectory within it, and put css, js, images static resources within that `resources` directory.
- add the following mapping in the spring mvc config file
	- <mvc:resources mapping="/resources/**" location="/resources/"></mvc:resources> 
- use the resource in the view file, eg
	- <img src="${pageContext.request.contextPath}/resources/images/spring-logo.png">
	- use the JSP expression `${pageContext.request.contextPath}` to access the correct root directory for your web application.