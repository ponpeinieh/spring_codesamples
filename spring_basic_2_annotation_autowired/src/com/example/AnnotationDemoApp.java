package com.example;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class AnnotationDemoApp {

	public static void main(String[] args) {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
		var theCoach = context.getBean("baseballCoach", Coach.class);
		// use the object
		System.out.println(theCoach.getDailyWorkout());
		System.out.println(theCoach.getDailyFortune());
		theCoach = context.getBean("trackCoach", Coach.class);
		// use the object
		System.out.println(theCoach.getDailyWorkout());
		System.out.println(theCoach.getDailyFortune());
		var theCoach2 = context.getBean("cricketCoach", CricketCoach.class);
		// use the object
		System.out.println(theCoach2.getDailyWorkout());
		System.out.println(theCoach2.getDailyFortune());
		System.out.println(theCoach2.getEmailAddress());
		System.out.println(theCoach2.getTeam());
		context.close();
	}

}
