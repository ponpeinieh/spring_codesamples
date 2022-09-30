package com.example.aopdemo.aspect;

import java.util.List;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import com.example.aopdemo.entity.Account;

@Aspect
@Component
public class MyDemoLogginAspect {
	// @Before advice
	@Before("execution( * findAccounts(..))")
	public void beforeFindAccountAdvice() {
		System.out.println("\n====>> Executing @Before advice on findAccount()!");
	}

	// @AfterReturning advice
	@AfterReturning(pointcut = "execution( * findAccounts(..))", returning = "result")
	public void afterReturningFindAccountAdvice(JoinPoint joinPoint, List<Account> result) {
		System.out.println("\n====>> Executing @AfterReturning advice on findAccount()!");
		// let's post-process the data ... let's modify it :-)
		System.out.println(result);
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
