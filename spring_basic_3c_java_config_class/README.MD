This is Spring demo from Udemy Spring & Hibernate course. It includes 
- How to use java class for spring configuration  
- Difference from the spring_basic_3_java_config_class project are:
   1. **Instread of using @ComponentScan, it uses @Bean inside the Config class SportConfig to specify the beans created by the container**
   2. **Use `@Bean` on the method declaration, of which the method name becomes the bean id and the return type is the bean type.** 
   3. This is useful when we are using third party code, which we **don't have the authorship to add @Component annotation.** But we can certainly use @Bean on methods inside our own code.

   ```
   @Bean
	public FortuneService happyFortuneService() {
		return new HappyFortuneService();
	}

	@Bean
	public Coach baseballCoach() {
		return new BaseballCoach(happyFortuneService());
	}
   ```

   3. It can also autowire values from properties file
   
   ```
	@Bean
	public MyLoggerConfig myLoggerConfig(@Value("${root.logger.level}") String rootLoggerLevel,
			@Value("${printed.logger.level}") String printedLoggerLevel) {
		return new MyLoggerConfig(rootLoggerLevel, printedLoggerLevel);
	}
   ```
