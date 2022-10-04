package com.example.springboot.demo.mycoolapp.test;

import java.time.LocalDateTime;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FunRestController {
	@GetMapping("/")
	public String sayHello() {
		return "Hello World! The local time is " + LocalDateTime.now();
	}
}
