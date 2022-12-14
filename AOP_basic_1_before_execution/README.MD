This is Spring demo from Udemy Spring & Hibernate course. 

AOP - Aspect Oriented Programming

- Problems to solve:
	- Code Tangling
		- Logging, security code tangled in methods
	- Code Scattering
		- Update all classes to change logging , security code
- AOP concept:
	- Aspect encapsulates cross-cutting logic
	- Aspect can be reused
		- Same aspect/class applied based on configuration
	- Apply the **Proxy design pattern**
		- Code for Aspect is defined in a single class
		- Business code in your application is cleaner
		- Congifurable: apply aspects selectively to different parts of app
		- No need to modify main application code
	- Most common usage cases:
		- Logging, security, transaction
		- Audit loggin
			- who/what/when/where
		- Exception handling
			- log exceptions and notify DevOps via email
		- API management
			- how many times has a method been called by users
			- analytics: what are peak times? average load? Top user?
	- Disadvantages:
		- Too many aspects
		- performance cost
	- AOP terms:
		- Aspect
		- Advice : What action to taken, when it should be applied
		- Join Point : When to apply code during program execution
		- Pointcut: A predicate expression for where advice should be applied
	- Advice types:
		- Before advice: run before the method
		- After finally advice: run after the method (finally)
		- After returning advice: run after the method(success execution)
		- After throwing advice: run after the method if exception thrown
		- Around advice: run before and after a method
	- Weaving: connecting aspects to target objects to create an advised object
		- Different types of weaving
			- compile-time,
			- load-time
			- run-time - the slowest kind
	- Two leading AOP Frameworks for Java 
		- Spring AOP
			- Key component of Spring
			- Security, transactions, caching etc
			- Use **run-time weaving** of aspects
			- Simpler than AspectJ
			- Disadvantages:
				- Only supports method-level join points
				- Can only apply aspects to beans created by Spring app context
				- Minor performance cost for run-time weaving
		- AspectJ
			- Provide full stack functionalities

- `@Before` Advice
	- Most common - Logging, security, transaction
		- `@Transactional` uses AOP behind the scenes
	- require AspectJ jar files
		- Spring AOP uses some of AspectJ annotations
		- Spring AOP uses some of AspectJ classes
	- Development Process 
		1. Create target object: AccountDAO

		```
		@Component
		public class AccountDAO {
			public void addAccount() {
				System.out.println("DOING MY DB WORK: ADDING AN ACCOUNT");
			}
		}
		```

		2. Create Spring Java Config class
			- @EnableAspectJAutoProxy

			```
			@Configuration
			@EnableAspectJAutoProxy
			@ComponentScan("com.luv2code.aopdemo")
			public class DemoConfig {
			}
			```

		3. Create main app

		```
		public class MainDemoApp {
			public static void main(String[] args) {
				// read spring config java class
				AnnotationConfigApplicationContext context =
						new AnnotationConfigApplicationContext(DemoConfig.class);
				// get the bean from spring container
				AccountDAO theDAO = context.getBean("accountDAO", AccountDAO.class);
				// call the business method
				theDAO.addAccount();
				// close the context
				context.close();
			}
		}
		```

		4. Create an Aspect with @Before advice
			- Pointcut expression:  `execution(public void addAccount())`
			- The annotated method can have any name - here is called beforeAddAccountAdvice()

			```
			@Aspect
			@Component
			public class MyDemoLoggingAspect {
				@Before("execution(public void addAccount())")
				public void beforeAddAccountAdvice() {
					System.out.println("Executing @Before advice on addAccount()");
				}
			}
			```

	- Best Practices: Aspect and Advices
		- Keep the code small
		- Keep the code fast
		- Do not perform any expensive / slow operations
		- Get in and out as QUICKLY as possible

- Eclipse project settings:
	- download AspectJ Weaver 1.9.1 - https://mvnrepository.com/artifact/org.aspectj/aspectjweaver

-  Pointcut expression Language
	- Spring AOP uses AspectJ's pointcut expression language
	- `execution` pointcuts:
		- Applied to execution of methods
		- execution(modifiers-pattern? return-type-pattern declaring-type-pattern?
			method-name-pattern(param-pattern) throws-pattern?)
			- The pattern is optional if it has ???????
		- examples: 
			- @Before("execution(public void addAccount())")
			- @Before("execution(public void add*())???)
			- @Before(???execution(public * processCreditCard*())???)
			- @Before(???execution(* processCreditCard*())???)
		- For param-pattern
			-  () - matches a method with no arguments
			-  (*) - matches a method with one argument of any type
			-  (..) - matches a method with 0 or more arguments of any type
			- examples:
				- @Before("execution(* addAccount())")
				- @Before("execution(* addAccount(com.luv2code.aopdemo.Account))")
				- @Before("execution(* addAccount(..))???)
				- @Before("execution(* com.luv2code.aopdemo.dao.*.*(..))")
