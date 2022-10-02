This is Spring demo from Udemy Spring & Hibernate course.   
- Spring Security 
    - Use Spring Security JSP tags to display content based on the user role

    ```
    <security:authorize access="hasRole('MANAGER')">
        <p>
        <a href="${pageContext.request.contextPath}/leaders"> Leadership Meeting </a>
        (Only for Manager peeps)
        </p>
    </security:authorize>
    ```
       