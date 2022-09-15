/*
 * YPN note: Since @PostConstruct and @PreDestroy is deprecated since jdk 9 and removed since jdk 11,
 * we need another way to implements post construct and pre destroy methods.
 * Solution: use org.springframework.beans.factory.InitializingBean and DisposableBean interfaces
 * 
 */
package com.example;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import javax.annotation.PostConstruct;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;


@Component
public class MyLoggerConfig2 implements InitializingBean, DisposableBean{
	@Value("FINER")
	private String rootLoggerLevel;
	@Value("FINER")
	private String printedLoggerLevel; 
	 
	public void initLogger() {
		System.out.println("initLogger is called!");
		// parse levels
		Level rootLevel = Level.parse(rootLoggerLevel);
		Level printedLevel = Level.parse(printedLoggerLevel);

		// get logger for app context
		Logger applicationContextLogger = Logger.getLogger(AnnotationConfigApplicationContext.class.getName());

		// get parent logger
		Logger loggerParent = applicationContextLogger.getParent();

		// set root logging level
		loggerParent.setLevel(rootLevel);

		// set up console handler
		ConsoleHandler consoleHandler = new ConsoleHandler();
		consoleHandler.setLevel(printedLevel);
		consoleHandler.setFormatter(new SimpleFormatter());

		// add handler to the logger
		loggerParent.addHandler(consoleHandler);
	}

	@Override
	public void destroy() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		initLogger();
		
	}

}