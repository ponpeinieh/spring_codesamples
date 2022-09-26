package com.example.hibernate.demo;

import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import com.example.hibernate.demo.entity.Course;
import com.example.hibernate.demo.entity.Instructor;
import com.example.hibernate.demo.entity.InstructorDetail;

public class FetchJoinDemo {

	public static void main(String[] args) {
		// Create a SessionFactory
		var sessionFactory = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(Instructor.class)
				.addAnnotatedClass(InstructorDetail.class).addAnnotatedClass(Course.class).buildSessionFactory();
		// Create a Session
		var session = sessionFactory.getCurrentSession();
		// create a Instructor object
		try {
			session.beginTransaction();
			// hibernate query with HQL
			int id=7;
			Query<Instructor> query = session.createQuery(
					"select i from Instructor i " + "join fetch i.courses " + "where i.id=:instructorId",
					Instructor.class);
			//set parameter on query
			query.setParameter("instructorId", id);
			Instructor i = query.getSingleResult();
			System.out.println("Instructor:"+i);
			session.getTransaction().commit();
			//close the session
			session.close();
			//get the courses 
			System.out.println("Courses:"+i.getCourses());
			//
			System.out.println("Done!");
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			session.close();
			sessionFactory.close();
		}
	}

}
