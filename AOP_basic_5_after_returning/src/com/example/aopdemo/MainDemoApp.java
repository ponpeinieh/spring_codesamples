package com.example.aopdemo;

import java.util.List;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.example.aopdemo.dao.AccountDAO;
import com.example.aopdemo.entity.Account;

public class MainDemoApp {
	public static void main(String[] args) {
		// read spring config class
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(DemoConfig.class);
		// get the bean
		AccountDAO dao = context.getBean("accountDAO", AccountDAO.class);
		// call method to find the accounts
		List<Account> theAccounts = dao.findAccounts();

		// display the accounts
		System.out.println(theAccounts);

		System.out.println("\n");

		// close the context
		context.close();
	}
}
