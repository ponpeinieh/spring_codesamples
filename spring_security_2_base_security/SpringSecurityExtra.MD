This is Spring demo from Udemy Spring & Hibernate course.   
-  Spring Security - Lambda DSL 
    - reference: https://spring.io/blog/2019/11/21/spring-security-lambda-dsl
    - **Spring Security 5.2** includes enhancements to the **DSL, which allow `HTTP security` to be configured using lambdas.**
    - It is important to note that the prior configuration style is still valid and supported. 
    - The addition of lambdas is intended to provide more flexibility, but their usage is optional.

    - Config using lambdas

        ```
        @EnableWebSecurity
        public class SecurityConfig extends WebSecurityConfigurerAdapter {

            @Override
            protected void configure(HttpSecurity http) throws Exception {
                http
                    .authorizeRequests(authorizeRequests ->
                        authorizeRequests
                            .antMatchers("/blog/**").permitAll()
                            .anyRequest().authenticated()
                    )
                    .formLogin(formLogin ->
                        formLogin
                            .loginPage("/login")
                            .permitAll()
                    )
                    .rememberMe(withDefaults());
            }
        }
        ```
        
    - Equivalent configuration without using lambdas
    
    ```
    @EnableWebSecurity
    public class SecurityConfig extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                .authorizeRequests()
                    .antMatchers("/blog/**").permitAll()
                    .anyRequest().authenticated()
                    .and()              //<- use and()
                .formLogin()
                    .loginPage("/login")
                    .permitAll()
                    .and()              //<- use and()
                .rememberMe();
        }
    }
    ```
    - Key differences:
        - In the Lambda DSL there is no need to chain configuration options using the and() method. 
        - The `HttpSecurity` instance is automatically returned for further configuration after the call to the lambda method.
        - `withDefaults()` enables a security feature using the defaults provided by Spring Security. 
            - This is a shortcut for the lambda expression **it -> {}**.
    - Goals of the Lambda DSL
        - Automatic indentation makes the configuration more readable.
        - The is no need to chain configuration options using and().
        - The Spring Security DSL has a similar configuration style to other Spring DSLs such as Spring Integration and Spring Cloud Gateway.

- Spring Security without the WebSecurityConfigurerAdapter
    - In **Spring Security 5.7.0-M2** we deprecated the `WebSecurityConfigurerAdapter`, as we encourage users to move towards a **component-based security configuration**.

- antMatcher() vs mvcMatcher():
    - reference: https://stackoverflow.com/questions/50536292/difference-between-antmatcher-and-mvcmatcher
    - In the official documentation:
        - antMatcher(String antPattern) - Allows configuring the HttpSecurity to only be invoked when matching the provided ant pattern.
        - mvcMatcher(String mvcPattern) - Allows configuring the HttpSecurity to only be invoked when matching the provided Spring MVC pattern.
        - Generally **mvcMatcher is more secure than an antMatcher**. As an example:

            - antMatchers("/secured") matches only the exact /secured URL
            - mvcMatchers("/secured") matches /secured as well as /secured/, /secured.html, /secured.xyz and therefore is more general and can also **handle some possible configuration mistakes**.

- Configuring `WebSecurity`
    - In Spring Security 5.4 we also introduced the `WebSecurityCustomizer`.
    - The WebSecurityCustomizer is a callback interface that can be used to customize `WebSecurity`.
    - Below is an example configuration using the WebSecurityConfigurerAdapter that **ignores requests that match /ignore1 or /ignore2** (Note, for example, can be used on the `resources` or `public` folders)

        ```
        @Configuration
        public class SecurityConfiguration {

            @Bean
            public WebSecurityCustomizer webSecurityCustomizer() {
                return (web) -> web.ignoring().antMatchers("/ignore1", "/ignore2");
            }

        }
        ```

- Register a JdbcUserDetailsManager bean: 
    - Add one user in the user table using `User.builder()` (ie. The Builder pattern)
    - password encoding scheme : `{noop}` or `{bcrypt}`
    - `roles("EMPLOEE")` -> the actual value in the authorities table is `ROLE_EMPLOYEE`

    ```
    @Bean
    public UserDetailsManager userDetailsManager() {
        //return new JdbcUserDetailsManager(securityDataSource);
    	UserDetailsManager users = new JdbcUserDetailsManager(securityDataSource);
    	UserDetails user = User.builder()
                .username("user")
                .password("{noop}password")
                .roles("EMPLOYEE")
                .build();
    	if(! users.userExists(user.getUsername()))
    		users.createUser(user);
    	return users;
    }
    ```