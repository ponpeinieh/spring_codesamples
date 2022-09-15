package com.example;

import org.springframework.beans.factory.DisposableBean;

public class BaseballCoach2 implements Coach, DisposableBean{
	private FortuneService fortuneService;
	@Override
	public String getDailyWorkout() {
		//  
		return "Workout 30 mins";
	}
 

	public void setFortuneService(FortuneService fortuneService) {
		this.fortuneService = fortuneService;
	}


	public BaseballCoach2() {
	}

	public BaseballCoach2(FortuneService theService) {
		this.fortuneService=theService;
	}

	@Override
	public String getDailyFortune() { 
		return fortuneService.getFortune();
	}
	
	//init method
	public void doInitWork() {
		System.out.println("do initial work ");
	}

	//destroy method
	@Override
	public void destroy() throws Exception {
		System.out.println("do destroy work ");
	}
}
