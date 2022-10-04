package com.example.springboot.cruddemo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.springboot.cruddemo.dao.EmployeeDAO;
import com.example.springboot.cruddemo.dao.EmployeeRepository;
import com.example.springboot.cruddemo.entity.Employee;

@Service
public class EmployeeServiceSpringDataImpl implements EmployeeService {
	// inject EmployeeRepository
	@Autowired
	private EmployeeRepository employeeRepository;

	@Override
	public List<Employee> findAll() {
		return employeeRepository.findAll();
	}

	@Override
	public void save(Employee c) {
		employeeRepository.save(c);
	}

	@Override
	public Employee findById(int id) {
		Optional<Employee> result = employeeRepository.findById(id);
		if (result.isPresent()) {
			return result.get();
		} else {
			throw new RuntimeException("Employee id:" + id + " not exist!");
		}
	}

	@Override
	public void deleteById(int id) {
		employeeRepository.deleteById(id);

	}

}
