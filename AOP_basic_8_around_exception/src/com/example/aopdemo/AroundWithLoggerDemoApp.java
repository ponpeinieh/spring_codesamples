package com.example.aopdemo;

import java.util.logging.Logger;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.example.aopdemo.service.TrafficFortuneService;

public class AroundWithLoggerDemoApp {
	private static Logger logger = Logger.getLogger(AroundWithLoggerDemoApp.class.getName());

	public static void main(String[] args) {
		// read spring config class
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(DemoConfig.class);
		// get the bean from spring container
		TrafficFortuneService theFortuneService = context.getBean("trafficFortuneService", TrafficFortuneService.class);

		logger.info("\nMain Program: AroundDemoApp");

		logger.info("Calling getFortune");

		boolean tripWire = true;
		String data = theFortuneService.getFortune(tripWire);

		logger.info("\nMy fortune is: " + data);

		logger.info("Finished");

		// close the context
		context.close();
	}
}
