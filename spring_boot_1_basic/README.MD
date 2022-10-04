This is Spring demo from Udemy Spring & Hibernate course.  
- Spring Boot
	- Make it easier to get started with Spring development
	- Minimize the amount of manual configuration
	- Perform auto-configuration based on props files and JAR classpath
	- Help to resolve dependency conflicts (Maven or Gradle)
	- Provide an **embedded HTTP server** so you can get started quickly
 		- Tomcat, Jetty, Undertow, ...

- Spring Initializer(starter)
	- http://start.spring.io
	- Quickly create a starter Spring project
		- Select your dependencies
		- Creates a Maven/Gradle project
		- Import the project into your IDE
			- Eclipse, IntelliJ, NetBeans etc ...
- Running Spring Boot Apps
	- Spring Boot apps can be run standalone (includes embedded server)
	- Run the Spring Boot app from the IDE or command-line
		- java -jar mycoolapp.jar
- Deploying Spring Boot Apps
	- Spring Boot apps can also be deployed in the traditional way
	- Deploy WAR file to an external server: Tomcat, JBoss, WebSphere etc ...

- Spring FAQ
	- Spring Boot uses those technologies, eg. Spring MVC, Spring REST, Spring Core, Spring AOP ...
	- Once you do Spring Boot configs then you make use of regular Spring coding @Component, @Controller, @Autowired etc...
	- You can use any IDE for Spring Boot apps ... even use plain text editor
		- The Spring team provides free **Spring Tool Suite (STS)** [IDE plugins]

- Development Process
	1. Configure our project at Spring Initializr website
		- http://start.spring.io
	2. Download the zip file
	3. Unzip the file
	4. Import Maven project into our IDE

- Maven Wrapper(mvnw) files 
	- mvnw allows you to run a Maven project
	- No need to have Maven installed or present on your path
	- If correct version of Maven is NOT found on your computer
		- Automatically downloads correct version and runs Maven
 			- mvnw clean compile test
	- Two files are provided
		- mvnw.cmd for MS Windows
		- mvnw.sh for Linux/Mac
	-  If you already have Maven installed previously, you can ignore/delete the mvnw files and just use Maven as you normally would.

- Maven POM file:
	- dependency : `spring-boot-starter-web` include a collection of Maven dependencies(compatible versions), including
		- spring-web
		- spring-webmvc
		- hibernate-validator
		- tomcat
		- json etc
	- Save the developer from having to list all of the spring-web individual dependencies. Also, makes sure you have compatible versions
 
 	- Spring Boot Maven plugin 
		- to package executable jar or war
		- to easily run the app
	
		```
		<build>
			<plugins>
				<plugin>
					<groupId>org.springframework.boot</groupId>
					<artifactId>spring-boot-maven-plugin</artifactId>
				</plugin>
			</plugins>
		</build>
		```

		- use maven wrapper: mvnw 

		```
		$ ./mvnw package  
		$ ./mvnw spring-boot:run
		```

		- or if you have maven installed locally, just use mvn directly.

		---
		mvn package
		mvn spring-boot:run
		---

- Main application code
	- @SpringBootApplication is composed of the following annotations
		- @EnableAutoConfiguration :  Enables Spring Boot's auto-configuration support
		- @ComponentScan : Enables component scanning of current package. Also recursively scans sub-packages
			- Best practice is to **place your main application class in the root package above all other packages**
				- This implicitly defines a base search package. 
				- Allows you to leverage default component scanning
				- **No need to explicitly reference the base package name**
		- @Configuration : Able to register extra beans with @Bean or import other configuration classes

	```
	package com.luv2code.springboot.demo.mycoolapp; //root package 
	import org.springframework.boot.SpringApplication;
	import org.springframework.boot.autoconfigure.SpringBootApplication;

	@SpringBootApplication
	public class MycoolappApplication {
		public static void main(String[] args) {
			SpringApplication.run(MycoolappApplication.class, args);
		}
	}
	```
		- SpringApplication.run(MycoolappApplication.class, args);
			- Bootstrap your Spring Boot application
			- Behind the scenes 
				- Creates application context
				- Registers all beans
				- Starts the embedded server, Tomcat etc...

	- Note that the main applicaiton is located at the root package: com.luv2code.springboot.demo.mycoolapp, you can have other packages under this root package, like rest, controller, service, dao etc

	- Of course, you can scan other packages:
		- specify other base packages using `scanBasePackages` attribute

	```
	package com.luv2code.springboot.demo.mycoolapp;
	
	@SpringBootApplication(
		scanBasePackages={
		"com.luv2code.springboot.demo.mycoolapp",
		"org.acme.iot.utils",
		"edu.cmu.wean"
		})
	public class MycoolappApplication {
	...
	}
	```

