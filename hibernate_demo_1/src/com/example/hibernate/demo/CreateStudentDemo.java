package com.example.hibernate.demo;

import org.hibernate.cfg.Configuration;

import com.example.hibernate.demo.entity.Student;

public class CreateStudentDemo {

	public static void main(String[] args) {
		// Create a SessionFactory
		var sessionFactory = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(Student.class)
				.buildSessionFactory();
		// Create a Session
		var session = sessionFactory.getCurrentSession();
		// create a Student object

		Student s = new Student("John", "Smith", "jsmith@example.com");
		System.out.println("Saving the student object!");
		session.beginTransaction();
		session.save(s);
		session.getTransaction().commit();
		System.out.println("Done!");

	}

}
