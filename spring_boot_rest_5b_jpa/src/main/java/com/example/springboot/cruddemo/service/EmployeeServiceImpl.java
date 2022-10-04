package com.example.springboot.cruddemo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.springboot.cruddemo.dao.EmployeeDAO;
import com.example.springboot.cruddemo.entity.Employee;

@Service
public class EmployeeServiceImpl implements EmployeeService {
	// inject EmployeeDAO
	@Autowired
	@Qualifier("employeeDAOJpaImpl")
	private EmployeeDAO employeeDAO;

	@Override
	@Transactional
	public List<Employee> findAll() {
		return employeeDAO.findAll();
	}

	@Override
	@Transactional
	public void save(Employee c) {
		employeeDAO.save(c);
	}

	@Override
	@Transactional
	public Employee findById(int id) {
		return employeeDAO.findById(id);
	}

	@Override
	@Transactional
	public void deleteById(int id) {
		employeeDAO.deleteById(id);

	}

}
