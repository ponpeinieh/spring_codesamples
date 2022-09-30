This is Spring demo from Udemy Spring & Hibernate course. 

Spring MVC + AOP Logging
- Development Process
    1. Add AspectJ JAR file to web project
        - download aspectj weaver
        - add jar to WEB-INF/lib

    2. Enable AspectJ Auto Proxy
        - Spring xml config file

            ```
            <beans xmlns="http://www.springframework.org/schema/beans"
            ...
            xmlns:aop="http://www.springframework.org/schema/aop"
            xsi:schemaLocation="
            ...
            http://www.springframework.org/schema/aop
            http://www.springframework.org/schema/aop/spring-aop.xsd">

                <!-- Add AspectJ autoproxy support for AOP -->
                <aop:aspectj-autoproxy />
                ...
            </beans>
            ```

        - Or if you want to use pure Java configuration then use the annotation
            - @EnableAspectJAutoProxy

    3. Create Aspect
    1. Add logging support
    2. Setup pointcut declarations
        - Only match on three package : controller, dao, service
    3. Add @Before advice
    4. Add @AfterReturning advice