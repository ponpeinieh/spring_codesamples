This is Spring demo from Udemy Spring & Hibernate course. It includes hibernate demo.  
- Fetch Type: EAGER or LAZY
	- default fetch types from JPA 2.0 spec:
		- @OneToOne : FetchType.EAGER
		- @OneToMany : FetchType.LAZY
		- @ManyToOne : FetchType.EAGER
		- @ManyToMany : FetchType.LAZY
- To fetch lazy data, you need a open hibernate session. After Hibernate session is closed, attempt to retrieve lazy data will throw an exception.
	- First option: use session.get() and then call getter methods
	- Second option: use HQL queries
- Lazy loading prefered

```
// Create a SessionFactory
var sessionFactory = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(Instructor.class)
		.addAnnotatedClass(InstructorDetail.class).addAnnotatedClass(Course.class).buildSessionFactory();
// Create a Session
var session = sessionFactory.getCurrentSession();
// create a Instructor object
try { 
	session.beginTransaction();
	// get the instructor from db
	int id=2;
	Instructor i = session.get(Instructor.class, id);
	System.out.println("Instructor:"+i);
	System.out.println("Courses:"+i.getCourses()); //get the lazy data
	session.getTransaction().commit();
	System.out.println("Done!");
} catch (Exception ex) {
	ex.printStackTrace();
} finally {
	session.close();
	sessionFactory.close();
}
```

- The log shows a separate query for the courses

```
Hibernate: select instructor0_.id as id1_1_0_, instructor0_.email as email2_1_0_, instructor0_.first_name as first_na3_1_0_, instructor0_.instructor_detail_id as instruct5_1_0_, instructor0_.last_name as last_nam4_1_0_, instructor1_.id as id1_2_1_, instructor1_.hobby as hobby2_2_1_, instructor1_.youtube_channel as youtube_3_2_1_ from instructor instructor0_ left outer join instructor_detail instructor1_ on instructor0_.instructor_detail_id=instructor1_.id where instructor0_.id=?
TRACE BasicBinder - binding parameter [1] as [INTEGER] - [2]

Hibernate: select courses0_.instructor_id as instruct3_0_0_, courses0_.id as id1_0_0_, courses0_.id as id1_0_1_, courses0_.instructor_id as instruct3_0_1_, courses0_.title as title2_0_1_ from course courses0_ where courses0_.instructor_id=?
TRACE BasicBinder - binding parameter [1] as [INTEGER] - [2]
```

- Change FetchType from default LAZY to EAGER

```
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "instructor_detail_id")
	private InstructorDetail instructorDetail;
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "instructor", cascade = { CascadeType.DETACH, CascadeType.MERGE,
			CascadeType.PERSIST, CascadeType.REFRESH })
	private List<Course> courses;

```

```
Hibernate: select instructor0_.id as id1_1_0_, instructor0_.email as email2_1_0_, instructor0_.first_name as first_na3_1_0_, instructor0_.instructor_detail_id as instruct5_1_0_, instructor0_.last_name as last_nam4_1_0_, courses1_.instructor_id as instruct3_0_1_, courses1_.id as id1_0_1_, courses1_.id as id1_0_2_, courses1_.instructor_id as instruct3_0_2_, courses1_.title as title2_0_2_, instructor2_.id as id1_2_3_, instructor2_.hobby as hobby2_2_3_, instructor2_.youtube_channel as youtube_3_2_3_ from instructor instructor0_ left outer join course courses1_ on instructor0_.id=courses1_.instructor_id left outer join instructor_detail instructor2_ on instructor0_.instructor_detail_id=instructor2_.id where instructor0_.id=?
```

- Retrieve lazy objects after the session is closed - throw LazyInitializationException

```
session.beginTransaction();
// get the instructor from db
int id=7;
Instructor i = session.get(Instructor.class, id);
System.out.println("Instructor:"+i);
session.getTransaction().commit();
session.close();
//
System.out.println("Courses:"+i.getCourses());
System.out.println("Done!");
```

```
org.hibernate.LazyInitializationException: failed to lazily initialize a collection of role: com.example.hibernate.demo.entity.Instructor.courses, could not initialize proxy - no Session
```

