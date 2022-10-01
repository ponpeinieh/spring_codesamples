This is Spring demo from Udemy Spring & Hibernate course.   
- Spring Security - Cross Site Request Forgery (CSRF)
    - Use Spring MVC form tag <form:form>, because it provides automatic support for security defenses
    - <form:form> will place a hidden field (the csrf token) in the form .
    - If not using <form:form> , you have to manually place a hidden field representing the csrf token in the form, otherwise it will report Http 403 error.
    
    ```
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
    ```