- Application Properties
	- By default, Spring Boot will load properties from: `application.properties` located in `src/main/resources` ( created by spring initializer)
	- You can add spring boot properties
	- You can add custom properties

	```
	# configure server port
	server.port=8484
	# configure my props
	coach.name=Mickey Mouse
	team.name=The Mouse Crew
	```

	- use them in your code

	```
	@RestController
	public class FunRestController {

		@Value("${coach.name}")
		private String coachName;

		@Value("${team.name}")
		private String teamName;
	}
	...
	```

- Static content 
	- By default, Spring Boot will load static resources from "/static" directory
		- HTML files, CSS, JavaScript, images, etc ...

- WARNING:
	- Do not use the `src/main/webapp` directory if your application is packaged as a JAR.
	- Although this is a standard Maven directory, it works only with WAR packaging.
	- It is silently ignored by most build tools if you generate a JAR.

-  Spring Boot includes auto-configuration for following template engines
	- FreeMarker
	- Thymeleaf
	- Mustache
	- By default, Spring Boot will load templates from `/templates` directory under resources
 
 - Spring Boot Starter
	- Problem to solve:
		- what dependencies we need
		- what dependencies are compatiable
	- It would be great if there was a simple list of Maven dependencies
		- Collected as a group of dependencies ... one-stop shop
		- So I don't have to search for each dependency
	- A curated list of Maven dependencies
		- A collection of dependencies grouped together
		- Tested and verified by the Spring Development team
		- Makes it much easier for the developer to get started with Spring
		- Reduces the amount of Maven configuration
		- For example: spring-boot-starter-web

	- Spring Boot Starters
		- There are 30+ Spring Boot Starters from the Spring Development team
		- spring-boot-starter-web: Building web apps, includes validation, REST. Uses Tomcat as default embedded server
		- spring-boot-starter-security: Adding Spring Security support
		- spring-boot-starter-data-jpa: Spring database support with JPA and Hibernate
		- In Eclipse IDE, open pom file, check the dependency hierarchy

	- Spring Boot Starter Parent
		- A special starter that provides Maven defaults
			- Default compiler level (currently Java 8)
			- UTF-8 source encoding

			```
			<parent>
				<groupId>org.springframework.boot</groupId>
				<artifactId>spring-boot-starter-parent</artifactId>
				<version>2.7.4</version>
				<relativePath/> <!-- lookup parent from repository -->
			</parent>
			```
		
			-  To override a default, set as a property
			
			```
			<properties>
				<java.version>17</java.version>
			</properties>
			```

			-  For the spring-boot-starter-* dependencies, no need to list version
				- Inherit version from Starter Parent

			```
			<dependencies>
				<dependency>
					<groupId>org.springframework.boot</groupId>
					<artifactId>spring-boot-starter-web</artifactId>
				</dependency>

				<dependency>
					<groupId>org.springframework.boot</groupId>
					<artifactId>spring-boot-starter-test</artifactId>
					<scope>test</scope>
				</dependency>
			</dependencies>
			```

		- Benefits of the Spring Boot Starter Parent
			- Default Maven configuration: Java version, UTF-encoding etc
			- Dependency management
				- Use version on parent only
				- spring-boot-starter-* dependencies inherit version from parent
				- Default configuration of Spring Boot plugin