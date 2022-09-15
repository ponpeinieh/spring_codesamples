package com.example;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MySpringApp {

	public static void main(String[] args) {
		// create the object
		// Coach theCoach = new TrackCoach();

		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
		var theCoach = context.getBean("myCoach", Coach.class);
		// use the object
		System.out.println(theCoach.getDailyWorkout());
		System.out.println(theCoach.getDailyFortune());
		//
		theCoach = context.getBean("myCoach2", Coach.class);
		// use the object
		System.out.println(theCoach.getDailyWorkout());
		System.out.println(theCoach.getDailyFortune());
		//
		var theCoach2 = context.getBean("myCoach3", CricketCoach.class);
		// use the object
		System.out.println(theCoach2.getDailyWorkout());
		System.out.println(theCoach2.getDailyFortune());
		System.out.println(theCoach2.getEmailAddress());
		System.out.println(theCoach2.getTeam());
		context.close();
	}

}
