package com.example.hibernate.demo;

import org.hibernate.cfg.Configuration;

import com.example.hibernate.demo.entity.Course;
import com.example.hibernate.demo.entity.Instructor;
import com.example.hibernate.demo.entity.InstructorDetail;

public class CreateCoursesDemo {

	public static void main(String[] args) {
		// Create a SessionFactory
		var sessionFactory = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(Instructor.class)
				.addAnnotatedClass(InstructorDetail.class).addAnnotatedClass(Course.class).buildSessionFactory();
		// Create a Session
		var session = sessionFactory.getCurrentSession();
		// create a Instructor object
		try { 
			session.beginTransaction();
			// get the instructor from db
			int id=1;
			Instructor i = session.get(Instructor.class, id);
			//create some courses
			Course c1 = new Course("Yoga class");
			Course c2 = new Course("Photograph class");
			//add courses to instructor
			i.add(c1);
			i.add(c2);
			//save courses
			session.save(c1);
			session.save(c2);
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
