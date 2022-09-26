package com.example.hibernate.demo;

import java.util.List;

import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import com.example.hibernate.demo.entity.Course;
import com.example.hibernate.demo.entity.Instructor;
import com.example.hibernate.demo.entity.InstructorDetail;

public class LazyFetchDemo {

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
			int id = 7;
			Instructor i = session.get(Instructor.class, id);
			System.out.println("Instructor:" + i);
			session.getTransaction().commit();
			session.close();
			//
			session = sessionFactory.getCurrentSession();
			session.beginTransaction();
			Query<Course> query = session.createQuery("select a from Course a where a.instructor.id=:id", Course.class);
			query.setParameter("id", id);
			List<Course> courses = query.getResultList();
			session.getTransaction().commit();
			session.close();
			i.setCourses(courses);
			System.out.println("Courses:" + i.getCourses());
			System.out.println("Done!");
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			session.close();
			sessionFactory.close();
		}
	}

}
