package com.example.springdemo.aspect;

import java.util.logging.Logger;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class CRMLoggingAspect {
	// set logger
	private Logger logger = Logger.getLogger(CRMLoggingAspect.class.getName());

	// setup pointcut declaration
	@Pointcut("execution(* com.example.springdemo.controller.*.*(..))")
	public void forControllerPackage() {
	}

	@Pointcut("execution(* com.example.springdemo.service.*.*(..))")
	public void forServicePackage() {
	}

	@Pointcut("execution(* com.example.springdemo.dao.*.*(..))")
	public void forDAOPackage() {
	}

	@Pointcut("forControllerPackage() || forServicePackage() || forDAOPackage()")
	public void forAppFlow() {
	}

	// add @Before advice
	@Before("forAppFlow()")
	public void beforeAdvice(JoinPoint theJoinPoint) {
		// display method
		logger.info("====> in @Before: calling method:" + theJoinPoint.getSignature());
		// display arguments
		Object[] args = theJoinPoint.getArgs();
		for (Object a : args) {
			logger.info("========> argument:" + a);
		}

	}

	// add @AfterReturning advice
	@AfterReturning(pointcut = "forAppFlow()", returning = "result")
	public void AfterReturningAdvice(JoinPoint theJoinPoint, Object result) {
		// display method
		logger.info("====> in @AfterReturning: calling method:" + theJoinPoint.getSignature());
		// display result
		logger.info("=======> result:" + result);
	}
}
