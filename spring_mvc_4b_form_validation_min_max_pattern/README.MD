This is Spring demo from Udemy Spring & Hibernate course. It includes the validator of using
- @Min
	- @Min(value=0,message="greater or equal to 0")
- @Max 
	- @Max(value=10,message="less or equal to 10")
- @Pattern 
	- @Pattern(regexp="^[A-Z0-9a-z]{5}", message="only 5 alpha numericals")

- Object to validate

```
public class Customer {
	private String firstName;
	@NotNull(message = "is required")
	@Size(min = 1, message = "minimum size is 1")
	private String lastName;
	@Min(value = 0, message = "greater or equal to 0")
	@Max(value = 10, message = "less or equal to 10")
	private int freePasses;
	@Pattern(regexp = "^[A-Z0-9a-z]{5}", message = "only 5 alpha numericals")
	private String postalCode;

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public int getFreePasses() {
		return freePasses;
	}

	public void setFreePasses(int freePasses) {
		this.freePasses = freePasses;
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

- form

```
 
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html>
<head>
<title>Customer - registration form</title>
<style>
.error {
	color: red
}
</style>
</head>
<body>
	<i>Fill out the form please, asterisk(*) is required</i>
	<form:form action="processForm" modelAttribute="customer">
 First name: <form:input path="firstName" />
		<br />
 Last name(*): <form:input path="lastName" />
		<form:errors path="lastName" cssClass="error"></form:errors>
		<br />

 free passes: <form:input path="freePasses" />
		<form:errors path="freePasses" cssClass="error"></form:errors>
		<br />
		postal code: <form:input path="postalCode" />
		<form:errors path="postalCode" cssClass="error"></form:errors>
		<br/>
		<input type="submit" />
	</form:form>
</body>
</html>
```