This is Spring demo from Udemy Spring & Hibernate course. 

- Multiple datasource settings

- spring config file: 

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
	<context:component-scan
		base-package="com.example.demo" />

	<!-- Add support for conversion, formatting and validation support -->
	<mvc:annotation-driven />

	<!-- Define Spring MVC view resolver -->
	<bean
		class="org.springframework.web.servlet.view.InternalResourceViewResolver">
		<property name="prefix" value="/WEB-INF/view/" />
		<property name="suffix" value=".jsp" />
	</bean>

	<!-- Add support for reading web resources: css, images, js, etc ... -->
	<mvc:resources location="/resources/"
		mapping="/resources/**"></mvc:resources>

	<!-- DEFINE FIRST DATA SOURCE -->

	<!-- Step 1: CUSTOMER - Define Database DataSource / connection pool -->
	<bean id="customerDataSource"
		class="com.mchange.v2.c3p0.ComboPooledDataSource"
		destroy-method="close">
		<property name="driverClass" value="com.mysql.cj.jdbc.Driver" />
		<property name="jdbcUrl"
			value="jdbc:mysql://localhost:3306/web_customer_tracker?useSSL=false&amp;serverTimezone=UTC" />
		<property name="user" value="springstudent" />
		<property name="password" value="springstudent" />

		<!-- these are connection pool properties for C3P0 -->
		<property name="minPoolSize" value="5" />
		<property name="maxPoolSize" value="20" />
		<property name="maxIdleTime" value="30000" />
	</bean>

	<!-- Step 2: CUSTOMER - Setup Hibernate session factory -->
	<bean id="customerSessionFactory"
		class="org.springframework.orm.hibernate5.LocalSessionFactoryBean">
		<property name="dataSource" ref="customerDataSource" />
		<property name="packagesToScan"
			value="com.example.demo.datasources.entity" />
		<property name="hibernateProperties">
			<props>
				<prop key="hibernate.dialect">org.hibernate.dialect.MySQLDialect</prop>
				<prop key="hibernate.show_sql">true</prop>
			</props>
		</property>
	</bean>

	<!-- Step 3: CUSTOMER - Setup Hibernate transaction manager -->
	<bean id="customerTransactionManager"
		class="org.springframework.orm.hibernate5.HibernateTransactionManager">
		<property name="sessionFactory" ref="customerSessionFactory" />
	</bean>

	<!-- Step 4: CUSTOMER - Enable configuration of transactional behavior based 
		on annotations -->
	<tx:annotation-driven
		transaction-manager="customerTransactionManager" />

	<!-- REPEAT PROCESS FOR SECOND DATASOURCE -->

	<!-- Step 1: Define Database DataSource / connection pool -->
	<bean id="employeeDataSource"
		class="com.mchange.v2.c3p0.ComboPooledDataSource"
		destroy-method="close">
		<property name="driverClass" value="com.mysql.cj.jdbc.Driver" />
		<property name="jdbcUrl"
			value="jdbc:mysql://localhost:3306/employee_directory?useSSL=false&amp;serverTimezone=UTC" />
		<property name="user" value="springstudent" />
		<property name="password" value="springstudent" />

		<!-- these are connection pool properties for C3P0 -->
		<property name="minPoolSize" value="5" />
		<property name="maxPoolSize" value="20" />
		<property name="maxIdleTime" value="30000" />
	</bean>

	<!-- Step 2: Setup Hibernate session factory -->
	<bean id="employeeSessionFactory"
		class="org.springframework.orm.hibernate5.LocalSessionFactoryBean">
		<property name="dataSource" ref="employeeDataSource" />
		<property name="packagesToScan"
			value="com.example.demo.datasources.entity" />
		<property name="hibernateProperties">
			<props>
				<prop key="hibernate.dialect">org.hibernate.dialect.MySQLDialect</prop>
				<prop key="hibernate.show_sql">true</prop>
			</props>
		</property>
	</bean>

	<!-- Step 3: Setup Hibernate transaction manager -->
	<bean id="employeeTransactionManager"
		class="org.springframework.orm.hibernate5.HibernateTransactionManager">
		<property name="sessionFactory" ref="employeeSessionFactory" />
	</bean>

	<!-- Step 4: Enable configuration of transactional behavior based on annotations -->
	<tx:annotation-driven
		transaction-manager="employeeTransactionManager" />
</beans>
```

- service implementation

```
package com.example.demo.datasources.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.datasources.dao.CustomerDAO;
import com.example.demo.datasources.dao.EmployeeDAO;
import com.example.demo.datasources.entity.Customer;
import com.example.demo.datasources.entity.Employee;

@Service
public class MultiDataSourceServiceImpl implements MultiDataSourceService {

	@Autowired
	private CustomerDAO customerDAO;
	
	@Autowired
	private EmployeeDAO employeeDAO;
	
	@Override
	public List<Customer> getCustomers() {
		return customerDAO.getCustomers();
	}

	@Override
	public List<Employee> getEmployees() {
		return employeeDAO.getEmployees();
	}

}

```

- One of the DAO

```
package com.example.demo.datasources.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.datasources.entity.Employee;

@Repository
public class EmployeeDAOImpl implements EmployeeDAO {

	// need to inject the session factory
	@Autowired
	@Qualifier("employeeSessionFactory")
	private SessionFactory sessionFactory;
			
	@Override
	@Transactional("employeeTransactionManager")
	public List<Employee> getEmployees() {
		
		// get the current hibernate session
		Session currentSession = sessionFactory.getCurrentSession();
				
		// create a query  ... sort by last name
		Query<Employee> theQuery = 
				currentSession.createQuery("from Employee order by lastName",
						Employee.class);
		
		// execute query and get result list
		List<Employee> employees = theQuery.getResultList();
				
		// return the results		
		return employees;
	}


}
```

- Controller

```
package com.example.demo.datasources.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.datasources.entity.Customer;
import com.example.demo.datasources.entity.Employee;
import com.example.demo.datasources.service.MultiDataSourceService;

@Controller
public class DemoController {

	@Autowired
	private MultiDataSourceService multiDataSourceService;
	
	@GetMapping("/")
	public String getData(Model theModel) {
		
		// customers
		List<Customer> theCustomers = multiDataSourceService.getCustomers();
		theModel.addAttribute("customers", theCustomers);
		
		// employees
		List<Employee> theEmployees = multiDataSourceService.getEmployees();
		theModel.addAttribute("employees", theEmployees);
		
		return "display-results";
	}
}

```












