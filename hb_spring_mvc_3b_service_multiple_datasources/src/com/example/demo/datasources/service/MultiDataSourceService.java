package com.example.demo.datasources.service;

import java.util.List;

import com.example.demo.datasources.entity.Customer;
import com.example.demo.datasources.entity.Employee;

public interface MultiDataSourceService {

	public List<Customer> getCustomers();
	
	public List<Employee> getEmployees();
	
}
