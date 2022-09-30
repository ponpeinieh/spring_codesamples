package com.example.aopdemo.aspect;

import java.util.List;
import java.util.logging.Logger;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import com.example.aopdemo.entity.Account;

@Aspect
@Component
public class MyDemoLoggingAspect {

	private static Logger logger = Logger.getLogger(MyDemoLoggingAspect.class.getName());
	
	@Around("execution(* getFortune(..))")
	public Object aroundGetFortune(ProceedingJoinPoint theProceedingJoinPoint) throws Throwable{
		//print the method
		String method= theProceedingJoinPoint.getSignature().toShortString();
		logger.info("\n====>> Executing @Around advice on "+ method);
		long timeStart = System.currentTimeMillis();
		Object result = theProceedingJoinPoint.proceed();
		long timeEnd = System.currentTimeMillis();
		logger.info("\n====>> time spent on "+ method+" : "+(timeEnd-timeStart)/1000.0+" secs");
		return result;
	}
	
	
	
	// @Before advice
	@Before("execution( * findAccounts(..))")
	public void beforeFindAccountAdvice() {
		logger.info("\n====>> Executing @Before advice on findAccount()!");
	}

	// @After advice
	@After("execution( * findAccounts(..))")
	public void afterFindAccountAdvice(JoinPoint joinPoint) {
		logger.info("\n====>> Executing @After advice on findAccounts()!");
	}
	
	// @AfterThrowing advice
	@AfterThrowing(pointcut = "execution( * findAccounts(..))", throwing = "theExc")
	public void afterThrowingFindAccountAdvice(JoinPoint joinPoint, Throwable theExc) {
		logger.info("\n====>> Executing @AfterThrowing advice on findAccounts()!");
		logger.info("The exception thrown:" + theExc);
	}

	// @AfterReturning advice
	@AfterReturning(pointcut = "execution( * findAccounts(..))", returning = "result")
	public void afterReturningFindAccountAdvice(JoinPoint joinPoint, List<Account> result) {
		logger.info("\n====>> Executing @AfterReturning advice on findAccount()!");
		// let's post-process the data ... let's modify it :-)
		logger.info(result.toString());
		// convert the account names to uppercase
		convertAccountNamesToUpperCase(result);
	}

	private void convertAccountNamesToUpperCase(List<Account> result) {

		// loop through accounts

		for (Account tempAccount : result) {

			// get uppercase version of name
			String theUpperName = tempAccount.getName().toUpperCase();

			// update the name on the account
			tempAccount.setName(theUpperName);
		}

	}
}
