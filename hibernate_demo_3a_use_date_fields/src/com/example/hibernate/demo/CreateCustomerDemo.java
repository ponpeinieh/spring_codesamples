package com.example.hibernate.demo;

import java.text.ParseException;

import org.hibernate.cfg.Configuration;

import com.example.hibernate.demo.entity.Customer;
import com.example.hibernate.utils.DateUtils;

public class CreateCustomerDemo {

	public static void main(String[] args) {
		// Create a SessionFactory
		var sessionFactory = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(Customer.class)
				.buildSessionFactory();
		// Create a Session
		var session = sessionFactory.getCurrentSession();
		// create a Student object
		try {
			Customer c1 = new Customer("John", "Smith", "jsmith@example.com", DateUtils.parseDate("30/5/2001"));
			System.out.println("Saving the customer object!");
			session.beginTransaction();
			session.save(c1);
			session.getTransaction().commit();
			System.out.println("Done!");

		} catch (ParseException e) {
			e.printStackTrace();
		} finally {
			sessionFactory.close();
		}
	}

}
