This is Spring demo from Udemy Spring & Hibernate course. 

AOP - Aspect Oriented Programming

- Pointcut Declarations
	- reuse a pointcut expression and apply to multiple advices
	- Development process:
		1. Create a pointcut declaration using @Pointcut

		```
		@Pointcut("execution(* com.luv2code.aopdemo.dao.*.*(..))")
		private void forDaoPackage() {}
		```

		2. Apply pointcut declaration to advice(s)
			- note the inside parenthesis in @Before("forDaoPackage()") 
		```
		@Aspect
		@Component
		public class MyDemoLoggingAspect {
			@Pointcut("execution(* com.luv2code.aopdemo.dao.*.*(..))")
			private void forDaoPackage() {}
			@Before("forDaoPackage()")
			public void beforeAddAccountAdvice() {
			...
			}
			@Before("forDaoPackage()")
			public void performApiAnalytics() {
			...
			}
		}
		```
- How to apply multiple pointcut expressions to a single advice
	- Combine pointcut expressions using logic operators
		- AND (&&)
		- OR (||)
		- NOT (!)
	- Works like an “if” statement
		- @Before(“expressionOne() && expressionTwo()”)
		- @Before(“expressionOne() || expressionTwo()”)-
		- @Before(“expressionOne() && !expressionTwo()”)
	- examples:
		-  All methods in a package EXCEPT getter/setter methods
	- Development Process
		1. Create a pointcut declarations

			```
			@Pointcut("execution(* com.luv2code.aopdemo.dao.*.*(..))")
			private void forDaoPackage() {}
			// create pointcut for getter methods
			@Pointcut("execution(* com.luv2code.aopdemo.dao.*.get*(..))")
			private void getter() {}
			// create pointcut for setter methods
			@Pointcut("execution(* com.luv2code.aopdemo.dao.*.set*(..))")
			private void setter() {}
			```

		2. Combine pointcut declarations

			```
			// combine pointcut: include package ... exclude getter/setter
			@Pointcut("forDaoPackage() && !(getter() || setter())")
			private void forDaoPackageNoGetterSetter() {}
			```

		3. Apply pointcut declaration to advice(s)
		
			```
			@Before("forDaoPackageNoGetterSetter()")
			public void beforeAddAccountAdvice() {
			...
			}
			```
