This is Spring demo from Udemy Spring & Hibernate course. 

AOP - Aspect Oriented Programming

- Around advice :  run before and after method
    - Managing exceptions
        - Swallow / handle / stop exceptions
        - or rethrow the exception
    - The main app may never know exception happened

        ```
        @Around("execution(* com.luv2code.aopdemo.service.*.getFortune(..))")
        public Object afterGetFortune(
            ProceedingJoinPoint theProceedingJoinPoint) throws Throwable {
            Object result = null;
            try {
                // let's execute the method
                result = theProceedingJoinPoint.proceed();
            } catch (Exception exc) {
                // log exception
                System.out.println("@Around advice: We have a problem " + exc);
                // handle and give default fortune ... use this approach with caution
                result = "Nothing exciting here. Move along!";
            }
            return result;
        }
        ```
