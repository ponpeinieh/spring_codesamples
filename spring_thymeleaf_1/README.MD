This is Spring demo from Udemy Spring & Hibernate course.   
- Thymeleaf
    - General purpose templating engine
    - Can use Thymeleaf outside of web apps
    - Thymeleaf template
        - Can be an HTML page with some Thymeleaf expressions
        - Include dynamic content from Thymeleaf expressions
            - Can access Java code, objects , Spring beans
    - Thymeleaf Use Cases (non-web)
        - Email Template
        - CSV Template
        - PDF Template 
    - POM dependency
        - Spring Boot will auto configure to use Thymeleaf templates

    ```
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-thymeleaf</artifactId>
    </dependency>
    ```

    - Controller

    ```
    @Controller
    public class DemoController {
        @GetMapping("/")
        public String sayHello(Model theModel) {
            theModel.addAttribute("theDate", new java.util.Date());
            return"helloworld"; // return the template : src/main/resources/templates/helloworld.html
        }
    }
    ```  

    - Where to place Thymeleaf template
        - In Spring Boot, your Thymeleaf template files go in `src/main/resources/templates`
        - For web apps, Thymeleaf templates have a .html extension  
        - Use Thymeleaf expression
            - <html xmlns:th="http://www.thymeleaf.org">

    ```
    <!DOCTYPE HTML>
    <html xmlns:th="http://www.thymeleaf.org">
    <head> ... </head>
    <body>
    <p th:text="'Time on the server is ' + ${theDate}" />
    </body>
    </html>
    ```

    - Additional Features
        - Looping and conditionals
        - CSS and JavaScript integration
        - Template layouts and fragments


    - Using CSS with Thymleaf Templates
        - You have the option of using
            - Local CSS files as part of your project
            - Referencing remote CSS files
        - Create local CSS file
            - Spring Boot will look for static resources in the directory
                - `src/main/resources/static`
            - We can create a subfolder 'css' in the static 
        - Reference CSS in Thymeleaf template
            -  <link rel="stylesheet" th:href="@{/css/demo.css}" />
            - @ symbol : Reference context path of your application(app root)
            ```
            <head>
            <title>ThymeleafDemo</title>
            <!-- reference CSS file -->
            <link rel="stylesheet" th:href="@{/css/demo.css}" />
            </head>
            ```      
    - Spring Boot will search following directories for static resources: /src/main/resources
        1. /META-INF/resources
        2. /resources
        3. /static
        4. /public     
    - 3rd Party CSS Libraries - Bootstrap
        - Local Installation 
            - Download Bootstrap file(s) and add to /static/css directory

            ```
            <head>
            ... ...
            <!-- reference CSS file -->
            <link rel="stylesheet" th:href="@{/css/bootstrap.min.css}" />
            </head>         
            ```
            
        - Remote Files

        ```
        <head>
        ... ...
        <!-- reference CSS file -->
        <link rel="stylesheet"
        href="https://stackpath.bootstrapcdn.com/bootstrap/4.2.1/css/bootstrap.min.css" />
        ... ...
        </head>
        ```