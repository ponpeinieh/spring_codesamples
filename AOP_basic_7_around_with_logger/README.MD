This is Spring demo from Udemy Spring & Hibernate course. 

AOP - Aspect Oriented Programming

- Around advice :  run before and after method
    - @Around
    - Like a combination of @Before and @After , but gives you more fine-grained control
    - Use Cases
        - Most common: logging, auditing, security
        - Pre-processing and post-processing data
        - Instrumentation / profiling code
            - How long does it take for a section of code to run?
        - Managing exceptions
            - Swallow / handle / stop exceptions
    - ProceedingJoinPoint
        - The `proceeding join point` is a handle to the target method
        - Your code can use the proceeding join point to execute target method
            - Object result = theProceedingJoinPoint.proceed();

        ```
        @Around("execution(* com.luv2code.aopdemo.service.*.getFortune(..))")
        public Object afterGetFortune(
            ProceedingJoinPoint theProceedingJoinPoint) throws Throwable {

            // get begin timestamp
            long begin = System.currentTimeMillis();
            // now, let's execute the method
            Object result = theProceedingJoinPoint.proceed();
            // get end timestamp
            long end = System.currentTimeMillis();
            // compute duration and display it
            long duration = end - begin;
            System.out.println("\n=====> Duration: " + duration + " milliseconds");
            return result;
        }
        ```
    - Use Logger to log message
        - private static Logger logger = Logger.getLogger(AroundWithLoggerDemoApp.class.getName());
        - logger.info(...)