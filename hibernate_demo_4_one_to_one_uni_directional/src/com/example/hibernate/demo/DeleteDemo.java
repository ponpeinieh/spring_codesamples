package com.example.hibernate.demo;

import org.hibernate.cfg.Configuration;

import com.example.hibernate.demo.entity.Instructor;
import com.example.hibernate.demo.entity.InstructorDetail;

public class DeleteDemo {

	public static void main(String[] args) {
		// Create a SessionFactory
		var sessionFactory = new Configuration().configure("hibernate.cfg.xml")
				.addAnnotatedClass(Instructor.class)
				.addAnnotatedClass(InstructorDetail.class)
				.buildSessionFactory();
		// Create a Session
		var session = sessionFactory.getCurrentSession();
		// retrieve an Instructor object
		int id = 1;
		session.beginTransaction();
		Instructor i = session.get(Instructor.class, id); 
		if(i!=null) { 
			System.out.println("delete the Instructor object!");
			//this will also delete the InstructorDetail object, because of CascadeType.ALL
			session.delete(i);
		}
		session.getTransaction().commit();
		System.out.println("Done!");

	}

}
