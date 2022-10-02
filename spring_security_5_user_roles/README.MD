This is Spring demo from Udemy Spring & Hibernate course.   
- Spring Security 
    - Spring Security provides JSP custom tags for accessing user id and roles
        - Development Process
            1. Update POM file for Spring Security JSP Tag Library

                ```
                <dependency>
                    <groupId>org.springframework.security</groupId>
                    <artifactId>spring-security-taglibs</artifactId>
                    <version>${springsecurity.version}</version>
                </dependency>
                ```

            2. Add Spring Security JSP Tag Library to JSP page

                ```
                <%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
                ```

            3. Display User ID

                ```
                User: <security:authentication property="principal.username" />
                ```

            4. Display User Roles

                ```
                Role(s): <security:authentication property="principal.authorities" />
                ```
    - Restrict access based on roles
        - / -> role: EMPLOYEE
        - /leaders -> role: MANAGER
        - /systems -> role: ADMIN
        - Development Process
            1. Create supporting controller code and view pages
            2. Update user roles
                - A user can have multiple roles

            ```
            @Bean
            public InMemoryUserDetailsManager userDetailsManager() {
                
                UserDetails john = User.builder()
                    .username("john")
                    .password("{noop}test123")
                    .roles("EMPLOYEE")
                    .build();

                UserDetails mary = User.builder()
                        .username("mary")
                        .password("{noop}test123")
                        .roles("EMPLOYEE", "MANAGER") //multiple roles
                        .build();

                UserDetails susan = User.builder()
                        .username("susan")
                        .password("{noop}test123")
                        .roles("EMPLOYEE", "ADMIN")
                        .build();
                
                return new InMemoryUserDetailsManager(john, mary, susan);        
            }
            ```

            3. Restrict Access based on Roles

            ```
            @Bean
            public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                
                return http
                .authorizeRequests(configurer ->
                    configurer
                        .antMatchers("/").hasRole("EMPLOYEE")
                        .antMatchers("/leaders/**").hasRole("MANAGER")
                        .antMatchers("/systems/**").hasRole("ADMIN"))
                
                .formLogin(configurer ->
                    configurer
                        .loginPage("/showMyLoginPage")
                        .loginProcessingUrl("/authenticateTheUser")
                        .permitAll())
                
                .logout(configurer -> 
                    configurer
                        .permitAll())
                
                .exceptionHandling(configurer ->
                    configurer
                        .accessDeniedPage("/access-denied"))
                
                .build();        
            }	
            ```


    - Access Deny : Http Status 403 Forbidden
        - Development Process
            1. Configure custom page for access denied
                - as above code 

                ```
                .exceptionHandling(configurer ->
                    configurer
                        .accessDeniedPage("/access-denied"))
                ```
                
            2. Create supporting controller code and view page