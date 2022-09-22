package com.example.hibernate.demo;

import java.util.List;

import org.hibernate.cfg.Configuration;

import com.example.hibernate.demo.entity.Student;

public class UpdateStudentDemo {

	public static void main(String[] args) {
		// Create a SessionFactory
		var sessionFactory = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(Student.class)
				.buildSessionFactory();
		// Create a Session
		var session = sessionFactory.getCurrentSession();
		try {
			int studentId = 1;
			session.beginTransaction();
			// update the student
			Student s = session.get(Student.class, studentId);
			System.out.println("Update student object ...");
			s.setEmail("superman@gmail.com");
			session.getTransaction().commit();
			//
			session = sessionFactory.getCurrentSession();
			session.beginTransaction();
			//
			System.out.println("Bulk update student objects ...");
			session.createQuery("update Student set email='foo@gmail.com'").executeUpdate();
			session.getTransaction().commit();
			System.out.println("Done!");
		} finally {
			sessionFactory.close();
		}

	}

}
