This is Spring demo from Udemy Spring & Hibernate course. It includes 
- Spring MVC basic
- Config process:
	- Add configurations to file: WEB-INF/web.xml
		1. Configure Spring MVC Dispatcher Servlet
		2. Set up URL mappings to Spring MVC Dispatcher Servlet
	- Add configurations to file: WEB-INF/spring-mvc-demo-servlet.xml
		1. Add support for Spring component scanning
		2. Add support for conversion, formatting and validation
		3. Configure Spring MVC View Resolver

### Create Eclipse Dynamic Web project
- Use Eclipse JavaEE perspective
- Dynamic web module version - 3.0 (4.0 is available)
- Change source folder on build path - remove src/main/java and add src/
- Change Content directory - change src/main/webapp to WebContent
- WEB-INF/lib directory  - copy the spring framework lib jars to this directory	
	- jars within the WEB-INF/lib  directory are added to the Java Build Path (Classpath) automatically.
	- additional jars - javax.servlet.jsp.jstl-1.2.1.jar, javax.servlet.jsp.jstl-api-1.2.1.jar
- copy web.xml and spring-mvc-demo-servlet.xml into WEB-INF directory
- <context:component-scan base-package="xxx"> ensure to use the correct package. It will recursive scan sub directories.
- create the 'view' directory inside WEB-INF to place view files

### mvc:annotation-driven tag 
reference - https://spring.io/blog/2009/12/21/mvc-simplifications-in-spring-3-0/
A tag added in Spring 3.0 which does the following:
- Configures the Spring 3 Type ConversionService (alternative to PropertyEditors)
- Adds support for formatting Number fields with @NumberFormat
- Adds support for formatting Date, Calendar, and Joda Time fields with @DateTimeFormat, if Joda Time is on the classpath
- Adds support for validating @Controller inputs with @Valid, if a JSR-303 Provider is on the classpath
- Adds support for support for reading and writing XML, if JAXB is on the classpath (HTTP message conversion with @RequestBody/@ResponseBody)
- Adds support for reading and writing JSON, if Jackson is on the classpath (along the same lines as #5)

### Create Controller class
- use @Controller annotation on the class
	- @Controller inherits from @Component, so it supports component scan
- use @RequestMapping on methods - it specifies a path
	- the method returns a string for view name 
	- view name use the View Resolver to get the actual view file path and name

### Run on server
- Use Tomcat version 9 server
- Some errors occur, and change Tomcat Server ports to make it work, 
	- admin port - 8004
	- HTTP/1.1 - 8081