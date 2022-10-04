This is Spring demo from Udemy Spring & Hibernate course.   
- Thymeleaf - using tables
    - Controller

    ```
    package com.example.springboot.thymeleaf_demo.controller;

    import java.util.ArrayList;
    import java.util.List;

    import org.springframework.beans.factory.InitializingBean;
    import org.springframework.stereotype.Controller;
    import org.springframework.ui.Model;
    import org.springframework.web.bind.annotation.GetMapping;
    import org.springframework.web.bind.annotation.RequestMapping;

    import com.example.springboot.thymeleaf_demo.entity.Employee;

    @Controller
    @RequestMapping("/employees")
    public class EmployeeController implements InitializingBean {
        private List<Employee> theEmployees = new ArrayList<>();

        @GetMapping("/list")
        public String listEmployees(Model theModel) {
            theModel.addAttribute("employees", theEmployees);
            return "list-employees";
        }

        @Override
        public void afterPropertiesSet() throws Exception {
            theEmployees.add(new Employee("John", "Smith", "js@example.com"));
            theEmployees.add(new Employee("Jane", "Smith", "j2s@example.com"));
            theEmployees.add(new Employee("Steve", "Oat", "so@example.com"));
            theEmployees.add(new Employee("Peter", "Nie", "pn@example.com"));

        }
    }

    ```

    ```
    <! doctype html>
    <html xmlns:th="http://www.thymeleaf.org">
    <head>
    <title>Thymeleaf list employees demo</title>
    <!-- reference css -->
    <link rel="stylesheet" th:href="@{/css/demo.css}" />
    </head>
    <body> 
    <h3> Below are the list of our employees:</h3>

    <table border="1">
    <thead>
        <tr>
            <th>First Name </th>
            <th>Last Name </th>
            <th>Email </th>
        </tr>
    </thead>
    <tbody>
        <tr th:each="tempEmployee : ${employees}">
            <td th:text="${tempEmployee.firstName}" />
            <td th:text="${tempEmployee.lastName}" />
            <td th:text="${tempEmployee.email}" />
        </tr>
    </tbody>
    </table>
    </body>
    </html>
    ```