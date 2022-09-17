package com.example.mvc;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SillyController {
	//method to show the initial form
	@RequestMapping("/showForm")
	public String showForm() {
		return "silly";
	}
}
