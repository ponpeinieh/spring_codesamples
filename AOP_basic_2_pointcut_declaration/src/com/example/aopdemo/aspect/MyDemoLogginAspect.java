package com.example.aopdemo.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class MyDemoLogginAspect {
	// add all related advices for logging

	@Pointcut("execution(* com.example.aopdemo.dao.*.*(..))")
	private void forDaoPackage() {
	}

	@Pointcut("execution(* com.example.aopdemo.dao.*.get*(..))")
	private void getter() {
	}

	// create pointcut for setter methods
	@Pointcut("execution(* com.example.aopdemo.dao.*.set*(..))")
	private void setter() {
	}

	// combine pointcut: include package ... exclude getter/setter
	@Pointcut("forDaoPackage() && !(getter() || setter())")
	private void forDaoPackageNoGetterSetter() {
	}

//	@Before("forDaoPackage()")
//	public void beforeAddAccountAdvice() {
//		System.out.println("\n====>> Executing @Before advice on method!");
//	}

	@Before("forDaoPackageNoGetterSetter()")
	public void beforeAddAccountNoGetterSetterAdvice() {
		System.out.println("\n====>> Executing @Before advice on method, but no getters and setters!");
	}
}
