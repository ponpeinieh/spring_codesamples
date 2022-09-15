This is a first Spring demo from Udemy Spring & Hibernate course. It includes
- basic inversion of control
- basic dependency injection
   - using xml config files
- class structures:
   - Coach - interface
   - FortuneService - interface
   - BaseballCoach, CricketCoach etc are bean classes implementing Coach
   - HappyFortuneService, MyFortuneService implements FortuneService
   - applicationContext.xml - xml config file
      1. use constructor injection
      1. use setter injection
      1. use property placeholder to read in properties file
      1. setup logger service - change default logger level to somthing other than INFO
   - beanScope-applicationContext.xml - xml config file
      1. test  bean scopes, eg. singleton and prototype
   - beanLifecycle-applicationContext.xml - xml config file
      1. test  bean lifecycle methods, eg. init-method, destroy-method
      1. destroy-method is not applied to prototype beans, it needs workaround
   - beanLifecycle-applicationContext2.xml - xml config file
      1. test destroy work for prototype beans 
      1. it uses BeanPostProcessor mechanism, ie. to create a processor(also a bean) to record 
all prototype beans and store in a list and when this processor object is disposed, its destroy() method
is called and it also make sure all prototype beans' destroy() method are called.


