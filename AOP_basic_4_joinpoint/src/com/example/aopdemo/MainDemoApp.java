package com.example.aopdemo;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.example.aopdemo.dao.AccountDAO;

public class MainDemoApp {
	public static void main(String[] args) {
		// read spring config class
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(DemoConfig.class);
		// get the bean
		AccountDAO dao = context.getBean("accountDAO", AccountDAO.class);
		// call the business method
		dao.addAccount(1,1.1F);
		// close the context
		context.close();
	}
}
