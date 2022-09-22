package com.example.hibernate.demo;

import java.util.List;

import org.hibernate.cfg.Configuration;

import com.example.hibernate.demo.entity.Student;

public class QueryStudentDemo {

	public static void main(String[] args) {
		// Create a SessionFactory
		var sessionFactory = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(Student.class)
				.buildSessionFactory();
		// Create a Session
		var session = sessionFactory.getCurrentSession();
		try {
			System.out.println("Query student object ...");
			session.beginTransaction();
			// query students
			List<Student> students = session.createQuery("from Student").getResultList();
			//List<Student> students = session.createQuery("from Student s where s.lastName='Duck'").getResultList();
			//List<Student> students = session.createQuery("from Student s where s.lastName='Duck' or s.firstName='Kevin'").getResultList();
			//List<Student> students = session.createQuery("from Student s where s.email like '%example%'").getResultList();
			session.getTransaction().commit();
			System.out.println("Done!");
			// display students
			students.stream().forEach(System.out::println);
		} finally {
			sessionFactory.close();
		}

	}

}
