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




    
    