package com.example;

public class BaseballCoach implements Coach{
	private FortuneService fortuneService;
	@Override
	public String getDailyWorkout() {
		//  
		return "Workout 30 mins";
	}
 

	public void setFortuneService(FortuneService fortuneService) {
		this.fortuneService = fortuneService;
	}


	public BaseballCoach() {
	}

	public BaseballCoach(FortuneService theService) {
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
	public void doDestroyWork() {
		System.out.println("do destroy work ");
	}
}
