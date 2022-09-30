package com.example.aopdemo.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
@Order(2)
@Aspect
@Component
public class MyDemoLogginAspect {
	// add all related advices for logging

	@Before("com.example.aopdemo.aspect.MyAopExpressions.beforeAddAccountAdvice()")
	public void beforeAddAccountAdvice() {
		System.out.println("\n====>> Executing @Before advice on method!");
	}

}
