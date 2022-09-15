package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

//@Component("thisSillyCoach")
@Component //with default name of baseballCoach
@Scope("singleton") //or "prototype"
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
//	@Autowired
//	public BaseballCoach(
//			@Qualifier("happyFortuneService") FortuneService theService) {
//		System.out.println("BaseballCoach cstr is called.");
//		this.fortuneService=theService;
//	}
	@Autowired
	@Qualifier("myFortuneService")
	public void setFortuneService(FortuneService fortuneService) {
		System.out.println("BaseballCoach setter is called.");
		this.fortuneService = fortuneService;
	}
//	@Autowired //no need to be called setXXXX
//	@Qualifier("myFortuneService")
//	public void doSomethingUseFortuneService(FortuneService fortuneService) {
//		System.out.println("doSomethingUseFortuneService is called.");
//		this.fortuneService = fortuneService;
//	}
}
