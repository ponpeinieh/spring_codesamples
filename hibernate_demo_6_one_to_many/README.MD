This is Spring demo from Udemy Spring & Hibernate course. It includes hibernate demo. 
 
- One-to-many mapping with Bi-Directional relationship
- One instructor can have many courses, but one course can have only one instructor
- Delete the Course won't delete the Instructor, and vice versa, so the cascade attribute needs to specify all but CascadeType.REMOVE

- Course entity
	- `@JoinColumn` is needed to specify the actual column name of the foreign key in the course table

```
@ManyToOne(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
@JoinColumn(name = "instructor_id")
private Instructor instructor;
```

- Instructor entity
	- Use a collection, such as List, to store the courses.
	- `mappedBy` attribute is needed to specify the field name on the other side of the relation ship which has the @JoinColumn annotation. Similar to the 1-to-1 bi-directional example.

```
@OneToMany(mappedBy = "instructor", cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
		CascadeType.REFRESH })
private List<Course> courses;
```

- In summary, for the 1-to-many relationship, 
	- On the many side of it, that is where the foreign key is defined, @ManyToOne is needed on the FK field and @JoinColumn to specify the FK column name
	- On the one side of it, a collection is required to store the entities and @OneToMany(mappedBy=xxxx) is needed to specify the corresponding field name on the other side of the relationship which has the @JoinColumn annotation.

- A helper method in Instructor is created, ie. `add()` to add the Course entity into the collection and set the corresponding Instructor reference in the Course entity.

- Code
	- Course entity

```
package com.example.hibernate.demo.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "course")
public class Course {
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(name = "title")
	private String title;
	@ManyToOne(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
	@JoinColumn(name = "instructor_id")
	private Instructor instructor;

	public Course(String title) {
		super();
		this.title = title;
	}

	public Course() {
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Instructor getInstructor() {
		return instructor;
	}

	public void setInstructor(Instructor instructor) {
		this.instructor = instructor;
	}

	public int getId() {
		return id;
	}

	@Override
	public String toString() {
		return "Course [id=" + id + ", title=" + title + "]";
	}
}
```
	- Instructor entity:
```
package com.example.hibernate.demo.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "instructor")
public class Instructor {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(name = "first_name")
	private String firstName;
	@Column(name = "last_name")
	private String lastName;
	@Column(name = "email")
	private String email;
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "instructor_detail_id")
	private InstructorDetail instructorDetail;
	@OneToMany(mappedBy = "instructor", cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	private List<Course> courses;

	//
	public Instructor() {
	}

	public Instructor(String firstName, String lastName, String email) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
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

	public InstructorDetail getInstructorDetail() {
		return instructorDetail;
	}

	public void setInstructorDetail(InstructorDetail instructorDetail) {
		this.instructorDetail = instructorDetail;
	}

	public int getId() {
		return id;
	}

	public List<Course> getCourses() {
		return courses;
	}

	public void setCourses(List<Course> courses) {
		this.courses = courses;
	}

	// add the convenience method for bi-directional relationship
	public void add(Course course) {
		if (courses == null) {
			courses = new ArrayList<>();
		}
		courses.add(course);
		course.setInstructor(this);
	}

	@Override
	public String toString() {
		return "Instructor [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", email=" + email
				+ "]";
	}

}
```

- code - create courses - assume the Instructor object is queried from db.
```
// Create a SessionFactory
var sessionFactory = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(Instructor.class)
		.addAnnotatedClass(InstructorDetail.class).addAnnotatedClass(Course.class).buildSessionFactory();
// Create a Session
var session = sessionFactory.getCurrentSession();
try { 
	session.beginTransaction();
	// get the instructor from db
	int id=1;
	Instructor i = session.get(Instructor.class, id);
	//create some courses
	Course c1 = new Course("Yoga class");
	Course c2 = new Course("Photograph class");
	//add courses to instructor
	i.add(c1);
	i.add(c2);
	//save courses
	session.save(c1);
	session.save(c2);
	session.getTransaction().commit();
	System.out.println("Done!");
} catch (Exception ex) {
	ex.printStackTrace();
} finally {
	session.close();
	sessionFactory.close();
}
```

- code - create Instructor/InstructorDetial/Courses
	- below code won't save courses associated with instructor object, why?
	- Test: if CascadeType is changed to CascadeType.ALL , it will save associated course objects.
	- See references : 
		- https://mkyong.com/hibernate/cascade-jpa-hibernate-annotation-common-mistake/
		- https://mkyong.com/hibernate/hibernate-cascade-example-save-update-delete-and-delete-orphan/
	- In summary:
		- The Hibernate save process will causing a ACTION_SAVE_UPDATE action, but the JPA will pass a ACTION_PERSIST and ACTION_MERGE, it will not match and causing the cascade failed to execute.
		- Solution: Delete the JPA cascade(`javax.persistence.CascadeType`), replace it with Hibernate cascade(`org.hibernate.annotations.CascadeType`), with `CascadeType.SAVE_UPDATE`.

```
// Create a SessionFactory
var sessionFactory = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(Instructor.class)
		.addAnnotatedClass(InstructorDetail.class).addAnnotatedClass(Course.class).buildSessionFactory();
// Create a Session
var session = sessionFactory.getCurrentSession();
// create a Instructor object
try {
	Instructor i = new Instructor("Jane", "Smith", "jsmith@example.com");
	InstructorDetail idl = new InstructorDetail("https://....", "Coding");
	i.setInstructorDetail(idl);
	//add courses
	Course c1 = new Course("Yoga class");
	Course c2 = new Course("Photograph class");
	//add courses to instructor
	i.add(c1);
	i.add(c2);
	System.out.println("Saving the Instructor object!");
	session.beginTransaction();
	// this will also save the InstructorDetail object, because of CascadeType.ALL
	// but this won't save the courses, why?
	session.save(i);
	session.getTransaction().commit();
	System.out.println("Done!");
} catch (Exception ex) {
	ex.printStackTrace();
} finally {
	session.close();
	sessionFactory.close();
}
```