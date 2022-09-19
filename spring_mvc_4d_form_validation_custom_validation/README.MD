This is Spring demo from Udemy Spring & Hibernate course. It includes form validation
-  How to write a custom validation rule
    - Write a custom java annotation, eg @CourseCode, so we can apply it to the field, such as

```
@CourseCode(value="LUV", message="must start with LUV")
private String courseCode;
```
    - Development Process
        1. Create custom validation rule
            - Create @CourseCode annotation
            - Create CourseCodeConstraintValidator
        2. Add validation rule to Customer class
        3. Display error messages on HTML form
        4. Update confirmation page

- CourseCode annotation 

```
@Constraint(validatedBy=CourseCodeConstraintValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CourseCode {
	//default course code
	public String value() default "LUV";

	//default error message
	public String message() default "must start with LUV";

	//default groups
	public Class<?>[] groups() default {};

	//default payloads
	public Class<? extends Payload>[] payload() default {};
}
```

- CourseCodeConstraintValidator

```
public class CourseCodeConstraintValidator implements ConstraintValidator<CourseCode, String> {
	private String coursePrefix;

	@Override
	public void initialize(CourseCode theCourseCode) {
		coursePrefix = theCourseCode.value();
	}

	@Override
	public boolean isValid(String theCode, ConstraintValidatorContext theConstraintValidatorContext) {
		boolean result = true;
		if(theCode!=null) //make sure check is not null
			result = theCode.startsWith(coursePrefix);	
		return result;
	}

}

```

- Customer class

```
	@CourseCode(value="ABC", message="The code need to prefix with ABC")
	private String courseCode;
```