This is Spring demo from Udemy Spring & Hibernate course.   
Spring Security with JDBC
- Spring Security can read user account info from database
- By Default, you have to follow Spring Security's **predefined table schemas**
- You can also customize the table schema
    - You are responsible for developing the code to access the data using JDBC, Hibernate etc.

-  Spring Security's **predefined table schemas** 
    - Development Process
        1. Develop SQL Script to set up database tables
            - Tables: with exact name `users` and `authorities` (means roles)
            - With exact columns
            - Spring Security Password Storage
                - In Spring Security 5, passwords are stored using a specific format: `{id}encodedPassword`
                - id: **encoding algorithm id**
                    - noop (means **no opeartion**, Plain text passwords)
                    - bcrypt( means **BCrypt** password hashing)
                    
            ```
            CREATE TABLE `users` (
            `username` varchar(50) NOT NULL,
            `password` varchar(50) NOT NULL,
            `enabled` tinyint(1) NOT NULL,
            PRIMARY KEY (`username`)
            ) ENGINE=InnoDB DEFAULT CHARSET=latin1;
            ```

            ```
            INSERT INTO `users` VALUES
            ('john','{noop}test123',1),
            ('mary','{noop}test123',1),
            ('susan','{noop}test123',1);
            ```

            ```
            CREATE TABLE `authorities` (
            `username` varchar(50) NOT NULL,
            `authority` varchar(50) NOT NULL,
            UNIQUE KEY `authorities_idx_1` (`username`,`authority`),
            CONSTRAINT `authorities_ibfk_1`
            FOREIGN KEY (`username`)
            REFERENCES `users` (`username`)
            ) ENGINE=InnoDB DEFAULT CHARSET=latin1;
            ```

            ```
            INSERT INTO `authorities` VALUES
            ('john','ROLE_EMPLOYEE'),
            ('mary','ROLE_EMPLOYEE'),
            ('mary','ROLE_MANAGER'),
            ('susan','ROLE_EMPLOYEE'),
            ('susan','ROLE_ADMIN');
            ```

        2. Add database support to Maven POM file

        ```
        <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
        <version>5.1.45</version>
        </dependency>

        <dependency>
        <groupId>com.mchange</groupId>
        <artifactId>c3p0</artifactId>
        <version>0.9.5.2</version>
        </dependency>
        ```

        3. Create JDBC properties file
            - src/main/resources/persistence-mysql.properties

            ```
            #
            # JDBC connection properties
            #
            jdbc.driver=com.mysql.jdbc.Driver
            jdbc.url=jdbc:mysql://localhost:3306/spring_security_demo?useSSL=false
            jdbc.user=springstudent
            jdbc.password=springstudent
            #
            # Connection pool properties
            #
            connection.pool.initialPoolSize=5
            connection.pool.minPoolSize=5
            connection.pool.maxPoolSize=20
            connection.pool.maxIdleTime=3000
            ```

        4. Define DataSource in Spring Configuration
            - @PropertySource("classpath:persistence-mysql.properties") read the properties file in src/main/resources/
            - @Autowired on Environment field - to hold data from properties file

        ```
        @Configuration
        @EnableWebMvc
        @ComponentScan(basePackages="com.example.springsecurity.demo")
        @PropertySource("classpath:persistence-mysql.properties")
        public class DemoAppConfig {

            // set up variable to hold the properties
            
            @Autowired
            private Environment env;
            
            // set up a logger for diagnostics
            
            private Logger logger = Logger.getLogger(getClass().getName());
            
            
            // define a bean for ViewResolver

            @Bean
            public ViewResolver viewResolver() {
                
                InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
                
                viewResolver.setPrefix("/WEB-INF/view/");
                viewResolver.setSuffix(".jsp");
                
                return viewResolver;
            }
            
            // define a bean for our security datasource
            
            @Bean
            public DataSource securityDataSource() {
                
                // create connection pool
                ComboPooledDataSource securityDataSource
                                            = new ComboPooledDataSource();
                        
                // set the jdbc driver class
                
                try {
                    securityDataSource.setDriverClass(env.getProperty("jdbc.driver"));
                } catch (PropertyVetoException exc) {
                    throw new RuntimeException(exc);
                }
                
                // log the connection props
                // for sanity's sake, log this info
                // just to make sure we are REALLY reading data from properties file
                
                logger.info(">>> jdbc.url=" + env.getProperty("jdbc.url"));
                logger.info(">>> jdbc.user=" + env.getProperty("jdbc.user"));
                
                
                // set database connection props
                
                securityDataSource.setJdbcUrl(env.getProperty("jdbc.url"));
                securityDataSource.setUser(env.getProperty("jdbc.user"));
                securityDataSource.setPassword(env.getProperty("jdbc.password"));
                
                // set connection pool props
                
                securityDataSource.setInitialPoolSize(
                        getIntProperty("connection.pool.initialPoolSize"));

                securityDataSource.setMinPoolSize(
                        getIntProperty("connection.pool.minPoolSize"));

                securityDataSource.setMaxPoolSize(
                        getIntProperty("connection.pool.maxPoolSize"));

                securityDataSource.setMaxIdleTime(
                        getIntProperty("connection.pool.maxIdleTime"));
                
                return securityDataSource;
            }
            
            // need a helper method 
            // read environment property and convert to int
            
            private int getIntProperty(String propName) {
                
                String propVal = env.getProperty(propName);
                
                // now convert to int
                int intPropVal = Integer.parseInt(propVal);
                
                return intPropVal;
            }
        }
        ```

        5. Update Spring Security Configuration to use JDBC

        ```
        package com.luv2code.springsecurity.demo.config;

        import javax.sql.DataSource;

        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.context.annotation.Bean;
        import org.springframework.context.annotation.Configuration;
        import org.springframework.security.config.annotation.web.builders.HttpSecurity;
        import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
        import org.springframework.security.provisioning.JdbcUserDetailsManager;
        import org.springframework.security.provisioning.UserDetailsManager;
        import org.springframework.security.web.SecurityFilterChain;

        @Configuration
        @EnableWebSecurity
        public class DemoSecurityConfig { 
            @Autowired
            private DataSource securityDataSource;
  
            @Bean
            public UserDetailsManager userDetailsManager() {
                return new JdbcUserDetailsManager(securityDataSource);
            }

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
        }
        ```