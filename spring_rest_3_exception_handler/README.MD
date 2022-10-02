This is Spring demo from Udemy Spring & Hibernate course.   
- Spring REST
    - Development Process
        1. Create Java POJO class for Student

        ```
        public class Student {
            private String firstName;
            private String lastName;
            public Student() {
            }
            public Student(String firstName, String lastName) {
                this.firstName = firstName;
                this.lastName = lastName;
            }
            public String getFirstName() {
                return firstName;
            }
            public void setFirstName(String firstName) {
                this.firstName = firstName;
            }
            public String getLastName() {
                return lastName;
            }
            public void setLastName(String lastName) {
                this.lastName = lastName;
            }
        }
        ```

        2. Create Spring REST Service using @RestController 
        ```
        @RestController
        @RequestMapping("/api")
        public class StudentRestController {
            // define endpoint for "/students" - return list of students
            @GetMapping("/students")
            public List<Student> getStudents() {
                List<Student> theStudents = new ArrayList<>();
                theStudents.add(new Student("Poornima", "Patel"));
                theStudents.add(new Student("Mario", "Rossi"));
                theStudents.add(new Student("Mary", "Smith"));
                return theStudents;
            }
        }
        ```

- Refactor the controller code
    - Implements InitialingBean
        - Override afterPropertiesSet() method to initialize Student List data
        - This is the new way of doing post construct work for a bean (ie. @PostConstruct is deprecated)

        ```
        @RestController
        @RequestMapping("/api")
        public class StudentRestController implements InitializingBean {
            private List<Student> theStudents;

            public void loadData() {
                theStudents = new ArrayList<>();
                theStudents.add(new Student("Poornima", "Patel"));
                theStudents.add(new Student("Mario", "Rossi"));
                theStudents.add(new Student("Mary", "Smith"));
            }

            @GetMapping("/students")
            public List<Student> getStudents() {
                return theStudents;
            }

            @Override
            public void afterPropertiesSet() {
                loadData();
            }
        }
        ```

- Path Variables:
    - Retrieve a single Student by id
        - GET　/api/students/{studentId}
        - @PathVariable

        ```
        @RestController
        @RequestMapping("/api")
        public class StudentRestController {
            // define endpoint for "/students/{studentId}" - return student at index (just for simplicity)
            @GetMapping("/students/{studentId}")
            public Student getStudent(@PathVariable int studentId){
                List<Student> theStudents = new ArrayList<>();
                // populate theStudents
                ...
                return theStudents.get(studentId);
            }
        }
        ```

- Exception Handling
    - For `IndexOutOfRangeException` when calling List.get(index) 
    - Development Process
        1. Create a custom **error response class**
            - The custom error response class will be sent back to client as JSON
            - We will define as Java class (POJO)
            - Jackson will handle converting it to JSON

            ```
            public class StudentErrorResponse {
                private int status;
                private String message;
                private long timeStamp;
                // constructors
                // getters / setters
            }
            ```

        2. Create a custom exception class

        ```
        public class StudentNotFoundException extends RuntimeException {
            public StudentNotFoundException(String message) {
                super(message);
            }
        }
        ```

        3. Update REST service to throw exception if student not found

            ```
            @RestController
            @RequestMapping("/api")
            public class StudentRestController {
                @GetMapping("/students/{studentId}")
                public Student getStudent(@PathVariable int studentId) {
                    // check the studentId against list size
                    if ( (studentId >= theStudents.size()) || (studentId < 0) ) {
                        throw new StudentNotFoundException("Student id not found - " + studentId);
                    }
                    return theStudents.get(studentId);
                }
            }
                ...
            ```

        4. Add an exception handler method using `@ExceptionHandler`
            - Note that this handler method is only used by this controller( see global handler for sharing between controllers).
            - Exception handler will return a `ResponseEntity`
            - `ResponseEntity` is a wrapper for the HTTP response object (ie. StudentErrorResponse)
            - `ResponseEntity` provides fine-grained control to specify:
                - HTTP status code, HTTP headers and Response body
            
            ```
            @RestController
            @RequestMapping("/api")
            public class StudentRestController {
            ...
                @ExceptionHandler
                public ResponseEntity<StudentErrorResponse> handleException(StudentNotFoundException exc) {
                    StudentErrorResponse error = new StudentErrorResponse();
                    error.setStatus(HttpStatus.NOT_FOUND.value()); //404
                    error.setMessage(exc.getMessage());
                    error.setTimeStamp(System.currentTimeMillis());
                    return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
                }
            }
            ```
    - For `MethodArgumentTypeMismatchException` when the PathVariable is something other than an integer
        - Need a generic exception handler

        ```
        警告: Resolved [org.springframework.web.method.annotation.MethodArgumentTypeMismatchException: Failed to convert value of type 'java.lang.String' to required type 'int'; nested exception is java.lang.NumberFormatException: For input string: "20yyu"]

        ```
        // Add a generic exception handler for Exception
        @ExceptionHandler
        public ResponseEntity<StudentErrorResponse> handleException(Exception exc) {
            StudentErrorResponse error = new StudentErrorResponse();
            error.setStatus(HttpStatus.BAD_REQUEST.value()); // 400
            error.setMessage(exc.getMessage());
            error.setTimeStamp(System.currentTimeMillis());
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
        ```

        ``
        {
        "status": 400,
        "message": "Failed to convert value of type 'java.lang.String' to required type 'int'; nested exception is java.lang.NumberFormatException: For input string: \"20yyu\"",
        "timeStamp": 1664682270381
        }
        ```
