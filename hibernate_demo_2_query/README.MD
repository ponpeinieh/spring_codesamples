This is Spring demo from Udemy Spring & Hibernate course. It includes hibernate  demo.

- @Id annotation 
	- Hibernate identity - primary key
	- let database generate a pk for us
	- can specify different pk generation strategies
	- @GeneratedValue(strategy=GenerationType.IDENTITY) - use auto increment pk

- Primary key generation strategies:
	- GenerationType.AUTO : use an appropriate strategy for the particular db
	- GenerationType.IDENTITY : assign pk using database identity column
	- GenerationType.SEQUENCE : assign pk using a database sequence
	- GenerationType.TABLE : assign pk using an underlying database table to ensure uniqueness


- SQL command to change auto increment value:
	- ALTER TABLE hb_student_tracker.student AUTO_INCREMENT=1000;

- SQL command to clear database table contents
	- TRUNCATE hb_student_tracker.student;

- CRUD : create, retrieve, update, delete

- Retrieve an object based on id 
	- session.get(Student.class, s.getId())
	- create new session and begin a new transaction and commit the transaction after retrieve
```
Student s = new Student("Daffy", "Duck", "daffy@example.com");
session.beginTransaction();
session.save(s);
session.getTransaction().commit();
//
session = sessionFactory.getCurrentSession();
session.beginTransaction();
Student s2 = session.get(Student.class, s.getId());
System.out.println("Retrieved Student :" + s2);
session.getTransaction().commit();
```
- Query objects with HQL(hibernate query language)
	- note that **the query strings use java class/field names not database table/column names**

```
var sessionFactory = new Configuration().configure("hibernate.cfg.xml")
	.addAnnotatedClass(Student.class).buildSessionFactory();
// Create a Session
var session = sessionFactory.getCurrentSession();
try {
	System.out.println("Query student object ...");
	session.beginTransaction();
	// query students

	List<Student> students = session.createQuery("from Student").getResultList();
	//List<Student> students = session.createQuery("from Student s where s.lastName='Duck'").getResultList();
	//List<Student> students = session.createQuery("from Student s where s.lastName='Duck' or s.firstName='Kevin'").getResultList();
	//List<Student> students = session.createQuery("from Student s where s.email like '%example%'").getResultList();

	session.getTransaction().commit();
	System.out.println("Done!");
	// display students
	students.stream().forEach(System.out::println);
} finally {
	sessionFactory.close();
}
```