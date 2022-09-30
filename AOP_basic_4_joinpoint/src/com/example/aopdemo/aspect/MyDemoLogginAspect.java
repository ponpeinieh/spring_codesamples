package com.example.aopdemo.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class MyDemoLogginAspect {
	// @Before advice
	@Before("execution(public void addAccount(..))")
	public void beforeAddAccountAdvice(JoinPoint joinpoint) {
		System.out.println("\n====>> Executing @Before advice on addAccount()!");
		System.out.println("\n====>> printing method signature!");
		Signature sig = joinpoint.getSignature();
		System.out.println("\n"+sig);
		System.out.println("\n====>> printing method arguments!");
		Object[] args = joinpoint.getArgs();
		for(var v : args) {
			System.out.println("\n"+v);
		}
		
	}
}
