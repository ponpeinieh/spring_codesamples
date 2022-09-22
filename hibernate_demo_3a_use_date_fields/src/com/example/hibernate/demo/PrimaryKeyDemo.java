package com.example.hibernate.demo;

import org.hibernate.cfg.Configuration;

import com.example.hibernate.demo.entity.Student;

public class PrimaryKeyDemo {

	public static void main(String[] args) {
		// Create a SessionFactory
		var sessionFactory = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(Student.class)
				.buildSessionFactory();
		// Create a Session
		var session = sessionFactory.getCurrentSession();
		// create a Student object
		try {
			System.out.println("Create student object ...");
			Student s = new Student("Daffy", "Duck", "daffy@example.com");
			session.beginTransaction();
			session.save(s);
			session.getTransaction().commit();
			// retrieve an object
			System.out.println("Student generated id:" + s.getId());
			// get a new session
			session = sessionFactory.getCurrentSession();
			session.beginTransaction();
			Student s2 = session.get(Student.class, s.getId());
			System.out.println("Retrieved Student :" + s2);
			session.getTransaction().commit();

			System.out.println("Done!");
		} finally {
			sessionFactory.close();
		}

	}

}
