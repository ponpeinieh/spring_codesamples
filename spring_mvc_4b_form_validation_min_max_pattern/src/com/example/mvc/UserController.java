package com.example.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserController {

	@RequestMapping("/showForm")
	public String showForm(Model theModel) {

		User s = new User();
		theModel.addAttribute("command", s);
		return "user-form";
	}

	@RequestMapping("/processForm")
	public String processForm(@ModelAttribute("command") User theUser) {
		System.out.println("FirstName:" + theUser.getFirstName() + ", LastName:" + theUser.getLastName());
		return "user-confirmation";
	}
}
