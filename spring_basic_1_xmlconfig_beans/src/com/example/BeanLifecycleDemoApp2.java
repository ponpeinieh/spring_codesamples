package com.example;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class BeanLifecycleDemoApp2 {

	public static void main(String[] args) {
		// load spring config file
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("beanLifecycle-applicationContext2.xml");
		// retrieve bean from spring container
		Coach theCoach = context.getBean("myCoach", Coach.class); 
		System.out.println(theCoach.getDailyWorkout());
		context.close();
	}
}
