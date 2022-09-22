package com.example.hibernate.demo;

import org.hibernate.cfg.Configuration;

import com.example.hibernate.demo.entity.Customer;
import com.example.hibernate.demo.entity.Student;

public class UpdateCustomerDemo {

	public static void main(String[] args) {
		// Create a SessionFactory
		var sessionFactory = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(Customer.class)
				.buildSessionFactory();
		// Create a Session
		var session = sessionFactory.getCurrentSession();
		try {
			int customerId = 1;
			session.beginTransaction();
			// update the student
			Customer s = session.get(Customer.class, customerId);
			System.out.println("Update customer object ...");
			s.setEmail("milkman@gmail.com");
			session.getTransaction().commit();
			//
			session = sessionFactory.getCurrentSession();
			session.beginTransaction();
			//
			 
			System.out.println("Done!");
		} finally {
			sessionFactory.close();
		}

	}

}
