package com.example.mvc;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/hello")
public class HelloWorldController {
	//method to show the initial form
	@RequestMapping("/showForm")
	public String showForm() {
		return "helloworld-form";
	}
	//method to process the form
	@RequestMapping("/processForm")
	public String processForm() {
		return "helloworld";
	}
	//method to read the form data and add data to the model
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
	
	@RequestMapping("/processFormVersion3")
	public String processFormVersion3(@RequestParam("studentName") String name, Model model) {
		//upper case 
		name=name.toUpperCase();
		//add to model
		model.addAttribute("message","Yo! "+name);
		return "helloworld";
	}
}
