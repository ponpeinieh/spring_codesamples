This is Spring demo from Udemy Spring & Hibernate course. It includes 
- Spring MVC basic
- How to use spring mvc form tags in **jsp pages** 
	- support data binding
	- includes:
		- form:form for the form container
		- form:input for text field
		- form:textarea
		- form:checkbox
		- form:radiobutton
		- form:select for drop down list
		- more
	- The form tag library comes bundled in `spring-webmvc.jar`. 
	- The library descriptor is called `spring-form.tld`.
	- To use the tags from this library, add the following directive to the top of your **JSP page**:
		- <%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
		- where `form` is the tag name prefix you want to use for the tags from this library.
	- To use an object in the form, we need to add it to the model as an attribute, eg
		- theModel.addAttribute("student", new Student());
	- The variable name of the form backing object is default to `command` or use `modelAttribute` to specify a different name, eg.
		- <form:form action="processForm" modelAttribute="student">
	- Handling the form in the controller:
		- use @ModelAttribute annotation to get the form object, eg
		- public String processForm(@ModelAttribute("student") Student theStudent) {...}
		- the model attribute can be used in the result jsp page.
	- form:select for drop down list:
		- use form:option for each option:
```
Country: <form:select path="country">
 <form:option value="China" label="China"/>
 <form:option value="Malaysia" label="Malaysia"/>
 <form:option value="Indonisia" label="Indonisia"/>
 </form:select>
```
		- or use form:options for filling options from a collection object
			- you can use a Map for select options, then the map key will be the option value and map value will be the display option label.
			- you can also use a List for select options, then the option label and option value will be set to the list item value.
```
Country: <form:select path="country">
 <form:options items="${student.countryOptions}"/>
 </form:select>
```
	- Student bean
```
public class Student {
	private String firstName;
	private String lastName;
	private String country;
	private Map<String,String> countryOptions;
	public Student() {
		countryOptions = new HashMap<String,String>();
		countryOptions.put("BR", "Brazil");
		countryOptions.put("FR", "France");
		countryOptions.put("DE", "Germany");
		countryOptions.put("IN", "India");
	}
	public Map<String, String> getCountryOptions() {
		return countryOptions;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
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

	- Controller
```
@Controller
@RequestMapping("/student")
public class StudentController {

	@RequestMapping("/showForm")
	public String showForm(Model theModel) {

		Student s = new Student();
		theModel.addAttribute("student", s);
		return "student-form";
	}

	@RequestMapping("/processForm")
	public String processForm(@ModelAttribute("student") Student theStudent) {
		System.out.println("FirstName:" + theStudent.getFirstName() + ", LastName:" + theStudent.getLastName());
		return "student-confirmation";
	}
}

```
	- spring mvc form
```
<form:form action="processForm" modelAttribute="student">
 First name: <form:input path="firstName"/>
 <br/>
 Last name: <form:input path="lastName"/>
 <br/>
 Country: 
 <form:select path="country">
 <form:options items="${student.countryOptions}"/>
 </form:select>
<br/>
 <input type="submit"/>
 </form:form>
 ```
	- result jsp page
 ```
<html>
<head>
<title>Student - confirmation</title>
</head>
<body>
  
 First name: ${student.firstName}
 <br/>
 Last name: ${student.lastName}
 <br/>
Country: ${student.country}
</body>
</html>
```