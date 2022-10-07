This is Spring demo from Udemy Spring & Hibernate course.   
- Spring Security 
    - Add Maven dependencies for Spring Security
        - `spring-security-web`
        - `spring-security-config`
    - Common pitfall is using incompatible versions
    - Finding compatible version for spring-security-web and compatiable Spring framework version
        - check the POM file for latest version of spring-security-web in Central Maven repo.
        - For the latest spring-security-web version 5.7.3, the compatible version of Spring Framework version is 5.3.22

        ```
        <properties>
		<springframework.version>5.3.22</springframework.version>
		<springsecurity.version>5.7.3</springsecurity.version>
		<maven.compiler.source>1.8</maven.compiler.source>
		<maven.compiler.target>1.8</maven.compiler.target>
        </properties>

        <dependencies>

            <!-- Spring MVC support -->
            <dependency>
                <groupId>org.springframework</groupId>
                <artifactId>spring-webmvc</artifactId>
                <version>${springframework.version}</version>
            </dependency>

            <!-- Spring Security support -->
            <dependency>
                <groupId>org.springframework.security</groupId>
                <artifactId>spring-security-web</artifactId>
                <version>${springsecurity.version}</version>
            </dependency>
            <dependency>
                <groupId>org.springframework.security</groupId>
                <artifactId>spring-security-config</artifactId>
                <version>${springsecurity.version}</version>
            </dependency>
        ```
- Developement process
    - Create Spring Security Initializer
        - Spring Security Web App Initializer
            - Spring Security provides support for security initialization
            - Your security code is used to initialize the servlet container
            - Special class to register the `Spring Security Filters`
                - AbstractSecurityWebApplicationInitializer
            - To Do List:
                - Extend above abstract class
                - That's it.

                ```
                import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;
                public class SecurityWebApplicationInitializer extends AbstractSecurityWebApplicationInitializer {
                }
                ```

    - Create Spring Security Configuration(@Configuration)
        - The Spring Security team released Spring Security 5.7 on 16 May 2022. In this release, the deprecated the `WebSecurityConfigurerAdapter` class. The current recommendation is to make use of a more component based approach by creating beans to handle the configuration.
        - See article reference: https://spring.io/blog/2022/02/21/spring-security-without-the-websecurityconfigureradapter
        - See accompanying md file.

        ```
        import org.springframework.context.annotation.Configuration;
        import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
        import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
        
        @Configuration
        @EnableWebSecurity
        public class DemoSecurityConfig extends WebSecurityConfigurerAdapter { //deprecated

        }
        ```

        - change to the following

        ```
        package com.example.springsecurity.demo.config;

        import org.springframework.context.annotation.Bean;
        import org.springframework.context.annotation.Configuration;
        import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
        import org.springframework.security.core.userdetails.User;
        import org.springframework.security.core.userdetails.UserDetails;
        import org.springframework.security.provisioning.InMemoryUserDetailsManager;

        @Configuration
        @EnableWebSecurity
        public class DemoSecurityConfig {
            //...
        }
        ```

    - Add users, passwords, roles

        ```
        @Bean
        public InMemoryUserDetailsManager userDetailsService() {
            
            UserDetails john = User.builder()
                .username("john")
                .password("{noop}test123") //{noop} for encoder?
                .roles("EMPLOYEE")
                .build();

            UserDetails mary = User.builder()
                    .username("mary")
                    .password("{noop}test123")
                    .roles("MANAGER")
                    .build();

            UserDetails susan = User.builder()
                    .username("susan")
                    .password("{noop}test123")
                    .roles("ADMIN")
                    .build();
            
            return new InMemoryUserDetailsManager(john, mary, susan);
            
        }
        ```
- Run the App: show the default login form

- Change Context root (also called context path):
    - http://localhost:8080/<context_root>
    - Project properties > Web Project Settings > Context Root