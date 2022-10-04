This is Spring demo from Udemy Spring & Hibernate course.   
- Thymeleaf - CRUD

- Show Employees 

	- controller

	```
	@GetMapping("/list")
	public String listEmployees(Model theModel) {
		List<Employee> theEmployees = employeeService.findAll();
		theModel.addAttribute("employees", theEmployees);
		return "employees/list-employees";
	}
	```

	- for sorting by last name
		- add `public List<Employee> findAllByOrderByLastNameAsc();`
		- Spring Data JPA can fill in the details based on method name
			- findAllBy + OrderBy + LastName + Asc

	- Repository 

	```
	package com.example.springboot.thymeleaf_demo.dao;

	import java.util.List;

	import org.springframework.data.jpa.repository.JpaRepository;

	import com.example.springboot.thymeleaf_demo.entity.Employee;
	
	public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
		//add a method to sort by last name
		
		public List<Employee> findAllByOrderByLastNameAsc();
	}
	```
	- service

	```
	@Service
	public class EmployeeServiceImpl implements EmployeeService {
		// inject EmployeeRepository
		@Autowired
		private EmployeeRepository employeeRepository;

		@Override
		public List<Employee> findAll() {
			return employeeRepository.findAllByOrderByLastNameAsc();
		}

	```
	
- Add Employee
	- Add Employee button
		- @ symbol to Reference context path of your application(app root)

	```
	<a th:href="@{/employees/showFormForAdd}"
	class="btn btn-primary btn-sm mb-3"> Add Employee </a>
	```

	```
	@GetMapping("/showFormForAdd")
	public String showFormForAdd(Model theModel) {
		Employee theEmployee = new Employee();
		theModel.addAttribute("employee", theEmployee);
		return "employees/employee-form";
	}
	```

	- Thymeleaf expressions can help you build the HTML form :

	|Expression| Description|
	|---|---|
	|th:action| Location to send form data|
	|th:object| Reference to model attribute|
	|th:field| Bind input field to a property on model attribute|
     
	 - form :
	 	- action="#" : Empty place holder , so Thymeleaf will handle real work
		- When form is loaded, will call getter methods
		- When form is submitted, will call setter methods

	 	```
	 	<form action="#" th:action="@{/employees/save}"
			th:object="${employee}" method="POST">
			<input type="hidden" th:field="*{id}"/> <!-- this is for update action-->
			<input type="text" th:field="*{firstName}" placeholder="First name"
				class="form-control mb-4 col-4" /> 
				<input type="text" th:field="*{lastName}" placeholder="Last name"
				class="form-control mb-4 col-4" /> 
				<input type="text" th:field="*{email}" placeholder="Email"
				class="form-control mb-4 col-4" />
			<button type="submit" class="btn btn-info col-2">Save</button>
		</form>
		```
	- controller:
		- **use redirect, so to prevent duplicate submission**
		- use post/redirect/get pattern
	```
	@PostMapping("/save")
	public String saveEmployee(@ModelAttribute("employee") Employee theEmployee) {
		employeeService.save(theEmployee);

		// use a redirect to prevent duplicate submissions
		// use Post/Redirect/Get pattern
		return "redirect:/employees/list";
	}
	```

- Update button:
	- Appends to URL (request parameters) 
		- (employeeId=${tempEmployee.id}) -> `?employeeId=xxx`
		- The controller method uses `@RequestParam("employeeId")` to read the parameter

	```
	<a th:href="@{/employees/showFormForUpdate(employeeId=${tempEmployee.id})}" 
						class="btn btn-info btn-sm">Update</a>
	```

	```
	@GetMapping("/showFormForUpdate")
	public String showFormForAdd(@RequestParam("employeeId") int employeeId, Model theModel) {
		Employee theEmployee = employeeService.findById(employeeId);
		theModel.addAttribute("employee", theEmployee);
		return "employees/employee-form";
	}
	```

	- Hidden form field required for updates
		- <input type="hidden" th:field="*{id}"/>

	```
	<form action="#" th:action="@{/employees/save}"
		th:object="${employee}" method="POST">
		<input type="hidden" th:field="*{id}"/>
		<input type="text" th:field="*{firstName}" placeholder="First name"
			class="form-control mb-4 col-4" /> 
			<input type="text" th:field="*{lastName}" placeholder="Last name"
			class="form-control mb-4 col-4" /> 
			<input type="text" th:field="*{email}" placeholder="Email"
			class="form-control mb-4 col-4" />
		<button type="submit" class="btn btn-info col-2">Save</button>
	</form>
	```
	- No need for new code ... we can reuse our existing code. Works the same for add or update :-)


- Delete button:

```
<a th:href="@{/employees/delete(employeeId=${tempEmployee.id})}" 
	class="btn btn-danger btn-sm" 
	onclick="if (!confirm('Please confirm if delete this employee?')) return false">Delete</a>
```

```
@GetMapping("/delete")
public String deleteEmployee(@RequestParam("employeeId") int employeeId) {
	employeeService.deleteById(employeeId);
	return "redirect:/employees/list";
}
```