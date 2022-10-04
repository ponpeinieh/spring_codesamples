package com.example.springboot.thymeleaf_demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.springboot.thymeleaf_demo.dao.EmployeeRepository;
import com.example.springboot.thymeleaf_demo.entity.Employee;

@Service
public class EmployeeServiceImpl implements EmployeeService {
	// inject EmployeeRepository
	@Autowired
	private EmployeeRepository employeeRepository;

	@Override
	public List<Employee> findAll() {
		return employeeRepository.findAllByOrderByLastNameAsc();
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
