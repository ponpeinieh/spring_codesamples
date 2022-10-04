This is Spring demo from Udemy Spring & Hibernate course.  
- Spring Boot
	- Spring Boot Actuator
		- The Problem to solve:
			- How can I monitor and manage my application?
			- How can I check the application health?
			- How can I access application metrics
		-  Exposes endpoints to monitor and manage your application
			- You easily get DevOps functionality out-of-the-box
			- Simply add the dependency to your POM file
			- **REST endpoints are automatically added to your application**

			```
			<dependency>
				<groupId>org.springframework.boot</groupId>
				<artifactId>spring-boot-starter-actuator</artifactId>
			</dependency>
			```
		
		- Endpoints are prefixed with: `/actuator`
			- `/health` : health info about your app
			- `/info` : info about your project
			- Note that `/info` is NOT exposed by default
				- You have to manually expose it and enable custom app info properties (in `application.properties` file)

				```
				management.endpoints.web.exposure.include=health,info
				management.info.env.enabled=true

				info.app.name=My Super Cool App
				info.app.description=A crazy and fun app, yoohoo!
				info.app.version=1.0.0
				```
			
			- To expose all actuator endpoints over HTTP 
			
				```
				management.endpoints.web.exposure.include=*
				```
	- Security:
		- You may NOT want to expose all of this information
		- Add Spring Security to project and endpoints are secured :-)

			```
			<dependency>
				<groupId>org.springframework.boot</groupId>
				<artifactId>spring-boot-starter-security</artifactId>
			</dependency>
			```
		- Now when you access: /actuator/beans- Spring Security will prompt for login
			- default user name: user
			- **check console logs for password**
		- You can override default user name and generated password, ie. in the `application.properties` add the following:
			- spring.security.user.name=scott
			- spring.security.user.password=tiger
			- Or use jdbc , just like previous secions on spring security setup.
	- Run Spring Boot apps from the command line
		- Spring Boot apps are self contained
		- Two options to run:
			- Run it as a java app : java -jar xxx.jar
			- Use Spring boot maven plugin : 
				- mvnw clean compile test : clean and compile and run test for the project
				- mvnw package 
					- jar is created in 'target' directory
				- mvnw spring-boot:run 