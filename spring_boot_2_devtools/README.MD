This is Spring demo from Udemy Spring & Hibernate course.  
- Spring Boot
	- Spring Boot Dev Tools
		- The Problem to solve:
			- When running Spring Boot applications, if you make changes to your source code, then you have to manually restart your application :-(
		-  `spring-boot-devtools` to the rescue!
			- **Automatically restarts your application when code is updated**
			- Simply add the dependency to your POM file

				```
				<dependency>
					<groupId>org.springframework.boot</groupId>
					<artifactId>spring-boot-devtools</artifactId>
				</dependency>
				```

			- No need to write additional code :-)