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
