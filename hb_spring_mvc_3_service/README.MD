This is Spring demo from Udemy Spring & Hibernate course.
- @RequestMapping() generally handles all HTTP methods

```
@RequestMapping("/processForm")
public String processForm(...) {
...
}
```

- Constrain the Request mapping - only handle Get/Post request
	- method=RequestMethod.GET for Get method only
	- method=RequestMethod.POST for Post method only
	- Or use new Annotation short-cut
		- @GetMapping()
		- @PostMapping()

```
@RequestMapping(path="/processForm", method=RequestMethod.GET)
public String processForm(...) {
...
}
```

```
@GetMapping("/processForm")
public String processForm(...) {
...
}
```

- Service Layer: 
	- Service lies between Controller and DAO
	- Follow Service Facade design pattern
	- Intermediate layer for custom business logic
	- Integrate data from multiple sources(DAO/repositories)
	- Making use of @Transactional at service layer, we can have the transactional support on multiple DAO method calls, which will run in the same transaction.
	- Most times - just pass delegate calls to DAOs
	- @Service annotation
		- extends @Component
		- applied to Service implementations
	- Developement processes:
		1. Define Service interface

			```
			public interface CustomerService {
				public List<Customer> getCustomers();
			}
			```

		2. Define Service implementation
			- inject DAOs
			- Previously, we annotate @Transactional on DAOs, so use it on Services and we need to remove them from DAOs

			```
			@Service
			public class CustomerServiceImpl implements CustomerService {
				@Autowired
				private CustomerDAO customerDAO;
				@Transactional
				public List<Customer> getCustomers() {
				...
				}
			}
			```

