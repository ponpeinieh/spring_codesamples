This is Spring demo from Udemy Spring & Hibernate course.   
- REST over HTTP
    - Leverage HTTP methods for CRUD operations
        - POST : Create a new entity
        - GET : Read a list of entities or single entity
        - PUT : Update an existing entity
        - DELETE : Delete an existing entity 
    - HTTP Request Message
        - Request line: **the HTTP command**
        - Header variables: request metadata
        - Message body: contents of message
    - HTTP Response Message
        - Response line: **server protocol** and **status code**(HTTP Status Code: 200,404,500)
            - 100-199: informational
            - 200-299: successful
            - 300-399: redirection
            - 400-499: client error (eg. 401:Authentication required, 404: File not found)
            - 500-599: server error
        - Header variables: response metadata
            - Content type: XML , JSON
                - MIME(Multipurpose Internet Mail-Extension) Content Types: 
                    - The message format is described by MIME content type
                    - Basic Syntax: type/sub-type
                        - text/html, text/plain, application/json, application/xml, ...
            - Size of data
        - Message body: contents of message
    - Client Tool : Send HTTP requests to the REST Web Service / API
        - Plenty of tools available: `curl`, `Postman`, etc ...

    - Postman  - a popular REST client
    - www.jsontest.com : provide a collection of backend REST services to test against
    - jsonplaceholder.typicode.com : provide free fake API for testing and prototyping.

- Spring REST support
    - **Spring Web MVC provides support for Spring REST**
        - just add dependency for Spring MVC and you will get Spring REST
    - New annotation @RestController
        - Extension of @Controller
        - Handles REST requests and responses
        - Spring REST will also **automatically convert Java POJOs to JSON** 
            - As long as the Jackson project is on the classpath or pom.xml

        ```
        @RestController
        @RequestMapping("/test")
        public class DemoRestController {
            @GetMapping("/hello")
            public String sayHello() {
                return "Hello World!";
            }
        }
        ```
    - Development Process
        1. Add Maven dependency for Spring MVC and Jackson project

            ```
            <!-- Add Spring MVC and REST support -->
            <dependency>
                <groupId>org.springframework</groupId>
                <artifactId>spring-webmvc</artifactId>
                <version>...</version>
            </dependency>

            <!-- Add Jackson for JSON converters -->
            <dependency>
                <groupId>com.fasterxml.jackson.core</groupId>
                <artifactId>jackson-databind</artifactId>
                <version>...</version>
            </dependency>

            <!-- Add Servlet support -->
            <dependency>
                <groupId>javax.servlet</groupId>
                <artifactId>javax.servlet-api</artifactId>
                <version>...</version>
            </dependency>
            <!-- Add JSP support -->
            <dependency>
                <groupId>javax.servlet.jsp</groupId>
                <artifactId>javax.servlet.jsp-api</artifactId>
                <version>...</version>
            </dependency>
            ```    

        2. Add code for All Java Config: @Configuration

            ```
            @Configuration
            @EnableWebMvc
            @ComponentScan(basePackages="com.luv2code.springdemo")
            public class DemoAppConfig {
            }
            ```

        3. Add code for All Java Config: Servlet Initializer

        ```
        public class MySpringMvcDispatcherServletInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
            @Override
            protected Class<?>[] getRootConfigClasses() {
            // TODO Auto-generated method stub
            return null;
            }
            @Override
            protected Class<?>[] getServletConfigClasses() {
            return new Class[] { DemoAppConfig.class };
            }
            @Override
            protected String[] getServletMappings() {
            return new String[] { "/" };
            }
        }
        ```

        4. Create Spring REST Service using @RestController

        ```
        @RestController
        @RequestMapping("/test")
        public class DemoRestController {
            @GetMapping("/hello")
            public String sayHello() {
                return "Hello World!";
            }
        }
```
