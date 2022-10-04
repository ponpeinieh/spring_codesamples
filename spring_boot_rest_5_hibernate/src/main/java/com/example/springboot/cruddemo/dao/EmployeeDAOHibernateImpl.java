package com.example.springboot.cruddemo.dao;

import java.util.List;

import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.hibernate.query.Query;//make sure you import correct one
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.example.springboot.cruddemo.entity.Employee;

@Repository
public class EmployeeDAOHibernateImpl implements EmployeeDAO {

	// define entityManager
	private EntityManager entityManager;

	// setup constructor injection
	@Autowired
	public EmployeeDAOHibernateImpl(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	public List<Employee> findAll() {
		// get the current hibernate session
		Session currentSession = entityManager.unwrap(Session.class);
		// create a query
		Query<Employee> query = currentSession.createQuery("from Employee", Employee.class);
		List<Employee> result = query.getResultList();
		return result;
	}

	@Override
	public void save(Employee c) {
		Session currentSession = entityManager.unwrap(Session.class);
		currentSession.saveOrUpdate(c);
	}

	@Override
	public Employee findById(int id) {
		Session currentSession = entityManager.unwrap(Session.class);
		// create a query
		Employee employee = currentSession.get(Employee.class, id);
		return employee;
	}

	@Override
	public void deleteById(int id) {
		Session currentSession = entityManager.unwrap(Session.class);
		// create a query
		Query query = currentSession.createQuery("delete from Employee where id=:id");
		query.setParameter("id", id);
		query.executeUpdate();
	}

}
