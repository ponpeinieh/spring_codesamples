This is Spring demo from Udemy Spring & Hibernate course. It includes hibernate demo. 
 
- One-to-one mapping with Bi-Directional relationship
- change InstructorDetail's Instructor field cascade setting to all but `CascadeType.REMOVE`
	- So delete the InstructorDetail entity will NOT cascade remove the Instructor entity
	- But note that **before deleting the InstructorDetail , we need to break the association between the Instructor and InstructorDetail**.

- InstructorDetail entity
```
@OneToOne(mappedBy = "instructorDetail", cascade = { CascadeType.DETACH, CascadeType.MERGE, 
	CascadeType.PERSIST, CascadeType.REFRESH })
private Instructor instructor;
```

```
var sessionFactory = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(Instructor.class)
		.addAnnotatedClass(InstructorDetail.class).buildSessionFactory();
// Create a Session
var session = sessionFactory.getCurrentSession();
try {
	// retrieve an InstructorDetail object
	int id = 8; 
	session.beginTransaction();
	InstructorDetail idl = session.get(InstructorDetail.class, id);
	Instructor i = idl.getInstructor();
	System.out.println("delete instructorDetail");
	i.setInstructorDetail(null); // break the association between the Instructor and InstructorDetail
	session.delete(idl); //delete the InstructorDetail
	session.getTransaction().commit();
	System.out.println("Done!");
} catch (Exception ex) {
	ex.printStackTrace();
} finally {
	session.close();
	sessionFactory.close();
}
```
- orphanRemoval 
	- We can annotate the OneToOne mapping of InstructorDetail field in Instructor entity with orphanRemoval attribute set to true, so it will automatically remove the child entity (InstructorDetail) once the association has been broken
	- Below notes are from Oracle
```
Orphan Removal in Relationships
When a target entity in one-to-one or one-to-many relationship is removed from the relationship, it is often desirable to cascade the remove operation to the target entity. Such target entities are considered “orphans,” and the orphanRemoval attribute can be used to specify that orphaned entities should be removed. For example, if an order has many line items and one of them is removed from the order, the removed line item is considered an orphan. If orphanRemoval is set to true, the line item entity will be deleted when the line item is removed from the order.
```

- Instructor entity
```
@OneToOne(cascade = CascadeType.ALL, orphanRemoval=true)
@JoinColumn(name = "instructor_detail_id")
private InstructorDetail instructorDetail;
```

- So we can delete the InstructorDetail entity but not cascade delete the Instructor entity as below code:

```
var sessionFactory = new Configuration().configure("hibernate.cfg.xml")
		.addAnnotatedClass(Instructor.class)
		.addAnnotatedClass(InstructorDetail.class)
		.buildSessionFactory();
// Create a Session
var session = sessionFactory.getCurrentSession();
try {
	// retrieve an InstructorDetail object
	int id = 8; 
	session.beginTransaction();
	InstructorDetail idl = session.get(InstructorDetail.class, id);
	Instructor i = idl.getInstructor();
	System.out.println("delete instructorDetail");
	i.setInstructorDetail(null);
	//session.delete(idl); //don't need to write this 
	session.getTransaction().commit();
	System.out.println("Done!");
} catch (Exception ex) {
	ex.printStackTrace();
} finally {
	session.close();
	sessionFactory.close();
}
```		

- The log result:

```
DEBUG SQL - update instructor set email=?, first_name=?, instructor_detail_id=?, last_name=? where id=?
Hibernate: update instructor set email=?, first_name=?, instructor_detail_id=?, last_name=? where id=?
TRACE BasicBinder - binding parameter [1] as [VARCHAR] - [jsmith@example.com]
TRACE BasicBinder - binding parameter [2] as [VARCHAR] - [John]
TRACE BasicBinder - binding parameter [3] as [INTEGER] - [null] <-- set the instructor_detail_id to null
TRACE BasicBinder - binding parameter [4] as [VARCHAR] - [Smith]
TRACE BasicBinder - binding parameter [5] as [INTEGER] - [4]
DEBUG SQL - delete from instructor_detail where id=?
Hibernate: delete from instructor_detail where id=?
TRACE BasicBinder - binding parameter [1] as [INTEGER] - [8]
```

- Note that if we still have the cascade type set to `CascadeType.ALL` for the Instructor field in InstructorDetail entity, 
and the `orphanRemoval` set to true in InstructorDetail field in Instructor entity, breaking the association between Instructor and InstructorDetail, will not only delete the InstructorDetail entity, but also the Instructor entity.