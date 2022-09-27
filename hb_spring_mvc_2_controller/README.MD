This is Spring demo from Udemy Spring & Hibernate course.
- Using Spring with hibernate for CRUD

- Test Spring MVC Controller
- Customer Data Access Object(DAO)
    - responsible for interfacing with the DB
    - use hibernate to connect to DB
    - contains CRUD methods
        - saveCustomer(...)
        - getCustomer(...)
        - getCustomers()
        - updateCustomer(...)
        - deleteCustomer(...)

- List Customers
    1. Create Customer.java
    2. Create CustomerDAO.java
    1. and CustomerDAOImpl.java
    3. Create CustomerController.java
    4. Create JSP page: list-customers.jsp

- Entity scanning 
    - Spring config file add the packages to scan
    - <context:component-scan base-package="com.example.springdemo" />
        - recursively scanning sub directories in it
        - use comma to separate multiple packages to scan.

- Customer entity

```
package com.example.springdemo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="customer")
public class Customer {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private int id;
	
	@Column(name="first_name")
	private String firstName;
	
	@Column(name="last_name")
	private String lastName;
	
	@Column(name="email")
	private String email;
	
	public Customer() {}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "Customer [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", email=" + email + "]";
	}
}

```

- For Hibernate, DAO needs a Hibernate SessionFactory
- Hibernate SessionFactory needs a Data Source
    - Data source defines DB connection info
- All are dependencies - use Dependency injection

- Customer DAO
    1. Define DAO interface

        ```
        public interface CustomerDAO {
            public List<Customer> getCustomers();
        }
        ```

    2. Define DAO implementation
        - Inject the session factory
        - Add @Repository annotation
        - Add @Transactional on methods

        ```
        @Repository
        public class CustomerDAOImpl implements CustomerDAO {
            @Autowired
            private SessionFactory sessionFactory;

            @Transactional
            public List<Customer> getCustomers() {
            ...
            }
        }
        ```

- Spring @Transactional
    - Spring provides an @Transactional annotation
    - make sure import the current class : **org.springframework.transaction.annotation.Transactional**
    - Automagically begin and end a transaction for your Hibernate code
    - No need for you to explicitly do this in your code
    - This Spring magic happens behind the scenes

    ```
    @Transactional //org.springframework.transaction.annotation.Transactional
    public List<Customer> getCustomers() {
        // get the current hibernate session
        Session currentSession = sessionFactory.getCurrentSession();
        // create a query
        // import current one : org.hibernate.query.Query
        Query<Customer> theQuery =
            currentSession.createQuery("from Customer", Customer.class);
        // get the result list
        List<Customer> customers = theQuery.getResultList();
        return customers;
    }
    ```

- @Repository annotation : applied for DAO implementations
    - extends @Component
    - automatically register DAO implementations, thanks to component-scanning
    - translation of any JDBC-related Checked Exceptions to RuntimeException

- Query: make sure import correct one: org.hibernate.query.Query

- CustomerDAOImpl

```
package com.example.springdemo.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query; //import correct one
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional; //import correct one

import com.example.springdemo.entity.Customer;

@Repository
public class CustomerDAOImpl implements CustomerDAO {
	// need to inject session factory
	@Autowired
	private SessionFactory sessionFactory;

	@Override
	@Transactional
	public List<Customer> getCustomers() {
		// get the hibernate session
		Session session = sessionFactory.getCurrentSession();
		// create a query
		Query<Customer> query = session.createQuery("from Customer", Customer.class);
		// execute the query and get the result
		List<Customer> customers = query.getResultList();
		// return the result
		return customers;
	}

}

```

- Controller
    - inject the customer DAO

```
@Controller
@RequestMapping("/customer")
public class CustomerController {
	
	// need to inject the customer DAO
	@Autowired
	private CustomerDAO customerDAO;
	
	@RequestMapping("/list")
	public String listCustomers(Model model) {
		//get customers from dao
		List<Customer> customers = customerDAO.getCustomers();
		//add customers to the model
		model.addAttribute("customers",customers);
		return "list-customers";
	}
}

```

- jsp view file
    - to use css/js/images etc , we create a `resources` folder(can called any name) under WEB-INF
    - configure the resources directory in spring config file
    - <mvc:resources mapping="/resources/**" location="/resources/" />
        - note that location attribute need an ending slash `/resources/`
    - we create sub folders, such as css, js, images in the resources folder etc.
    - reference css in jsp: 

        ```
        <link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/style.css" />
        ```

```
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>

<html>

<head>
<title>List Customers</title>

<!-- reference our style sheet -->

<link type="text/css" rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/css/style.css" />

</head>

<body>

	<div id="wrapper">
		<div id="header">
			<h2>CRM - Customer Relationship Manager</h2>
		</div>
	</div>

	<div id="container">

		<div id="content">

			<!--  add our html table here -->

			<table>
				<tr>
					<th>First Name</th>
					<th>Last Name</th>
					<th>Email</th>
				</tr>

				<!-- loop over and print our customers -->
				<c:forEach var="tempCustomer" items="${customers}">

					<tr>
						<td>${tempCustomer.firstName}</td>
						<td>${tempCustomer.lastName}</td>
						<td>${tempCustomer.email}</td>
					</tr>

				</c:forEach>

			</table>

		</div>

	</div>


</body>

</html>
```