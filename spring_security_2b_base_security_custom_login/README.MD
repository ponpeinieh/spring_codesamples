This is Spring demo from Udemy Spring & Hibernate course.   
Spring Security 
- Custom login form:
    - Development Process
        1. Modify Spring Security Configuration to reference custom login form

            ```
            @Bean
            public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                        
                    return http
                    .authorizeRequests(configurer ->
                        configurer
                            .anyRequest()
                            .authenticated())
                    
                    .formLogin(configurer ->
                        configurer
                            .loginPage("/showMyLoginPage")
                            .loginProcessingUrl("/authenticateTheUser")
                            .permitAll())
                    
                    .build();  
                }	
            ```

        2. Develop a Controller to show the custom login form
            - We need to create a controller for the request mapping '/showMyLoginPage'
            - No need the controler for the request mapping '/authenticateTheUser'
                - Spring Security provides for us. 
                - Only in your form submits request to above request url.

            ```
            @Controller
            public class LoginController {

                @GetMapping("/showMyLoginPage")
                public String showMyLoginPage() {
                    return "plain-login";
                }
            }
            ```

        3. Create custom login form
            - Spring security defines default names for login form fields
                - username and password
            - Form action specifies the context path(ie. the context root)  ${pageContext.request.contextPath} 
                - Allows us to dynamically reference the context path
                - Helps to keep links relative to application context path
                - If we change context path, the links still work, so don't hard code the context path

            ```
            <%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
            ...
            <form:form action="${pageContext.request.contextPath}/authenticateTheUser" method="POST">
            <p>
            User name: <input type="text" name="username" />
            </p>
            <p>
            Password: <input type="password" name="password" />
            </p>
            <input type="submit" value="Login" />
            </form:form>
            ...
            ```

            - modify it to show login fail error message
                - Spring Security, if login failed, will send the `error` parameter back to the login page.
                    - http://localhost:8080/myapp/showMyLoginPage?error
                - use JSTL to check the error parameter

                ```
                <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
                ...
                <form:form action="..." method="...">
                <c:if test="${param.error != null}”>
                <i>Sorry! You entered invalid username/password.</i>
                </c:if>
                User name: <input type="text" name="username" />
                Password: <input type="password" name="password" />
                </form:form>
                ```

            - use Bootstrap design page
                - fancy-login.jsp