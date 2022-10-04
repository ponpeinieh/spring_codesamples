package com.example.springboot.cruddemo.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.springboot.cruddemo.entity.Employee;
import com.example.springboot.cruddemo.service.EmployeeService;

@RestController
@RequestMapping("/api")
public class EmployeeRestController {
	// inject EmployeeService
	@Autowired
	@Qualifier("employeeServiceSpringDataImpl")
	private EmployeeService employeeService;

	// expose /employees end point and return list of employees
	@GetMapping("/employees")
	public List<Employee> findAll() {
		return employeeService.findAll();
	}

	// expose /employees/{employeeId} end point and return an employee
	@GetMapping("/employees/{employeeId}")
	public Employee findById(@PathVariable int employeeId) {
		Employee e = employeeService.findById(employeeId);
		return e;
	}

	// expose /employees end point and create an employee
	@PostMapping("/employees")
	public Employee save(@RequestBody Employee employee) {
		employee.setId(0);
		employeeService.save(employee);
		return employee;
	}

	// expose /employees end point and update an employee
	@PutMapping("/employees")
	public Employee update(@RequestBody Employee employee) {
		employeeService.save(employee);
		return employee;
	}

	// expose /employees/{employeeId} end point and delete an employee
	@DeleteMapping("/employees/{employeeId}")
	public String deleteById(@PathVariable int employeeId) {
		Employee e = employeeService.findById(employeeId);
		if (e == null) {
			throw new RuntimeException("Employee id:" + employeeId + " not exist!");
		}
		employeeService.deleteById(employeeId);
		return "Employee id:" + employeeId + " is deleted!";
	}
}
