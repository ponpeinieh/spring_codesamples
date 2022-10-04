package com.example.springboot.thymeleaf_demo.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.springboot.thymeleaf_demo.entity.Employee;

@Controller
@RequestMapping("/employees")
public class EmployeeController implements InitializingBean {
	private List<Employee> theEmployees = new ArrayList<>();

	@GetMapping("/list")
	public String listEmployees(Model theModel) {
		theModel.addAttribute("employees", theEmployees);
		return "list-employees";
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		theEmployees.add(new Employee("John", "Smith", "js@example.com"));
		theEmployees.add(new Employee("Jane", "Smith", "j2s@example.com"));
		theEmployees.add(new Employee("Steve", "Oat", "so@example.com"));
		theEmployees.add(new Employee("Peter", "Nie", "pn@example.com"));

	}
}
