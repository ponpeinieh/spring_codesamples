package com.example.hibernate.demo;

import org.hibernate.cfg.Configuration;

import com.example.hibernate.demo.entity.Student;

public class ReadStudentDemo {

	public static void main(String[] args) {
		// Create a SessionFactory
		var sessionFactory = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(Student.class)
				.buildSessionFactory();
		// Create a Session
		var session = sessionFactory.getCurrentSession();
		// create a Student object
		try {
			System.out.println("Create 3 student objects ...");
			Student s = new Student("Bruce", "Smith", "bsmith@example.com");
			Student s2 = new Student("Steve", "Smith", "ssmith@example.com");
			Student s3 = new Student("Kevin", "Smith", "ksmith@example.com");
			session.beginTransaction();
			session.save(s);
			session.save(s2);
			session.save(s3);
			session.getTransaction().commit();
			System.out.println("Done!");
		} finally {
			sessionFactory.close();
		}

	}

}
