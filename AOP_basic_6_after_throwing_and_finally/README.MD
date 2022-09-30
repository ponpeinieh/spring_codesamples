This is Spring demo from Udemy Spring & Hibernate course. 

AOP - Aspect Oriented Programming

- After throwing advice
    - @AfterThrowing advice has a change to look at the exception
    - **But the exception still propagates back to the calling method**
        -  If you want to stop the exception propagation, then use the `@Around` advice
    - Use cases:
        - Log exceptions
        - Audit on the exceptions
        - Notify DevOps team via email
        - Encapsuate this functionality in AOP for easy reuse

        ```
        @AfterThrowing("execution(* com.luv2code.aopdemo.dao.AccountDAO.findAccounts(..))")
        public void afterThrowingFindAccountsAdvice() {
            System.out.println("Executing @AfterThrowing advice");
        }
        ```

    - Access the Exception:
        - The `throwing` attribute value - `theExc` should match the advice method parameter name `theExc`   

        ```
        @AfterThrowing(
            pointcut="execution(* com.luv2code.aopdemo.dao.AccountDAO.findAccounts(..))",
            throwing="theExc")
        public void afterThrowingFindAccountsAdvice(JoinPoint theJoinPoint, Throwable theExc) {
            // log the exception
            System.out.println("\n=====>>> The exception is: " + theExc);
        }
        ```
- After finally advice - @After 
    - Runs after a method is completed, regardless of outcout/exception
    - @After will execute before @AfterThrowing
    - **Changes in the Spring 5.2.7** (released on 9 June 2020). Starting with Spring 5.2.7:
        - if advice methods defined in the same @Aspect class that need to run at the same join point
        - the @After advice method is invoked **AFTER** any @AfterReturning or @AfterThrowing advice methods in the same aspect class 