- Use HQL queries
	- Here `join fetch` is used and for `query.getSingleResult()` , it returns a single Instructor object with associated course collections
	- See the other markdown for comparision on Join, Left Join and Join Fetch in HQL.

```
// Create a SessionFactory
var sessionFactory = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(Instructor.class)
		.addAnnotatedClass(InstructorDetail.class).addAnnotatedClass(Course.class).buildSessionFactory();
// Create a Session
var session = sessionFactory.getCurrentSession();
// create a Instructor object
try {
	session.beginTransaction();
	// hibernate query with HQL
	int id=7;
	Query<Instructor> query = session.createQuery(
			"select i from Instructor i " + "join fetch i.courses " + "where i.id=:instructorId",
			Instructor.class);
	//set parameter on query
	query.setParameter("instructorId", id);
	Instructor i = query.getSingleResult();
	System.out.println("Instructor:"+i);
	session.getTransaction().commit();
	//close the session
	session.close();
	//get the courses 
	System.out.println("Courses:"+i.getCourses());
	//
	System.out.println("Done!");
} catch (Exception ex) {
	ex.printStackTrace();
} finally {
	session.close();
	sessionFactory.close();
}
```

```
Hibernate: select instructor0_.id as id1_1_0_, courses1_.id as id1_0_1_, instructor0_.email as email2_1_0_, instructor0_.first_name as first_na3_1_0_, instructor0_.instructor_detail_id as instruct5_1_0_, instructor0_.last_name as last_nam4_1_0_, courses1_.instructor_id as instruct3_0_1_, courses1_.title as title2_0_1_, courses1_.instructor_id as instruct3_0_0__, courses1_.id as id1_0_0__ from instructor instructor0_ inner join course courses1_ on instructor0_.id=courses1_.instructor_id where instructor0_.id=?

Hibernate: select instructor0_.id as id1_2_0_, instructor0_.hobby as hobby2_2_0_, instructor0_.youtube_channel as youtube_3_2_0_, instructor1_.id as id1_1_1_, instructor1_.email as email2_1_1_, instructor1_.first_name as first_na3_1_1_, instructor1_.instructor_detail_id as instruct5_1_1_, instructor1_.last_name as last_nam4_1_1_ from instructor_detail instructor0_ left outer join instructor instructor1_ on instructor0_.id=instructor1_.instructor_detail_id where instructor0_.id=?

Hibernate: select instructor0_.id as id1_1_1_, instructor0_.email as email2_1_1_, instructor0_.first_name as first_na3_1_1_, instructor0_.instructor_detail_id as instruct5_1_1_, instructor0_.last_name as last_nam4_1_1_, instructor1_.id as id1_2_0_, instructor1_.hobby as hobby2_2_0_, instructor1_.youtube_channel as youtube_3_2_0_ from instructor instructor0_ left outer join instructor_detail instructor1_ on instructor0_.instructor_detail_id=instructor1_.id where instructor0_.instructor_detail_id=?

```

- Load the courses at a later time in the application?

```
// Create a SessionFactory
var sessionFactory = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(Instructor.class)
		.addAnnotatedClass(InstructorDetail.class).addAnnotatedClass(Course.class).buildSessionFactory();
// Create a Session
var session = sessionFactory.getCurrentSession();
// create a Instructor object
try {
	session.beginTransaction();
	// get the instructor from db
	int id = 7;
	Instructor i = session.get(Instructor.class, id);
	System.out.println("Instructor:" + i);
	session.getTransaction().commit();
	session.close();
	// create new session
	session = sessionFactory.getCurrentSession();
	session.beginTransaction();
	//HQL query for Course
	Query<Course> query = session.createQuery("select a from Course a where a.instructor.id=:id", Course.class);
	query.setParameter("id", id);
	List<Course> courses = query.getResultList();
	session.getTransaction().commit();
	session.close();
	i.setCourses(courses);
	System.out.println("Courses:" + i.getCourses());
	System.out.println("Done!");
} catch (Exception ex) {
	ex.printStackTrace();
} finally {
	session.close();
	sessionFactory.close();
}
```
