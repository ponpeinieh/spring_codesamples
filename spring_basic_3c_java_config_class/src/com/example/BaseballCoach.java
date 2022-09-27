package com.example;

public class BaseballCoach implements Coach {
	private FortuneService fortuneService;

	@Override
	public String getDailyWorkout() {
		//
		return "Workout 30 mins";
	}

	@Override
	public String getDailyFortune() {
		return fortuneService.getFortune();
	}

//	public BaseballCoach() {
//	}

	public BaseballCoach(FortuneService theService) {
		System.out.println("BaseballCoach cstr is called.");
		this.fortuneService = theService;
	}
}
