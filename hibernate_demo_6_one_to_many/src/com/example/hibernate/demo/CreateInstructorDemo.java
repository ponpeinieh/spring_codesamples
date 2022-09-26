package com.example.hibernate.demo;

import org.hibernate.cfg.Configuration;

import com.example.hibernate.demo.entity.Course;
import com.example.hibernate.demo.entity.Instructor;
import com.example.hibernate.demo.entity.InstructorDetail;

public class CreateInstructorDemo {

	public static void main(String[] args) {
		// Create a SessionFactory
		var sessionFactory = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(Instructor.class)
				.addAnnotatedClass(InstructorDetail.class).addAnnotatedClass(Course.class).buildSessionFactory();
		// Create a Session
		var session = sessionFactory.getCurrentSession();
		// create a Instructor object
		try {
			Instructor i = new Instructor("Jane", "Smith", "jsmith@example.com");
			InstructorDetail idl = new InstructorDetail("https://....", "Coding");
			i.setInstructorDetail(idl);
			//add courses
			Course c1 = new Course("Yoga class");
			Course c2 = new Course("Photograph class");
			//add courses to instructor
			i.add(c1);
			i.add(c2);
			System.out.println("Saving the Instructor object!");
			session.beginTransaction();
			// this will also save the InstructorDetail object, because of CascadeType.ALL
			session.save(i);
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
