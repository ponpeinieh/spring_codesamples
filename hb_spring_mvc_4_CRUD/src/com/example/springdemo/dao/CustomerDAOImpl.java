package com.example.springdemo.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

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
		Query<Customer> query = session.createQuery("from Customer order by binary(lastName)", Customer.class);
		// execute the query and get the result
		List<Customer> customers = query.getResultList();
		// return the result
		return customers;
	}

	@Override
	public void saveCustomer(Customer c) {
		Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(c);
	}

	@Override
	public Customer getCustomer(int id) {

		Session session = sessionFactory.getCurrentSession();
		return session.get(Customer.class, id);
		 
	}

	@Override
	public void deleteCustomer(int id) {

		Session session = sessionFactory.getCurrentSession();
		//delete object with primary key
		Query q = session.createQuery("delete from Customer where id=:id");
		q.setParameter("id", id);
		q.executeUpdate();
		 
	}

}
