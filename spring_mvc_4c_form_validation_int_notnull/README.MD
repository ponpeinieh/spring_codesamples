This is Spring demo from Udemy Spring & Hibernate course. It includes form validation
- For an required integer field, use @NotNull on an int field may throw NumberFormatException
    - If an int field is left empty on input, it will transform into an empty string, ie ""
    - Then it try to convert "" to an int, it throws NumberFormatException
    - The workaround is change int to Integer, so "" is transformed into a null Integer.
- How to handle text input for an Integer field
    - It will show ugly error message, ie. NumberFormatException is seen in the message
    - So we need to override the default spring mvc validation messages
        - create  `resources` folder in Java Resources/src and create messages.properties file in it.
        - add an entry: typeMismatch.customer.freePasses = Invalid number
        - typeMismatch.customer.freePasses error code can be retrieved from BindingResult object
```
Binding result:org.springframework.validation.BeanPropertyBindingResult: 2 errors
Field error in object 'customer' on field 'freePasses': rejected value [dsds]; codes [typeMismatch.customer.freePasses,typeMismatch.freePasses,typeMismatch.java.lang.Integer,typeMismatch]; arguments [org.springframework.context.support.DefaultMessageSourceResolvable: codes [customer.freePasses,freePasses]; arguments []; default message [freePasses]]; default message [Failed to convert property value of type 'java.lang.String' to required type 'java.lang.Integer' for property 'freePasses'; nested exception is java.lang.NumberFormatException: For input string: "dsds"]
Field error in object 'customer' on field 'lastName': rejected value [null]; codes [NotNull.customer.lastName,NotNull.lastName,NotNull.java.lang.String,NotNull]; arguments [org.springframework.context.support.DefaultMessageSourceResolvable: codes [customer.lastName,lastName]; arguments []; default message [lastName]]; default message [is required]
```

        - add a bean into spring config file WEB-INF/spring-mvc-demo-servlet.xml
```
<bean id="messageSource" 
    class="org.springframework.context.support.ResourceBundleMessageSource">
    <property name="basenames" value="resources/messages" />
</bean>
```



