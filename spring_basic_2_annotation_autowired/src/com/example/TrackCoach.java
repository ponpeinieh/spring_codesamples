package com.example;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class TrackCoach implements Coach, InitializingBean, DisposableBean {
	@Autowired
	@Qualifier("happyFortuneService")
	private FortuneService fortuneService;


	@Override
	public String getDailyWorkout() {
		return "Run 20 mins";
	}

	@Override
	public String getDailyFortune() {
		return fortuneService.getFortune();
	}

	@Override
	public void destroy() throws Exception {
		System.out.println(">> in destroy()");
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		System.out.println(">> in afterPropertiesSet()");
		
	}
	
	
}
