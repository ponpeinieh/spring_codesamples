package com.example;

import java.util.Random;

import org.springframework.stereotype.Component;

@Component
public class MyFortuneService implements FortuneService {
	private static String[] mysayings = { "You got lucky today!", "Not a peaceful day!",
			"Try hard and you will be fine!" };

	@Override
	public String getFortune() {
		Random r = new Random();
		return mysayings[r.nextInt(3)];
	}

}
