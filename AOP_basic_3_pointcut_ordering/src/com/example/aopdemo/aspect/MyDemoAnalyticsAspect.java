package com.example.aopdemo.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Order(1)
@Aspect
@Component
public class MyDemoAnalyticsAspect {
	// add all related advices for analytics

	@Before("com.example.aopdemo.aspect.MyAopExpressions.beforeAddAccountAdvice()")
	public void beforeAddAccountAdvice() {
		System.out.println("\n====>> Executing @Before advice on method with analytics!");
	}

}
