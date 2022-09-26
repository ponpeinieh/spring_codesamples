package com.example.hibernate.demo;

import org.hibernate.cfg.Configuration;

import com.example.hibernate.demo.entity.Instructor;
import com.example.hibernate.demo.entity.InstructorDetail;

public class GetInstrutorDetailDemo {

	public static void main(String[] args) {
		// Create a SessionFactory
		var sessionFactory = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(Instructor.class)
				.addAnnotatedClass(InstructorDetail.class).buildSessionFactory();
		// Create a Session
		var session = sessionFactory.getCurrentSession();
		try {
			// retrieve an InstructorDetail object
			int id = 8;
			session.beginTransaction();
			InstructorDetail i = session.get(InstructorDetail.class, id);
			System.out.println("instructorDetail:" + i);
			System.out.println("instructor:" + i.getInstructor());
			session.getTransaction().commit();
			System.out.println("Done!");
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			session.close();
			sessionFactory.close();
		}
	}

}
