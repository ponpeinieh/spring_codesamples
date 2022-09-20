This is Spring demo from Udemy Spring & Hibernate course. It includes hibernate  demo.
How to view SQL parameter values?
- Use LogBack api , download here : https://repo1.maven.org/maven2/ch/qos/logback/
	- logback-core-1.3.1.jar
	- logback-classic-1.3.1.jar
- It requires slf4j, download here : https://repo1.maven.org/maven2/org/slf4j/slf4j-api/
	- slf4j-api-2.0.1.jar
- Maven dependencies:
	- no need  to explicitly list dependencies for slf4j-api.jar and logback-core.jar. 
	- The logback-classic dependency will automatically pull-in slf4j-api.jar and logback-core.jar into your project since they are transitive dependencies.

```
<dependency>
    <groupId>ch.qos.logback</groupId>
    <artifactId>logback-classic</artifactId>
    <version>1.2.7</version>
</dependency>
```

- Prepare the logback config file - logback.xml and put it under src folder
	- If you are using Maven, place this file at src/main/resources/logback.xml
	- Specify the package in the logger name attribute : <logger name="com.example.hibernate.demo" level="TRACE"/> 

```
<?xml version="1.0" encoding="UTF-8"?>
<configuration>
 
    <appender name="console" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>%-5level %logger{0} - %msg%n</pattern>
        </encoder>
    </appender>
 
    <logger name="com.example.hibernate.demo" level="TRACE"/> 
    <logger name="org.hibernate.SQL" level="DEBUG" />
    <logger name="org.hibernate.type" level="TRACE" />
 
    <root level="info">
        <appender-ref ref="console"/>
    </root>
 
</configuration>
```
- The output log show the SQL parameter values

```
Create 3 student objects ...
DEBUG SQL - insert into student (email, first_name, last_name) values (?, ?, ?)
Hibernate: insert into student (email, first_name, last_name) values (?, ?, ?)
TRACE BasicBinder - binding parameter [1] as [VARCHAR] - [bsmith@example.com]
TRACE BasicBinder - binding parameter [2] as [VARCHAR] - [Bruce]
TRACE BasicBinder - binding parameter [3] as [VARCHAR] - [Smith]
DEBUG SQL - insert into student (email, first_name, last_name) values (?, ?, ?)
Hibernate: insert into student (email, first_name, last_name) values (?, ?, ?)
TRACE BasicBinder - binding parameter [1] as [VARCHAR] - [ssmith@example.com]
TRACE BasicBinder - binding parameter [2] as [VARCHAR] - [Steve]
TRACE BasicBinder - binding parameter [3] as [VARCHAR] - [Smith]
DEBUG SQL - insert into student (email, first_name, last_name) values (?, ?, ?)
Hibernate: insert into student (email, first_name, last_name) values (?, ?, ?)
TRACE BasicBinder - binding parameter [1] as [VARCHAR] - [ksmith@example.com]
TRACE BasicBinder - binding parameter [2] as [VARCHAR] - [Kevin]
TRACE BasicBinder - binding parameter [3] as [VARCHAR] - [Smith]
```