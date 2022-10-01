This is Spring demo from Udemy Spring & Hibernate course.   
- Spring Security Model
    - Defines a framework for security
    - Implemented using `Servlet filters` in the background
        - Used to pre-process/post-process web request
        - Can route web requests based on security logic
        - Provides a bulk of security funcationality with servlet filters
    - Two methods of securing a Web app:
        - Declarative
        - Programmatic

- Spring Security concepts
    - Authentication
        - Check user id/passwd with credentials stored in app/db
    - Authorization
        - Check to see if user has an authorized role
    - Declarative Security
        - Define security constraints in configuration
            - All Java config (@Configuration, no xml)
            - Spring XML config
        - Separation of concerns between application code and security code
    - Programmatic Security
        - Provides an API for custom application coding

- Different Login Methods
    - Http basic authentication
    - Spring Security provides a default login form
    - Custom login form - your own look and feel, html+css
- Authentication and Authorization
    - In-memory
    - JDBC
    - LDAP
    - Custom/Pluggable

- Spring MVC using Java Configuration (no xml)
    - Replace web.xml and Spring config xml file with Spring `@Configuration` and `Spring Dispatcher Servlet Initializer`
    - Development Process
        1. Add Maven dependencies for Spring MVC Web App
            - spring-webmvc
            - jstl
            - javax.servlet-api
            - javax.servlet.jsp-api

            ```
            <dependency>
                <groupId>org.springframework</groupId>
                <artifactId>spring-webmvc</artifactId>
                <version>...</version>
            </dependency>

            <dependency>
                <groupId>javax.servlet</groupId>
                <artifactId>javax.servlet-api</artifactId>
                <version>3.1.0</version>
            </dependency>

            <dependency>
                <groupId>javax.servlet.jsp</groupId>
                <artifactId>javax.servlet.jsp-api</artifactId>
                <version>2.3.1</version>
            </dependency>

            <dependency>
                <groupId>javax.servlet</groupId>
                <artifactId>jstl</artifactId>
                <version>1.2</version>
            </dependency>
            ```

            - **Customize Maven Build**
                - Since we are not using web.xml, we need to **add `Maven WAR plugin`**

                ```
                <plugin>
                    <groupId>org.apache.maven.plugins</groupId> <!--can be omited???-->
                    <artifactId>maven-war-plugin</artifactId>
                    <version>3.2.2</version>
                </plugin>
                ```

        2. Create Spring App Configuration (@Configuration)
            - Replace XML config (the old way)

                ```
                <!-- Add support for component scanning -->
                <context:component-scan base-package="com.example.springdemo" />

                <!-- Add support for conversion, formatting and validation support -->
                <mvc:annotation-driven/>

                <!-- Define Spring MVC view resolver -->
                <bean
                    class="org.springframework.web.servlet.view.InternalResourceViewResolver">
                    <property name="prefix" value="/WEB-INF/view/" />
                    <property name="suffix" value=".jsp" />
                </bean>
                ```

            - Enable the MVC java config

                ```
                @Configuration
                @EnableWebMvc
                @ComponentScan("com.example.springdemo")
                public class DemoAppConfig {

                    // define a bean for ViewResolver
                    @Bean
                    public ViewResolver viewResolver() {
                        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
                        viewResolver.setPrefix("/WEB-INF/view/");
                        viewResolver.setSuffix(".jsp");
                        return viewResolver;
                    }
	
                ```

        3. Create Spring Dispatcher Servlet Initializer
            - Replace web.xml

            ```
            <servlet>
                <servlet-name>dispatcher</servlet-name>
                <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
                <init-param>
                <param-name>contextConfigLocation</param-name>
                <param-value>/WEB-INF/spring-mvc-crud-demo-servlet.xml</param-value>
                </init-param>
                <load-on-startup>1</load-on-startup>
            </servlet>
            
            <servlet-mapping>
                <servlet-name>dispatcher</servlet-name>
                <url-pattern>/</url-pattern>
            </servlet-mapping>
            ```

            - And use Web App Initializer
                - Spring MVC provides support for web app initialization
                - Make sure your code is automatically detected
                - Your code is used to initialize the servlet container
                - Use `AbstractAnnotationConfigDispatcherServletInitializer` abstract class
                - TO Do List:
                    - Extend above abstract class
                    - Override required methods
                    - Specify servlet mapping and location of your app config

                ```
                import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

                public class MySpringMvcDispatcherServletInitializer extends
                        AbstractAnnotationConfigDispatcherServletInitializer {
                    @Override
                    protected Class<?>[] getRootConfigClasses() {
                        // TODO Auto-generated method stub
                        return null;
                    }
                    @Override
                    protected Class<?>[] getServletConfigClasses() {
                        return new Class[] { DemoAppConfig.class }; //java config class
                    }
                    @Override
                    protected String[] getServletMappings() {
                        return new String[] { "/" };
                    }
                }
                ```

        4. Develop our Spring controller
            ```
            @Controller
            public class DemoController {
                @GetMapping("/")
                public String showHome() {
                    return "home";
                }
            }
            ```
        5. Develop our JSP view page

