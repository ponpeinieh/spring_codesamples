This is Spring demo from Udemy Spring & Hibernate course. 

- Add Customer
	1. Update list-customer.jsp
	2. New “Add Customer” button

		```
			<!--  add button for new customer -->
			<input type="button" value="Add Customer" class="add-button" onclick="window.location.href='showFormForAdd';return false;"/>
		```
	3. Create HTML form for new customer

		```
		<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
		<html>
		<head>
		<title>Customer form</title>

		<link type="text/css" rel="stylesheet"
			href="${pageContext.request.contextPath}/resources/css/style.css" />
		<link type="text/css" rel="stylesheet"
			href="${pageContext.request.contextPath}/resources/css/add-customer-style.css" />

		</head>
		<body>
			<div id="wrapper">
				<div id="header">
					<h2>CRM - Customer Relationship Manager</h2>
				</div>
			</div>

			<div id="container">

				<h3>Save Customer</h3>
				<form:form action="saveCustomer" modelAttribute="customer"
					method="POST">
					<table>
						<tbody>
							<tr>
								<td><label>First name:</label></td>
								<td><form:input path="firstName" /></td>
							</tr>
							<tr>
								<td><label>Last name:</label></td>
								<td><form:input path="lastName" /></td>
							</tr>
							<tr>
								<td><label>Email:</label></td>
								<td><form:input path="email" /></td>
							</tr>
							<tr>
								<td><label></label></td>
								<td><input type="submit" value="Save" class="save" /></td>
							</tr>
						</tbody>
					</table>
				</form:form>
				<p>
					<a href="${pageContext.request.contextPath}/customer/list">Back
						to List</a>
				</p>
			</div>
		</body>
		</html>
		```

	4. Process Form Data
		- controller

		```
		@GetMapping("/showFormForAdd")
		public String showFormForAdd(Model model) {
			Customer c = new Customer();
			model.addAttribute("customer",c);
			return "customer-form";
		}
		@PostMapping("/saveCustomer")
		public String saveCustomer(@ModelAttribute("customer") Customer c) {
			customerService.saveCustomer(c);
			return "redirect:/customer/list";
		}
		```

	5. Controller -> Service -> DAO
		- DAO

		```
		@Override
		public void saveCustomer(Customer c) {
			Session session = sessionFactory.getCurrentSession();
			session.save(c);
		}
		```

- To check mysql database collation
	- SELECT @@character_set_database, @@collation_database;
	- `utf8mb4`, `utf8mb4_0900_ai_ci` (`ci` means case-insensitive)
- Show all collations
	- SHOW COLLATION;
- For database with collation with _ci, the `order by clause` will carry out the sorting in **case insensitive** fashion. 
	- You can force a case-sensitive sort for a column by using the keyword **BINARY after ORDER BY clause.**
		- SELECT * FROM web_customer_tracker.customer order by binary last_name;
		- In HQL, use binary()
			- Query<Customer> query = session.createQuery("from Customer order by binary(lastName)", Customer.class);
		

-  Update Customer
	1. Update list-customers.jsp
	2. New “Update” link

	```
	<c:forEach var="tempCustomer" items="${customers}">
		<!-- construct an url -->
		<c:url var="updateLink" value="/customer/showFormForUpdate">
			<c:param name="customerId" value="${tempCustomer.id}"/>
		</c:url>
		<tr>
			<td>${tempCustomer.firstName}</td>
			<td>${tempCustomer.lastName}</td>
			<td>${tempCustomer.email}</td>
			<td><a href="${updateLink}">Update</a></td>
		</tr>

	</c:forEach>
	```

	3. Create customer-form.jsp
	4. Prepopulate the form
		- reuse the form jsp, but add a hidden input data
			- <form:hidden path="id"/>
	5. Process form data
		- reuse the controller method

	6. Controller > Service > DAO
		- modify DAO method
			- call session.saveOrUpdate() instead of session.save()

		```
		@Override
		public void saveCustomer(Customer c) {
			Session session = sessionFactory.getCurrentSession();
			session.saveOrUpdate(c);
		}
		```

- Delete Customer
	1. Add “Delete” link on JSP

		```
		<c:forEach var="tempCustomer" items="${customers}">
		<!-- construct urls -->
				<c:url var="updateLink" value="/customer/showFormForUpdate">
					<c:param name="customerId" value="${tempCustomer.id}"/>
				</c:url>
				<c:url var="deleteLink" value="/customer/delete">
					<c:param name="customerId" value="${tempCustomer.id}"/>
				</c:url>
			<tr>
				<td>${tempCustomer.firstName}</td>
				<td>${tempCustomer.lastName}</td>
				<td>${tempCustomer.email}</td>
				<td><a href="${updateLink}">Update</a>|
				<a href="${deleteLink}" onclick="if(!(confirm('Confirm to delete?'))) return false;">Delete</a></td>
			</tr>

		</c:forEach>
		```

	2. Add code for “Delete”
	3. Controller > Service > DAO
		- DAO

			```
			@Override
			public void deleteCustomer(int id) {
				Session session = sessionFactory.getCurrentSession();
				//delete object with primary key
				Query q = session.createQuery("delete from Customer where id=:id");
				q.setParameter("id", id);
				q.executeUpdate();
			}
			```