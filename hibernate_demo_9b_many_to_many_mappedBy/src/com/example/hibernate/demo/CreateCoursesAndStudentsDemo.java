package com.example.hibernate.demo;

import org.hibernate.cfg.Configuration;

import com.example.hibernate.demo.entity.Course;
import com.example.hibernate.demo.entity.Instructor;
import com.example.hibernate.demo.entity.InstructorDetail;
import com.example.hibernate.demo.entity.Review;
import com.example.hibernate.demo.entity.Student;

public class CreateCoursesAndStudentsDemo {

	public static void main(String[] args) {
		// Create a SessionFactory
		var sessionFactory = new Configuration().configure("hibernate.cfg.xml")
				.addAnnotatedClass(Instructor.class)
				.addAnnotatedClass(InstructorDetail.class)
				.addAnnotatedClass(Course.class)
				.addAnnotatedClass(Review.class)
				.addAnnotatedClass(Student.class).buildSessionFactory();
		// Create a Session
		var session = sessionFactory.getCurrentSession();
		// create a Instructor object
		try { 
			session.beginTransaction();
			//create a course
			Course c = new Course("How to fry pork");
			//add some students
			Student s1 = new Student("John","Smith","jsmith@gmail.com");
			Student s2 = new Student("Jane","Smith","jsmith@gmail.com");
//			Student s1 = session.get(Student.class, 1);
//			Student s2 = session.get(Student.class, 2);
			c.addStudent(s1);
			c.addStudent(s2);
			//
			//save the course
			session.save(c);
			//session.save(s1);
			//session.save(s2);
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
