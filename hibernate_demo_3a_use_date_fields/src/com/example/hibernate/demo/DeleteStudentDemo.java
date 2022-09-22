package com.example.hibernate.demo;

import java.util.List;

import org.hibernate.cfg.Configuration;

import com.example.hibernate.demo.entity.Student;

public class DeleteStudentDemo {

	public static void main(String[] args) {
		// Create a SessionFactory
		var sessionFactory = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(Student.class)
				.buildSessionFactory();
		// Create a Session
		var session = sessionFactory.getCurrentSession();
		try {
			int studentId = 1;
			session.beginTransaction();
			// delete the student
			Student s = session.get(Student.class, studentId);
			System.out.println("Delete student object ...");
			// session.delete(s);
			session.getTransaction().commit();
			//
			session = sessionFactory.getCurrentSession();
			session.beginTransaction();
			//
			System.out.println("Delete students object ...");
			session.createQuery("delete from Student where id=2").executeUpdate();
			session.getTransaction().commit();
			System.out.println("Done!");
		} finally {
			sessionFactory.close();
		}

	}

}
