package com.example.hibernate.demo;

import org.hibernate.cfg.Configuration;

import com.example.hibernate.demo.entity.Instructor;
import com.example.hibernate.demo.entity.InstructorDetail;

public class CreateDemo {

	public static void main(String[] args) {
		// Create a SessionFactory
		var sessionFactory = new Configuration().configure("hibernate.cfg.xml")
				.addAnnotatedClass(Instructor.class)
				.addAnnotatedClass(InstructorDetail.class)
				.buildSessionFactory();
		// Create a Session
		var session = sessionFactory.getCurrentSession();
		// create a Instructor object

		Instructor i = new Instructor("John", "Smith", "jsmith@example.com");
		InstructorDetail idl= new InstructorDetail("https://....", "Coding");
		i.setInstructorDetail(idl);
		
		System.out.println("Saving the Instructor object!");
		session.beginTransaction();
		//this will also save the InstructorDetail object, because of CascadeType.ALL
		session.save(i);
		session.getTransaction().commit();
		System.out.println("Done!");

	}

}
