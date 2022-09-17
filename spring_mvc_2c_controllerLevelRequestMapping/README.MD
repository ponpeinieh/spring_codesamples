This is Spring demo from Udemy Spring & Hibernate course. It includes 
- Spring MVC basic
- Use Controller level request mapping 
	- to avoid duplicate request mapping settings for different methods.
- Note that for the process form action is still working, though the controller level request mapping is added. This is because the action path is not with leading slash, so it is a relative path. 
```
@Controller
@RequestMapping("/hello") //Controller level request mapping 
public class HelloWorldController {

	//method to show the initial form
	@RequestMapping("/showForm")
	public String showForm() {
		return "helloworld-form";
	}
	
	//method to process the form
	@RequestMapping("/processFormVersion3")
	public String processFormVersion3(@RequestParam("studentName") String name, Model model) {
		//upper case 
		name=name.toUpperCase();
		//add to model
		model.addAttribute("message","Yo! "+name);
		return "helloworld";
	}
```

```
@Controller
public class SillyController {
	@RequestMapping("/showForm") //duplicate request mapping 
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

```
<html>
<head>
<title>Hello world - input form</title>
</head>
<body>
 <form action="processFormVersion3" method="GET"> <!-- note that it is still working, since it uses relative path here, no leading slash in the path -->
 <input type="text" name="studentName"
 placeholder="What's your name?"/>
 <input type="submit"/>
 
 </form>
</body></html>
```