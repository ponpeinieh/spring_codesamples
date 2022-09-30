This is Spring demo from Udemy Spring & Hibernate course. 

AOP - Aspect Oriented Programming

- Reading method arguments with JoinPoints
	- Development Process
		1. Access and display Method Signature

		```
		@Before("...")
		public void beforeAddAccountAdvice(JoinPoint theJoinPoint) {
			// display the method signature
			MethodSignature methodSig = (MethodSignature) theJoinPoint.getSignature();
			System.out.println("Method: " + methodSig);
		}
		```

		2. Access and display Method Arguments	
		```
		@Before("...") 
		public void beforeAddAccountAdvice(JoinPoint theJoinPoint) {
			// display method arguments
			// get args
			Object[] args = theJoinPoint.getArgs();
			// loop thru args
			for (Object tempArg : args) {
				System.out.println(tempArg);
			}
		}
		``` 