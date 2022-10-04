This is Spring demo from Udemy Spring & Hibernate course.  
- Spring Boot - Custom application properties
	- By default, Spring Boot reads information from a standard properties file
		- Located at: src/main/resources/application.properties
		- You can define ANY custom properties in this file and your Spring Boot app can access properties using @Value

		```
		@RestController
		public class FunRestController {

			// inject properties for: coach.name and team.name

			@Value("${coach.name}") //don't forget the double quote
			private String coachName;

			@Value("${team.name}")
			private String teamName;
		}
		```
	-  Spring Boot can be configured in the application.properties file
		- Server port, context path, actuator, security etc ...
		- Spring Boot has 1,000+ properties  - https://docs.spring.io/spring-boot/docs/current/reference/html/application-properties.html#appendix.application-properties
		- Some core properties:
			- Log levels severity mapping
				- logging.level.org.springframework=DEBUG
				- logging.level.org.hibernate=TRACE
				- logging.level.com.luv2code=INFO
			- Log file name
				- logging.file=my-crazy-stuff.log
			- Web: HTTP server port
				- server.port=7070
			- Web : Context path of the application
				- server.servlet.context-path=/my-silly-app
			- Web : Default HTTP session time out
				- server.servlet.session.timeout=15m
			- Actuator: Endpoints to include by name or wildcard
				- management.endpoints.web.exposure.include=*
			- Actuator: Endpoints to exclude by name or wildcard
				- management.endpoints.web.exposure.exclude=beans,mapping
			- Actuator: Base path for actuator endpoints
				- management.endpoints.web.base-path=/actuator
			- Security: Default user name
				- spring.security.user.name=admin
			- Security: Password for default user
				- spring.security.user.password=topsecret
			- Data: JDBC URL of the database
				- spring.datasource.url=jdbc:mysql://localhost:3306/ecommerce
			- Data: Login username of the database
				- spring.datasource.username=scott
			- Data: Login password of the database
				- spring.datasource.password=tiger


