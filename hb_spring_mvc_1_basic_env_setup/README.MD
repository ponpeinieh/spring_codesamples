This is Spring demo from Udemy Spring & Hibernate course.
- Using Spring with hibernate for CRUD

- Create MySQL new user - failed with ERROR 1396 
    - CREATE or DROP USER failed
    - MySQL has a bug that triggers this error when you remove a user without using the DROP USER statement.
    - This bug prevents you from re-creating a user previously deleted using the DELETE statement.
    - To fix this, you need to run a DROP USER statement for the same user account. MySQL will respond with the same error, but after that you can create the user again.
    - The error hasn’t been fixed up to MySQL version 8.0.26  
    - To list all existing users:
        - select user,host from mysql.user; 


- setup the dynamic web project 
    - add mysql jar into lib folder
    - Test jdbc connection using a servlet
        - note that here we need to have Class.forName("com.mysql.jdbc.Driver");
        ```
        String jdbcUrl = "jdbc:mysql://localhost:3306/web_customer_tracker?useSSL=false&serverTimezone=UTC";
		String user="springstudent";
		String passwd="springstudent";
		try {
			PrintWriter out = response.getWriter();
			out.println("Connect to db:"+jdbcUrl);
			Class.forName("com.mysql.jdbc.Driver"); //need this
			Connection conn = DriverManager.getConnection(jdbcUrl,user,passwd);
			out.println("Successful connect to db!");
			
		} catch(Exception ex) {
			ex.printStackTrace();
		}
        ```
    - add javax.servlet.jsp.jstl-1.2.1.jar and javax.servlet.jsp.jstl-api-1.2.1.jar 
    - add spring jar into lib folder
    - add hibernate jar into lib folder
        - also c3p0 optional jars for connection pool. 
            - c3p0-0.9.5.5.jar, 
            - hibernate-c3p0-5.6.5.Final.jar, 
            - mchange-commons-java-0.2.19.jar
    - For java 9 above, the spring class mentioned 4 more jar files
        - javax.activation-1.2.0.jar (already in hibernate jar)
        - jaxb-api-2.3.0.jar (already in hibernate jar)
        - jaxb-core-2.3.0.jar (https://repo1.maven.org/maven2/com/sun/xml/bind/jaxb-core/2.3.0/)
        - jaxb-impl-2.3.0.jar (https://repo1.maven.org/maven2/com/sun/xml/bind/jaxb-impl/2.3.0/)

    - Configuration for Spring + Hibernate
        1. Define database dataSource / connection pool
        2. Setup Hibernate session factory
            - make sure the package is correct
            - <property name="packagesToScan" value="com.example.springdemo.entity" />
        3. Setup Hibernate transaction manager
        4. Enable configuration of transactional annotations, ie. using @Transactional

        ```
        <!-- Step 1: Define Database DataSource / connection pool -->
	    <bean id="myDataSource" class="com.mchange.v2.c3p0.ComboPooledDataSource"
          destroy-method="close">
            <property name="driverClass" value="com.mysql.cj.jdbc.Driver" />
            <property name="jdbcUrl" value="jdbc:mysql://localhost:3306/web_customer_tracker?useSSL=false&amp;serverTimezone=UTC" />
            <property name="user" value="springstudent" />
            <property name="password" value="springstudent" /> 

            <!-- these are connection pool properties for C3P0 -->
            <property name="minPoolSize" value="5" />
            <property name="maxPoolSize" value="20" />
            <property name="maxIdleTime" value="30000" />
        </bean>  
	
        <!-- Step 2: Setup Hibernate session factory -->
        <bean id="sessionFactory"
            class="org.springframework.orm.hibernate5.LocalSessionFactoryBean">
            <property name="dataSource" ref="myDataSource" />
            <property name="packagesToScan" value="com.example.springdemo.entity" />
            <property name="hibernateProperties">
            <props>
                <prop key="hibernate.dialect">org.hibernate.dialect.MySQLDialect</prop>
                <prop key="hibernate.show_sql">true</prop>
            </props>
            </property>
        </bean>	  

        <!-- Step 3: Setup Hibernate transaction manager -->
        <bean id="myTransactionManager"
                class="org.springframework.orm.hibernate5.HibernateTransactionManager">
            <property name="sessionFactory" ref="sessionFactory"/>
        </bean>
        
        <!-- Step 4: Enable configuration of transactional behavior based on annotations -->
        <tx:annotation-driven transaction-manager="myTransactionManager" />
        ```

- complete spring config file

```
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
	xmlns:context="http://www.springframework.org/schema/context"
    xmlns:tx="http://www.springframework.org/schema/tx"
	xmlns:mvc="http://www.springframework.org/schema/mvc"
	xsi:schemaLocation="
		http://www.springframework.org/schema/beans
		http://www.springframework.org/schema/beans/spring-beans.xsd
		http://www.springframework.org/schema/context
		http://www.springframework.org/schema/context/spring-context.xsd
		http://www.springframework.org/schema/mvc
		http://www.springframework.org/schema/mvc/spring-mvc.xsd
		http://www.springframework.org/schema/tx 
		http://www.springframework.org/schema/tx/spring-tx.xsd">

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

    <!-- Step 1: Define Database DataSource / connection pool -->
	<bean id="myDataSource" class="com.mchange.v2.c3p0.ComboPooledDataSource"
          destroy-method="close">
        <property name="driverClass" value="com.mysql.cj.jdbc.Driver" />
        <property name="jdbcUrl" value="jdbc:mysql://localhost:3306/web_customer_tracker?useSSL=false&amp;serverTimezone=UTC" />
        <property name="user" value="springstudent" />
        <property name="password" value="springstudent" /> 

        <!-- these are connection pool properties for C3P0 -->
        <property name="minPoolSize" value="5" />
        <property name="maxPoolSize" value="20" />
        <property name="maxIdleTime" value="30000" />
	</bean>  
	
    <!-- Step 2: Setup Hibernate session factory -->
	<bean id="sessionFactory"
		class="org.springframework.orm.hibernate5.LocalSessionFactoryBean">
		<property name="dataSource" ref="myDataSource" />
		<property name="packagesToScan" value="com.example.springdemo.entity" />
		<property name="hibernateProperties">
		   <props>
		      <prop key="hibernate.dialect">org.hibernate.dialect.MySQL8Dialect</prop>
		      <prop key="hibernate.show_sql">true</prop>
		   </props>
		</property>
   </bean>	  

    <!-- Step 3: Setup Hibernate transaction manager -->
	<bean id="myTransactionManager"
            class="org.springframework.orm.hibernate5.HibernateTransactionManager">
        <property name="sessionFactory" ref="sessionFactory"/>
    </bean>
    
    <!-- Step 4: Enable configuration of transactional behavior based on annotations -->
	<tx:annotation-driven transaction-manager="myTransactionManager" />

</beans>
```