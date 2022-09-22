package com.example.hibernate.demo;

import org.hibernate.cfg.Configuration;

import com.example.hibernate.demo.entity.Instructor;
import com.example.hibernate.demo.entity.InstructorDetail;

public class DeleteInstrutorDetailDEmo {

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
			InstructorDetail idl = session.get(InstructorDetail.class, id);
			Instructor i = idl.getInstructor();
			System.out.println("delete instructorDetail");
			i.setInstructorDetail(null);
			//session.delete(idl);
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
