package com.example;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration // no @ComponentScan so we define beans within the config class using @Bean annotation
@PropertySource("classpath:mylogger.properties")
public class SportConfig {
	// define beans
	@Bean
	public FortuneService happyFortuneService() {
		return new HappyFortuneService();
	}

	@Bean
	public Coach baseballCoach() {
		return new BaseballCoach(happyFortuneService());
	}

	@Bean
	public MyLoggerConfig myLoggerConfig(@Value("${root.logger.level}") String rootLoggerLevel,
			@Value("${printed.logger.level}") String printedLoggerLevel) {
		return new MyLoggerConfig(rootLoggerLevel, printedLoggerLevel);
	}
}
