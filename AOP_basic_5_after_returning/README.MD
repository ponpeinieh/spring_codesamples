This is Spring demo from Udemy Spring & Hibernate course. 

AOP - Aspect Oriented Programming

- After returning advice
    - @AfterReturning
    - This advice will run after the method call (success execution)
    - use cases:
        - logging, security, transaction
        - audit logging
        - Post-processing data
            - **Post process the result before returning to caller**
            - Format the data or enrich the data(cool but careful)
            
    - Access the return value
        - The `returning` attribute value - `result` should match the advice method parameter name `result`   

        ```
        @AfterReturning(
            pointcut="execution(* com.luv2code.aopdemo.dao.AccountDAO.findAccounts(..))", 
            returning="result")
        public void afterReturningFindAccountsAdvice(JoinPoint theJoinPoint, List<Account> result) {
            // print out the results of the method call
            System.out.println("\n=====>>> result is: " + result);
        }
        ```

- actual code
    - CRMLoggingAspect

    ```
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
        ```