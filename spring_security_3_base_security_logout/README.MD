This is Spring demo from Udemy Spring & Hibernate course.   
- Spring Security : Logout
    - Send data to default logout URL - `/logout`
    - Logout URL will be handled by Spring Security Filters - no code required
        - Invalidate user's HTTP session and remove session cookies.
        - Send the user back to login page  
            - Append a logout parameter : `?logout`

    - Java Config class:

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
        
        .logout(configurer -> 
            configurer
                .permitAll())
        
        .build();        
    }	
    ```

    - Logout button
        - By default, **must be POST method.**

    ```
    <form:form action="${pageContext.request.contextPath}/logout" method="POST">
        <input type="submit" value="Logout" />
    </form:form>
    ```

    - login page
        - Check param `logout` and display logout message.
    ```
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    ...
    <form:form action="..." method="...">
    <c:if test=“${param.logout != null}”>
        <i>You have been logged out.</i>
    </c:if>
```