This is Spring demo from Udemy Spring & Hibernate course.  

Spring Boot - REST CRUD API with Hibernate
- Various DAO implementation techniques
	- Version 1: Use EntityManager but **leverage native Hibernate API**
	- Version 2: Use EntityManager and standard JPA API
	- Version 3: Use EntityManager and Spring Data JPA 

- Version 3: Use EntityManager and Spring Data JPA 
	- Spring Data JPA : problem to solve
		- Need to create a DAO for another entity? Customer, Student, Product, Book ...
		- Do we have to repeat all of the same code again???
		- Most of the code is the same. Only difference is the entity type and primary key
		- Why not create a DAO for me, plug in my entity type and primary key, give me all of the basic CRUD features for free
	- Spring Data JPA 
		- Create a DAO and just plug in your entity type and primary key
		- Spring will give you a CRUD implementation for FREE
		- Interface: JpaRepository
			- Exposes methods (some by inheritance from parents)
				- findAll()
				- findById(...)
				- save(...)
				- deleteById(...)
		- Development Process
			1. Extend JpaRepository interface. No need for implementation class.
			
			```
			public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
				// that's it ... no need to write any code
			}
			```

			2. Use your Repository in your app. 

			```
			@Service
			public class EmployeeServiceImpl implements EmployeeService {

				private EmployeeRepository employeeRepository;
			 
				@Autowired
				public EmployeeServiceImpl(EmployeeRepository theEmployeeRepository) {
					employeeRepository = theEmployeeRepository;
				}

				@Override
				public List<Employee> findAll() {
					return employeeRepository.findAll();
				}
			```
		- Advanced Features :
 			- Extending and adding custom queries with JPQL
			- Query Domain Specific Language (Query DSL)
			- Defining custom methods (low-level coding)

