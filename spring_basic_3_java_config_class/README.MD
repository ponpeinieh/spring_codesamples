This is Spring demo from Udemy Spring & Hibernate course. It includes 
- How to use java class for spring configuration 
   - there are basic 3 ways to config spring container, ie
      1. Full XML config
      2. XML Component Scan
      3. Java Configuration Class
- Java Config Class - Development Process
      1. Create a Java class and annotate as @Configuration
      2. Add component scanning support: @ComponentScan (optional)
      3. Read Spring Java configuration class
      4. Retrieve bean from Spring container
- Config class - SportConfig
   - @Configuation 
   - @ComponentScan("com.example") 
   - @PropertySource("classpath:sport.properties") for loading properties file
- use AnnotationConfigApplicationContext , ie. 
   - AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SportConfig.class);
- For logger config - MyLoggerConfig
   - use it as a component with @Component annotation and read values from mylogger.properties file
   - @PropertySource("classpath:mylogger.properties") is specified in SportConfig, but can also speicified in MyLoggerConfig