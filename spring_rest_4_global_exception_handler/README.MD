This is Spring demo from Udemy Spring & Hibernate course.   
- Global Exception Handler
    - @ExceptionHanlder is only used in a specific REST controller, so can't be reused by other controllers
    - @ControllerAdvice for gobal exception handler
        - Promotes reuse
        - Centralizes exception handling
- Spring @ControllerAdvice
    - @ControllerAdvice is similar to an interceptor / filter
    - Pre-process requests to controllers
    - Post-process responses to handle exceptions
    - Perfect for global exception handling
    - Real-time use of AOP
    - Development Process
        1. Create new @ControllerAdvice

        ```
        @ControllerAdvice
        public class StudentRestExceptionHandler {
        ...
        }
        ```

        2. Refactor REST service ... remove exception handling code
        3. Add exception handling code to @ControllerAdvice

        ```
        @ControllerAdvice
        public class StudentRestExceptionHandler {
            @ExceptionHandler
            public ResponseEntity<StudentErrorResponse> handleException(StudentNotFoundException exc) {
                StudentErrorResponse error = new StudentErrorResponse();
                error.setStatus(HttpStatus.NOT_FOUND.value());
                error.setMessage(exc.getMessage());
                error.setTimeStamp(System.currentTimeMillis());
                return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
            }
        }
```




