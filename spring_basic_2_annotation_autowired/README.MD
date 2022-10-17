This is Spring demo from Udemy Spring & Hibernate course. It includes 
- How to use java annotation for spring configuration
   - Developement processes:
      1. Enable component scanning in Spring config file
      2. Add the @Component Annotation to your Java classes and specify an id, but default to the class name with first letter decaptialized
      (except for classes whose names are with two leading capitalized letters, then the id will be the same as the class name).
      3. Retrieve bean from Spring container 
- How to autowire dependencies with annotations
   - Spring supports 3 types of autowiring - Constructor, setter and field autowires
   - Constructor autowiring - developement processes:
      1. Define the dependency interface and class
      2. Create a constructor in your class for injections
      3. Configure the dependency injection with @Autowired Annotation on the constructor
	- Note that **since Spring 4.3, @Autowired is not necessary on the constructor if the bean only defines one contructor.**
   - Setter autowiring - developement processes:
      1. Create setter method(s) in your class for injections
      2. Configure the dependency injection with @Autowired Annotation
         - note that the setter method can be called any name, as long as it is @Autowired annotated, it will be called after the bean is created. 
   - Field autowiring 
      1. just put the @Autowired annotation before the field declaration
      2. Use @Value() instead if you are going to assign literal value, such as @Value("xxx") or read it from properties file, such as @Value("${foo.email}")
      3. **Note that if you use field injection (@Autowired on a field), the field is not available in the constructor, only afterwards.**
- `@Autowired` may need a `@Qualifier` annotation to specify a specific bean id if more than one implementation beans are available.
- `@Scope` to specify the scope for the bean, eg. singleton, prototype etc
- Lifecycle methods: before java 8 , we can use `@PostConstruct` and `@PreDestroy` annotations to specify methods to call after initialization and before destruction, but
since java 9 they are deprecated and since java 11 are to be removed. So we need to use new ways.
- **New ways are for beans to implement `InitializingBean` and `DisposableBean` interfaces, and override `afterPropertiesSet()` and `destroy()`** which are called after bean initialization and before bean destruction.