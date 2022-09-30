package com.example.aopdemo.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
public class MyAopExpressions {
	// add all related advices for analytics

	@Pointcut("execution(public void addAccount())")
	public void beforeAddAccountAdvice() {}
}
