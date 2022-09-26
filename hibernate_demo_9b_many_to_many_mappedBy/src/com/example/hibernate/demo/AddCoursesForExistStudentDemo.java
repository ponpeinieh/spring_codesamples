package com.example.hibernate.demo;

import org.hibernate.cfg.Configuration;

import com.example.hibernate.demo.entity.Course;
import com.example.hibernate.demo.entity.Instructor;
import com.example.hibernate.demo.entity.InstructorDetail;
import com.example.hibernate.demo.entity.Review;
import com.example.hibernate.demo.entity.Student;

public class AddCoursesForExistStudentDemo {

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
			// get a Student
			Student s1 = session.get(Student.class, 5);
			System.out.println("Student:"+s1);
			System.out.println("Courses:"+s1.getCourses());
			//create more courses
			Course c1 = new Course("Rubik's Cube5");
			Course c2 = new Course("Game development5");
//			c1.addStudent(s1);
//			c2.addStudent(s1);
			session.save(c1);
			session.save(c2);
			s1.add(c1);
			s1.add(c2);
			
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
