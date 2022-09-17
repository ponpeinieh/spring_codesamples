This is Spring demo from Udemy Spring & Hibernate course. It includes 
- Spring MVC basic
- we use properties file to fill in select options
	- properties file should be in WEB-INF directory
	- update the header information in the Spring config file , spring-mvc-demo-servlet.xml
		- add <util> Spring tag
	- load the properties file in the spring config file with bean id : countryOptions
		- <util:properties  id="countryOptions" location="classpath:../countries.properties" />
	- inject the properties value into spring controller
```
@Value("#{countryOptions}") 
private Map<String, String> countryOptions;
```

	- Student bean - simpler than previous
```
public class Student {
	private String firstName;
	private String lastName;
	private String country;
	
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
	
	@Value("#{countryOptions}") //field autowiring
	private Map<String, String> countryOptions;

	@RequestMapping("/showForm")
	public String showForm(Model theModel) {
		Student s = new Student();
		theModel.addAttribute("student", s);
		theModel.addAttribute("countryOptions",countryOptions); //add to the model
		return "student-form";
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
 <form:options items="${countryOptions}"/>
 </form:select>
<br/>
 <input type="submit"/>
 </form:form>
 ```