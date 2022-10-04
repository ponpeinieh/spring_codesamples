This is Spring demo from Udemy Spring & Hibernate course.  

Spring Boot - Spring Data REST API
- Problem to solve:
	- We saw how to create a REST API for Employee, but we need to create REST API for another entity? Customer, Student, Product ...
	- I wish we could tell Spring: Create a REST API for me. Use my existing JpaRepository (entity, primary key). Give me all of the basic REST API CRUD features for free
-  Spring Data REST will expose these endpoints for free!
	- **Note that for PUT, the end point is `/employees/{employeeId}`, which means you only pass the id in the url, not in the request body, which is ignored.**

	|HTTP Method|End Point |CRUD Action|
	|---|---|---|
	|POST|/employees|Create a new employee|
	|GET|/employees|Read a list of employees|
	|GET|/employees/{employeeId}|Read a single employee|
	|PUT|/employees/{employeeId}|Update an existing employee|
	|DELETE|/employees/{employeeId}|Delete an existing employee|
- Spring Data REST will scan your project for JpaRepository
	- Expose REST APIs for each entity type for your JpaRepository
	- By default, Spring Data REST will create endpoints based on entity type
		- Simple pluralized form
			- First character of Entity type is lowercase
			- Then just adds an "s" to the entity
			- For example, the Employee entity will get `/employees` end point

			```
			public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
			}
			```

- Development Process
	1. Add Spring Data REST to your Maven POM file

	```
	<dependency>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-data-rest</artifactId>
	</dependency>
	```


- For Spring Data REST, you only need 3 items
	1. Your entity: Employee
	2. JpaRepository: EmployeeRepository extends JpaRepository
	3. Maven POM dependency for: spring-boot-starter-data-rest

- HATEOAS (Hypermedia as the Engine of Application State)
	- Spring Data REST endpoints are HATEOAS compliant
		- Hypermedia-driven sites provide information to access REST interfaces
		- Think of it as meta-data for REST data
	- Spring Data REST response using HATEOAS
		- HATEOAS uses `Hypertext Application Language (HAL)` data format
		- For example REST response from: GET /employees/3
			- The '_links' below is the response meta-data (links to data)

			```
			{
				"firstName": "Avani",
				"lastName": "Gupta",
				"email": "avani@luv2code.com",
				"_links": {
					"self": {
						"href": "http://localhost:8080/employees/3"
					},
					"employee": {
						"href": "http://localhost:8080/employees/3"
					}
				}
			}
			```

		- For a collection, meta-data includes page size, total elements, pages etc
			- For example REST response from: GET /employees
				- The 'page' below is the response meta-data (infomation about the page)


			```
			{
				"_embedded": {
					"employees": [
						{
						"firstName":...
						},
						...
					]
				},
				"page": {
					"size": 20,
					"totalElements": 5,
					"totalPages": 1,
					"number": 0
				}
			}
			```

- Spring Data REST advanced features
	- Pagination, sorting and searching
	- Extending and adding custom queries with JPQL
	- Query Domain Specific Language (Query DSL)

- Customize end point base path
	- add into application.properties

	```
	spring.data.rest.base-path=magic-api
	```

- Problem:
	- Spring Data REST does not handle complex pluralized forms
	- In this case, you need to specify plural name
	- What if we want to expose a different resource name? Instead of /employees ... use /members

- Solution:
	- Specify plural name / path with an annotation
	
	```
	@RepositoryRestResource(path="members")
	public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

	}
	```
- Pagination
	- By default, Spring Data REST will return the first 20 elements (Page size = 20)
	- You can navigate to the different pages of data using query param
		- page is zero based

	```
	http://localhost:8080/employees?page=0
	http://localhost:8080/employees?page=1
	```

- Spring Data REST Configuration - specify them in application.properties
	-	prefixed with spring.data.rest.*

	|Name| Description|
	|---|---|
	|spring.data.rest.base-path |Base path used to expose repository resources|
	|spring.data.rest.default-page-size |Default size of pages|
	|spring.data.rest.max-page-size |Maximum size of pages|

- Sorting : You can sort by the property names of your entity
	- In our Employee example, we have: firstName, lastName and email
	- Sort by last name (ascending is default)
 		- http://localhost:8080/employees?sort=lastName
	- Sort by first name, descending
 		- http://localhost:8080/employees?sort=firstName,desc
	- Sort by last name, then first name, ascending
		- http://localhost:8080/employees?sort=lastName,firstName,asc