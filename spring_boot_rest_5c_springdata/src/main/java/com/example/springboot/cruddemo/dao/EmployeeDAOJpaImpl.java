package com.example.springboot.cruddemo.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.example.springboot.cruddemo.entity.Employee;

@Repository
public class EmployeeDAOJpaImpl implements EmployeeDAO {

	// define entityManager
	private EntityManager entityManager;

	// setup constructor injection
	@Autowired
	public EmployeeDAOJpaImpl(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	public List<Employee> findAll() {
		// create a query
		TypedQuery<Employee> query = entityManager.createQuery("from Employee", Employee.class);
		// Query query = entityManager.createQuery("from Employee");
		List<Employee> result = query.getResultList();
		return result;
	}

	@Override
	public void save(Employee e) {
		Employee e2 = entityManager.merge(e);
		e.setId(e2.getId());
	}

	@Override
	public Employee findById(int id) {
		// create a query
		Employee employee = entityManager.find(Employee.class, id);
		return employee;
	}

	@Override
	public void deleteById(int id) {
		// create a query
		Query query = entityManager.createQuery("delete from Employee where id=:id");
		query.setParameter("id", id);
		query.executeUpdate();
	}

}
