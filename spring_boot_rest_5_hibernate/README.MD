This is Spring demo from Udemy Spring & Hibernate course.  

Spring Boot - REST CRUD API with Hibernate
- Create a REST API for the Employee Directory
- REST clients should be able to
	- Get a list of employees
 	- Get a single employee by id
 	- Add a new employee
 	- Update an employee
 	- Delete an employee
- End points:
	- POST /api/employees : Create a new employee
	- GET /api/employees : Read a list of employees
	- GET /api/employees/{employeeId} : Read a single employee
	- PUT /api/employees : Update an existing employee
	- DELETE /api/employees/{employeeId} : Delete an existing employee
- Development Process
	1. Set up Database Dev Environment
		- Create Employee table
	2. Create Spring Boot project using Spring Initializr
		- Dependencies choose
			- Spring Web Starter
			- Spring Data JPA
			- Spring Boot DevTools
			- MySQL Driver
			
	3. Create DAO's
		- task to do:
			1. Get list of employees
			2. Get single employee by ID
			3. Add a new employee
			4. Update an existing employee
			5. Delete an existing employee
		- Hibernate Session Factory:
			- In the past, our DAO used a Hibernate Session Factory
				- **Hibernate Session Factory needs a Data Source**
				- The data source defines database connection info
				- We normally had to do this configuration manually
			- Spring Boot to the Rescue
				- **Spring Boot will automatically configure your data source for you**
					- Based on entries from Maven pom file
						- JDBC Driver: mysql-connector-java
						- Spring Data (ORM): spring-boot-starter-data-jpa
					- DB connection info from application.properties
						- No need to give JDBC driver class name (Spring Boot will automatically detect it based on URL)

					```
					spring.datasource.url=jdbc:mysql://localhost:3306/employee_directory
					spring.datasource.username=springstudent
					spring.datasource.password=springstudent
					```

		- Auto Data Source Configuration
			- **Based on configs, Spring Boot will automatically create the beans**:
				- **DataSource**, **EntityManager**(from Java Persistence API, JPA), ...
			- You can then inject these into your DAOs
		- JPA:
			- Standard API for Object-to-Relational-Mapping (ORM)
			- Only a specification
				- Defines a set of interfaces
				- Requires an implementation to be usable, eg. Hibernate, EclipseLink
			- In Spring Boot, Hibernate is the default implementation of JPA
			- **EntityManager is similar to Hibernate SessionFactory**
				- EntityManager can serve as a wrapper for a Hibernate Session object
				- We can inject the EntityManager into our DAO

- Various DAO implementation techniques
	- Version 1: Use EntityManager but **leverage native Hibernate API**
	- Version 2: Use EntityManager and standard JPA API
	- Version 3: Use EntityManager and Spring Data API


- Version 1: Use EntityManager but leverage native Hibernate API

	```
	@Repository
	public class EmployeeDAOHibernateImpl implements EmployeeDAO {

		private EntityManager entityManager;
		@Autowired		//Optional, if only one constructor defined
		public EmployeeDAOHibernateImpl(EntityManager theEntityManager) { //contstuctor injection
			entityManager = theEntityManager;
		}

		@Override
		public List<Employee> findAll() {
			
			// get the current hibernate session
			Session currentSession = entityManager.unwrap(Session.class);
			// create a query
			Query<Employee> theQuery = currentSession.createQuery("from Employee", Employee.class); //use native Hibernate API
			// execute query and get result list
			List<Employee> employees = theQuery.getResultList();
			// return the results
			return employees;
		}

		@Override
		public Employee save(Employee c) {
			Session currentSession = entityManager.unwrap(Session.class);
			currentSession.saveOrUpdate(c);
			return c;
		}

		@Override
		public Employee findById(int id) {
			Session currentSession = entityManager.unwrap(Session.class);
			// create a query
			Employee employee = currentSession.get(Employee.class, id);
			return employee;
		}

		@Override
		public void deleteById(int id) {
			Session currentSession = entityManager.unwrap(Session.class);
			// create a query
			Query query = currentSession.createQuery("delete from Employee where id=:id");
			query.setParameter("id", id);
			query.executeUpdate();
		}
	}
	```
