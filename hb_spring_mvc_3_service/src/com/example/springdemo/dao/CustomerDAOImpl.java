package com.example.springdemo.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.springdemo.entity.Customer;

@Repository
public class CustomerDAOImpl implements CustomerDAO {
	// need to inject session factory
	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public List<Customer> getCustomers() {
		// get the hibernate session
		Session session = sessionFactory.getCurrentSession();
		// create a query
		Query<Customer> query = session.createQuery("from Customer", Customer.class);
		// execute the query and get the result
		List<Customer> customers = query.getResultList();
		// return the result
		return customers;
	}

}
