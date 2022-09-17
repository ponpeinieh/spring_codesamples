This is Spring demo from Udemy Spring & Hibernate course. It includes 
- Spring MVC basic
- Use Controller level request mapping 
	- to avoid duplicate request mapping settings for different methods.
```
@Controller
@RequestMapping("/hello") //Controller level request mapping 
public class HelloWorldController {
	//method to show the initial form
	@RequestMapping("/showForm")
	public String showForm() {
		return "helloworld-form";
	}
```

```
@Controller
public class SillyController {
	@RequestMapping("/showForm")
	public String showForm() {
		return "silly";
	}
}
```

```
<html>
<head>
</head>
<body>
<h2>My page header - Spring MVC Demo</h2>
<hr/>
<a href="hello/showForm">Hello world form</a>
</body></html>
```