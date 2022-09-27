This is Spring demo from Udemy Spring & Hibernate course. It includes 
- spring form validation using Java standard Bean Validation API(JSR-303), features include:
	- required
	- length
	- numbers
	- regular expressions
	- custom validation
- Validation annotations
	- @NotNull
	- @Min
	- @Max
	- @Size
	- @Pattern
	- @Future/@Past - used on date
	- others
- Java standard Bean Validation API(JSR-303) is a spec, not an implementation.
	- Hibernate Team provide an implementation
	- **www.hibernate.org/validator**
	- Hibernate Validator v.7 is supporting Jakarta EE 9, which uses jarkata package name, instead of javax name
	- But **Spring 5 still used javax package name**, so is not compatible with Hibernate Validator 7. 
	- So **Hibernate Validator v.6.2 is used** - which has the same features with version 7.
-  Hibernate Validator v.6.2 jar files include : the first 3 in the dist folder, the last 4 in the lib/required folder
	- hibernate-validator-6.2.4.Final
	- hibernate-validator-annotation-processor-6.2.4.Final
	- hibernate-validator-cdi-6.2.4.Final
	- classmate-1.5.1
	- jakarta.el-3.0.3
	- jakarta.validation-api-2.0.2
	- jboss-logging-3.4.1.Final
- required field - Development Process
	1. Add validation rule to Customer class
		- @NotNull(message="is required"), actually if the text input is not entered, it will be an empty string, instead of a null string object. Unless a `StringTrimmerEditor` is used to trim spaces and results in a null string object
		- @Size(min=1, message="minimum size is 1"), if no trimming of spaces is applied, use @Size with minimum value of 1 to deal with empty text field input. 
	2. Display error messages on HTML form 
		- use <form:errors> 
	3. Perform validation in the Controller class
	4. Update confirmation page

- Object to validate
```
public class Customer {
	private String firstName;
	@NotNull(message="is required")
	@Size(min=1, message="minimum size is 1")
	private String lastName;
	
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
- <form:errors> to display error message if validation failed. 
	- `path`: referene to the field
	- `cssClass`: can be used to style the message

```
 Last name(*): <form:input path="lastName" />
	<form:errors path="lastName" cssClass="error"></form:errors> 
```

- controller
	- use `@Valid` on the model attribute
	- `BindingResult` contains validation result
	- if `bindingResult.hasErrors()` is true, return to the form page, otherwise proceed to the result page.
	- Note that @RequestMapping handler methods have a flexible signature and can choose from a range of supported controller method arguments and return values. But the `BindingResult` parameter must appear immediately after the model attribute. 
	- Errors, BindingResult (from spring doc) : For access to errors from validation and data binding for a command object (that is, a @ModelAttribute argument) or errors from the validation of a @RequestBody or @RequestPart arguments. You must declare an Errors, or BindingResult argument immediately after the validated method argument.

```
@RequestMapping("/processForm")
	public String processForm(@Valid @ModelAttribute("customer") Customer theCustomer,
			BindingResult theBindingResult) {
		System.out.println("lastname:|"+theCustomer.getLastName()+"|");
		if(theBindingResult.hasErrors()) {
			return "customer-form";
		}
		System.out.println("FirstName:" + theCustomer.getFirstName() + ", LastName:" + theCustomer.getLastName());
		return "customer-confirmation";
	}
```

- @InitBinder
	- works as a preprocessor
	- preprocess each web request to the controller
	- The @InitBinder annotated method is executed before the handler methods in the controller
	- Here a preprocess of string input is applied for trimming the leading and trailing white spaces
		- StringTrimmerEditor is used to trim strings.
		- if string only has white spaces, make it a null string object
		- **The editor is only applied to 'String' type fields**

```
	@InitBinder
	public void initBinder(WebDataBinder dataBinder) {
		StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
		dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
	}
```