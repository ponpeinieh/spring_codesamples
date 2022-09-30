This is Spring demo from Udemy Spring & Hibernate course. 

AOP - Aspect Oriented Programming

- How to control the order of advices being applied?
	- Refactor: place advices in separate aspects
	- Using @Order to control the order on aspects

- Development process:
	- Refactor: place advices in separate Aspects
	- Using @Order on Aspects

- code
	- MyAopExpressions : declarea a pointcut for other to share

		```
		package com.example.aopdemo.aspect;

		import org.aspectj.lang.annotation.Aspect;
		import org.aspectj.lang.annotation.Before;
		import org.aspectj.lang.annotation.Pointcut;
		import org.springframework.core.annotation.Order;
		import org.springframework.stereotype.Component;

		@Aspect
		public class MyAopExpressions { 

			@Pointcut("execution(public void addAccount())")
			public void beforeAddAccountAdvice() {}
		}

		```

	- MyDemoLogginAspect
		- with @Order(2)
		- @Before("com.example.aopdemo.aspect.MyAopExpressions.beforeAddAccountAdvice()") to use the previous pointcut declaration in MyAopExpressions

		```
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

			@Before("com.example.aopdemo.aspect.MyAopExpressions.beforeAddAccountAdvice()")
			public void beforeAddAccountAdvice() {
				System.out.println("\n====>> Executing @Before advice on method!");
			}

		}
		```

	- MyDemoAnalyticsAspect
		- with @Order(1)
		- @Before("com.example.aopdemo.aspect.MyAopExpressions.beforeAddAccountAdvice()") to use the previous pointcut declaration in MyAopExpressions

		```
		package com.example.aopdemo.aspect;

		import org.aspectj.lang.annotation.Aspect;
		import org.aspectj.lang.annotation.Before;
		import org.springframework.core.annotation.Order;
		import org.springframework.stereotype.Component;

		@Order(1)
		@Aspect
		@Component
		public class MyDemoAnalyticsAspect {

			@Before("com.example.aopdemo.aspect.MyAopExpressions.beforeAddAccountAdvice()")
			public void beforeAddAccountAdvice() {
				System.out.println("\n====>> Executing @Before advice on method with analytics!");
			}

		}
		```
	- MainDemoApp

		```
		package com.example.aopdemo;

		import org.springframework.context.annotation.AnnotationConfigApplicationContext;

		import com.example.aopdemo.dao.AccountDAO;

		public class MainDemoApp {
			public static void main(String[] args) {
				// read spring config class
				AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(DemoConfig.class);
				// get the bean
				AccountDAO dao = context.getBean("accountDAO", AccountDAO.class);
				// call the business method
				dao.addAccount();
				// close the context
				context.close();
			}
		}
		```
				