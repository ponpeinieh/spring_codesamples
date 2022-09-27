This is Spring demo from Udemy Spring & Hibernate course. It includes hibernate demo. 
- Hibernate advanced mapping
    - 1-to-1
    - 1-to-many
    - many-to-1
    - many-to-many
- PK and Foreign Key (link tables together)
- Cascade update/delete
    - apply the same operation to related entities 
- Fetch Type : Eager vs Lazy loading
    - Eager : retrieve everything
    - Lazy : retrieve on request
-Uni-Directional
-Bi-Directional

- Cascade Types and Entity Lifecycle:
    - Detach : If entity is detached, it is not associated with a Hibernate session
    - Merge : If instance is detached from session, then merge will reattach to session
    - Persist : Transitions new instances to managed state. Next flush / commit will save in db.
    - Remove : Transitions managed entity to be removed. Next flush / commit will delete from db.
    - Refresh : Reload / synch object with data from db. Prevents stale data
 
- JPA Cascade Type : **javax.persistence.CascadeType** enum containing the following types
    - CascadeType.ALL : propagates all operations, **including Hibernate-specific ones**, from a parent to a child entity.
    - CascadeType.PERSIST : makes a transient instance persistent. Cascade Type PERSIST propagates the persist operation from a parent to a child entity. 
    - CascadeType.MERGE : The merge operation copies the state of the given object onto the persistent object with the same identifier. CascadeType.MERGE propagates the merge operation from a parent to a child entity.
    - CascadeType.REMOVE : CascadeType.REMOVE propagates the remove operation from parent to child entity. Similar to JPA's CascadeType.REMOVE, we have **CascadeType.DELETE, which is specific to Hibernate**. There is no difference between the two.
    - CascadeType.DETACH : The detach operation removes the entity from the persistent context. When we use CascadeType.DETACH, the child entity will also get removed from the persistent context.
    - CascadeType.REFRESH : Refresh operations reread the value of a given instance from the database. In some cases, we may change an instance after persisting in the database, but later we need to undo those changes. In that kind of scenario, this may be useful. When we use this operation with Cascade Type REFRESH, the child entity also gets reloaded from the database whenever the parent entity is refreshed.
- Hibernate Cascade Type: Hibernate supports three additional Cascade Types along with those specified by JPA. These Hibernate-specific Cascade Types are available in **org.hibernate.annotations.CascadeType**:
    - REPLICATE
    - SAVE_UPDATE
    - LOCK


- 1-to-1 mapping, Uni-directional relationship 
    - between Instructor and InstructorDetail entities

- create tables: 
    - Schema: group of tables(similar to Database)
    - SET FOREIGN_KEY_CHECKS = 0; 
        - It affects data definition statements: DROP SCHEMA drops a schema even if it contains tables that have foreign keys that are referred to by tables outside the schema, and DROP TABLE drops tables that have foreign keys that are referred to by other tables.
    - KEY : create an index
        - KEY `FK_DETAIL_idx` (`instructor_detail_id`)
    - Foreign key constraint
        - CONSTRAINT `FK_DETAIL` FOREIGN KEY (`instructor_detail_id`) REFERENCES `instructor_detail` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
        - ON DELETE NO ACTION. ON UPDATE NO ACTION : here we didn't use CASCADE, because we will apply it within the hibernate setting
    - We can use MySQL Workbench to generate database diagrams. 
        - Database > Reverse Engineer
```
DROP SCHEMA IF EXISTS `hb-01-one-to-one-uni`;

CREATE SCHEMA `hb-01-one-to-one-uni`;

use `hb-01-one-to-one-uni`;

SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS `instructor_detail`;

CREATE TABLE `instructor_detail` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `youtube_channel` varchar(128) DEFAULT NULL,
  `hobby` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;


DROP TABLE IF EXISTS `instructor`;

CREATE TABLE `instructor` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `first_name` varchar(45) DEFAULT NULL,
  `last_name` varchar(45) DEFAULT NULL,
  `email` varchar(45) DEFAULT NULL,
  `instructor_detail_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_DETAIL_idx` (`instructor_detail_id`),
  CONSTRAINT `FK_DETAIL` FOREIGN KEY (`instructor_detail_id`) REFERENCES `instructor_detail` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

SET FOREIGN_KEY_CHECKS = 1;
```

- 1-to-1 mapping
    - @OneToOne(cascade = CascadeType.ALL) , by default if no cascade is specified, no operations are cascaded
    - @JoinColumn(name= "instructor_detail_id") , here name attribute is the foreign key column in the instructor table 
```
@OneToOne(cascade = CascadeType.ALL)
@JoinColumn(name = "instructor_detail_id")
private InstructorDetail instructorDetail;
```

- InstructorDetail Entity

```
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "instructor_detail")
public class InstructorDetail {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(name = "youtube_channel")
	private String youtubeChannel;
	@Column(name = "hobby")
	private String hobby;

	public InstructorDetail() {
	}

	public InstructorDetail(String youtubeChannel, String hobby) {
		super();
		this.youtubeChannel = youtubeChannel;
		this.hobby = hobby;
	}

	public String getYoutubeChannel() {
		return youtubeChannel;
	}

	public void setYoutubeChannel(String youtubeChannel) {
		this.youtubeChannel = youtubeChannel;
	}

	public String getHobby() {
		return hobby;
	}

	public void setHobby(String hobby) {
		this.hobby = hobby;
	}

	public int getId() {
		return id;
	}

	@Override
	public String toString() {
		return "InstructorDetail [id=" + id + ", youtubeChannel=" + youtubeChannel + ", hobby=" + hobby + "]";
	}

}
```

- Instructor Entity

```

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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

	@Override
	public String toString() {
		return "Instructor [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", email=" + email
				+ ", instructorDetail=" + instructorDetail + "]";
	}

}

```

- usage

```
var sessionFactory = new Configuration().configure("hibernate.cfg.xml")
        .addAnnotatedClass(Instructor.class)
        .addAnnotatedClass(InstructorDetail.class) 
        .buildSessionFactory();
// Create a Session
var session = sessionFactory.getCurrentSession();
// create a Instructor object

Instructor i = new Instructor("John", "Smith", "jsmith@example.com");
InstructorDetail idl= new InstructorDetail("https://....", "Coding");
i.setInstructorDetail(idl);
//
System.out.println("Saving the Instructor object!");
session.beginTransaction();
//this will also save the InstructorDetail object, because of CascadeType.ALL
session.save(i);
session.getTransaction().commit();
System.out.println("Done!");
```

```
var sessionFactory = new Configuration().configure("hibernate.cfg.xml")
        .addAnnotatedClass(Instructor.class)
        .addAnnotatedClass(InstructorDetail.class)
        .buildSessionFactory();
// Create a Session
var session = sessionFactory.getCurrentSession();
// retrieve an Instructor object
int id = 1;
session.beginTransaction();
Instructor i = session.get(Instructor.class, id); 
if(i!=null) { 
    System.out.println("delete the Instructor object!");
    //this will also delete the InstructorDetail object, because of CascadeType.ALL
    session.delete(i);
}
session.getTransaction().commit();
System.out.println("Done!");
```