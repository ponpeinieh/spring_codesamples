This is Spring demo from Udemy Spring & Hibernate course. It includes 
- Spring MVC basic
- submit form
- process form
- controller method accept HttpServletRequest and Model as parameters
	- use HttpServletRequest to get the input parameter (eg. from forms)
	- use model to add attributes , so the view can access
		- in view, use ${param.xxx} to access input parameters
		- in view, use ${xxx} directly to access model attributes

```
@RequestMapping("/processFormVersion2")
	public String letsShoutDude(HttpServletRequest request, Model model) {
		//read the request parameter from the form
		String name = request.getParameter("studentName");
		//upper case 
		name=name.toUpperCase();
		//add to model
		model.addAttribute("message","Yo! "+name);
		return "helloworld";
	}
```

```
<html>
<head>
<title>Hello world - input form</title>
</head>
<body> 
Hello world from Spring:
<br/>
Student name: ${param.studentName}

<br/>
The message : ${message}
</body></html>
```