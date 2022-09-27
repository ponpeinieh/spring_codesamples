This is Spring demo from Udemy Spring & Hibernate course. It includes hibernate setup.
- Hibernate version:
	- 2022/3: Hibernate 6 was released, but it supports Jakarta EE 9 which rename javax.* packages to jakarta.*
	- Spring 5 currently doesn't support Jarkarta EE 9, so it can not use Hibernate version 6.
	- Here **Hibernate version 5 is used for Spring 5 applications.**
- Hibernate 5.6 version download - https://sourceforge.net/projects/hibernate/files/hibernate-orm/5.6.5.Final/
	- The jars are in lib/required folder
- Mysql connector/J driver download - https://dev.mysql.com/downloads/connector/j/
	- The jar has been downloaded when install mysql , which is located in C:\Program Files (x86)\MySQL\Connector J 8.0
	- mysql jdbc url : String jdbcUrl = "jdbc:mysql://localhost:3306/hb_student_tracker?useSSL=false&serverTimezone=UTC";
- Developement process for using hibernate:
	1. add hibernate config file
	2. annotate java class
	3. write java code
- Hibernate config file : `hibernate.cfg.xml`
	- put this file in the `src` folder
```
<!DOCTYPE hibernate-configuration PUBLIC
        "-//Hibernate/Hibernate Configuration DTD 3.0//EN"
        "http://www.hibernate.org/dtd/hibernate-configuration-3.0.dtd">

<hibernate-configuration>

    <session-factory>

        <!-- JDBC Database connection settings -->
        <property name="connection.driver_class">com.mysql.cj.jdbc.Driver</property>
        <property name="connection.url">jdbc:mysql://localhost:3306/hb_student_tracker?useSSL=false&amp;serverTimezone=UTC</property>
        <property name="connection.username">hbstudent</property>
        <property name="connection.password">hbstudent</property>

        <!-- JDBC connection pool settings ... using built-in test pool -->
        <property name="connection.pool_size">1</property>

        <!-- Select our SQL dialect -->
        <property name="dialect">org.hibernate.dialect.MySQLDialect</property>

        <!-- Echo the SQL to stdout -->
        <property name="show_sql">true</property>

		<!-- Set the current session context -->
		<property name="current_session_context_class">thread</property>
 
    </session-factory>

</hibernate-configuration>
```
Summary here: 
- Entity class - just a POJO
- ORM - Object to Relational DB Table mapping
	- two mapping options
		1. Legacy XML config file
		2. Modern Java Annotations
- Java Annotation mapping option - steps:
	1. Map class to db table
	2. Map fields to table columns
- SessionFactory: 
	- Reads the hibernate config file
	- Create Session objects
	- Heavy-weight object, so only created once in your app
- Session :
	- Wrap a JDBC connection
	- used to save/retrieve objects
	- Short-lived object, and retrieved from SessionFactory

### Entity class : Student
	- make sure **the annotations from java JPA is used, instead of Hibernate**
	- @Table `name` attribute can be omitted if the same as the table name, the same for @Column name too

```
//use java persistence API (JPA) instead of Hibernate JPA　implementation class
import javax.persistence.Column;
import javax.persistence.Entity; 
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "student")
public class Student {

	@Id
	@Column(name = "id")
	private int id;

	@Column(name = "first_name")
	private String firstName;
	@Column(name = "last_name")
	private String lastName;
	@Column(name = "email")
	private String email;

	public Student() {
	}

	public Student(String firstName, String lastName, String email) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "Student [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", email=" + email + "]";
	}
}
```

### Hibernate code 

- configure() method can use the default hibernate config file of `hibernate.cfg.xml`, so no input parameter is specified.
``` 
import org.hibernate.cfg.Configuration;

import com.example.hibernate.demo.entity.Student;

public class CreateStudentDemo {

	public static void main(String[] args) {
		// Create a SessionFactory
		SessionFactory sessionFactory = new Configuration().configure("hibernate.cfg.xml")
			.addAnnotatedClass(Student.class).buildSessionFactory();
		// Create a Session
		Session session = sessionFactory.getCurrentSession();
		// create a Student object

		Student s = new Student("John", "Smith", "jsmith@example.com");
		System.out.println("Saving the student object!");
		session.beginTransaction();
		session.save(s);
		session.getTransaction().commit();
		System.out.println("Done!");

	}

}
```

