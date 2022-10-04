This is Spring demo from Udemy Spring & Hibernate course.   
- REST API design Process
    1. Review API requirements
        - Create a REST API for the customer Relationship Management (CRM) system
        - REST clients should be able to
            - Get a list of customers
            - Get a single customer by id
            - Add a new customer
            - Update a customer
            - Delete a customer

    2. Identify main resource / entity
        - look for the most prominent "noun"
        - For our project, it is "customer"
        - Convention is to use plural form of resource / entity: customers
            - /api/customers
    3. Use HTTP methods to assign action on resource
        - POST : Create a new entity
        - GET : Read a list of entities or single entity
        - PUT : Update an existing entity
        - DELETE : Delete an existing entity
        - CRUD Endpoint : 
            - POST /api/customers : Create a new customer
            - GET /api/customers : Read a list of customers
            - GET /api/customers/{customerId} : Read a single customer
            - PUT /api/customers : Update an existing customer
            - DELETE /api/customers/{customerId} : Delete an existing customer    
        - **For POST and PUT, we will send customer data as JSON in request message body**
- Anti-Patterns : DO NOT DO THIS 
    - Don't include actions in the endpoint, instead, use HTTP methods to assign actions

    ```
    /api/customersList
    /api/deleteCustomer
    /api/addCustomer
    /api/updateCustomer
    ```
- Rest Controller
    - We use @RequestBody for POST and PUT to access the passed in object
    - For POST /api/customers : Create a new customer
        - set the id to 0, for hibernate to insert a new customer

    ```
    package com.example.springdemo.rest;

    import java.util.List;

    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.web.bind.annotation.DeleteMapping;
    import org.springframework.web.bind.annotation.GetMapping;
    import org.springframework.web.bind.annotation.PathVariable;
    import org.springframework.web.bind.annotation.PostMapping;
    import org.springframework.web.bind.annotation.PutMapping;
    import org.springframework.web.bind.annotation.RequestBody;
    import org.springframework.web.bind.annotation.RequestMapping;
    import org.springframework.web.bind.annotation.RestController;

    import com.example.springdemo.entity.Customer;
    import com.example.springdemo.service.CustomerService;

    @RestController
    @RequestMapping("/api")
    public class CustomerRestController {

        // autowire the CustomerService
        @Autowired
        private CustomerService customerService;
        
        // add mapping for GET /customers
        @GetMapping("/customers")
        public List<Customer> getCustomers() {
            
            return customerService.getCustomers();
            
        }
        
        // add mapping for GET /customers/{customerId}
        
        @GetMapping("/customers/{customerId}")
        public Customer getCustomer(@PathVariable int customerId) {
            
            Customer theCustomer = customerService.getCustomer(customerId);
            
            if (theCustomer == null) {
                throw new CustomerNotFoundException("Customer id not found - " + customerId);
            }
            
            return theCustomer;
        }
        
        // add mapping for POST /customers  - add new customer
        
        @PostMapping("/customers")
        public Customer addCustomer(@RequestBody Customer theCustomer) {
            
            // also just in case the pass an id in JSON ... set id to 0
            // this is force a save of new item ... instead of update
            
            theCustomer.setId(0);
            
            customerService.saveCustomer(theCustomer);
            
            return theCustomer;
        }
        
        // add mapping for PUT /customers - update existing customer
        
        @PutMapping("/customers")
        public Customer updateCustomer(@RequestBody Customer theCustomer) {
            
            customerService.saveCustomer(theCustomer);
            
            return theCustomer;
            
        }
        
        // add mapping for DELETE /customers/{customerId} - delete customer
        
        @DeleteMapping("/customers/{customerId}")
        public String deleteCustomer(@PathVariable int customerId) {
            
            Customer tempCustomer = customerService.getCustomer(customerId);
            
            // throw exception if null
            
            if (tempCustomer == null) {
                throw new CustomerNotFoundException("Customer id not found - " + customerId);
            }
                    
            customerService.deleteCustomer(customerId);
            
            return "Deleted customer id - " + customerId;
        }
        
    }
    ```

- Sending JSON to Spring REST Controllers
    - For controller to process JSON data, need to set a **HTTP request header**
    - Content-type: `application/json`
    - Need to configure REST client to send the correct HTTP request header
    - For postman - place the json data into the request body and set the content type to JSON.

- DAO
    - saveCustomer(Customer theCustomer) use session.saveOrUpdate(), so it can handle both insert and update, depending on if the id is set or not.

    ```
    @Override
        public void saveCustomer(Customer theCustomer) {

            // get current hibernate session
            Session currentSession = sessionFactory.getCurrentSession();
            
            // save/upate the customer ... finally LOL
            currentSession.saveOrUpdate(theCustomer);
            
        }
    ```













    
    