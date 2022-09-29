package com.example.demo.datasources.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.datasources.dao.CustomerDAO;
import com.example.demo.datasources.dao.EmployeeDAO;
import com.example.demo.datasources.entity.Customer;
import com.example.demo.datasources.entity.Employee;

@Service
public class MultiDataSourceServiceImpl implements MultiDataSourceService {

	@Autowired
	private CustomerDAO customerDAO;
	
	@Autowired
	private EmployeeDAO employeeDAO;
	
	@Override
	public List<Customer> getCustomers() {
		return customerDAO.getCustomers();
	}

	@Override
	public List<Employee> getEmployees() {
		return employeeDAO.getEmployees();
	}

}
