This is Spring demo from Udemy Spring & Hibernate course. It includes hibernate demo. 
- Many-To-Many mapping bi-directional relationship - Course and Student
    - Course entity:
        - Note that `Set` should be used instead of `List`
        - **When you map a many-to-many association, you should use a `Set` instead of a List as the attribute type.** Otherwise, Hibernate will take a very inefficient approach to remove entities from the association. **It will remove all records from the association table and re-insert the remaining ones. You can avoid that by using a Set instead of a List as the attribute type.**
        - It is also essential that **CascadeType.REMOVE is not specified for many-to-many relationship**, otherwise it will cascade remove a lot more records than you would expect!!!

    ```
    @ManyToMany(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
	@JoinTable(name = "course_student", 
            joinColumns = { @JoinColumn(name = "course_id") }, 
            inverseJoinColumns = { @JoinColumn(name = "student_id") })
	private List<Student> students; //later changed to Set

    //getter and setter for students

    //utility method for adding a Student
	public void addStudent(Student s) {
		if (students == null) {
			students = new ArrayList<Student>();  //later changed to HashSet
		}
		students.add(s);
	}
	```

    - Student entity:

    ```
    @ManyToMany(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
	@JoinTable(name="course_student",
	        joinColumns= { @JoinColumn(name="student_id")}, 
	        inverseJoinColumns= { @JoinColumn(name="course_id")})
	private List<Course> courses; //later changed to Set

    //getter and setter for students

    //utility method for adding a Course
    public void add(Course course) {
		if (courses == null) {
			courses = new ArrayList<>();  //later changed to HashSet
		}
		courses.add(course);
	}
    ```

    - code for creating Courses and Students
        - below code **first save the Course entity and then save the Student entities separately**
        - if not calling session.save() on Student entities, it will throw exception:
        ```
        java.lang.IllegalStateException: org.hibernate.TransientObjectException: object references an unsaved transient instance - save the transient instance before flushing: com.example.hibernate.demo.entity.Student
        ```
        - This means that hibernate doesn't cascase save the **transient** Student entities. **Why?** 
            - The reason for this is similar to the 1-to-many case encountered previously.
        - In order for the cascade to work, we can add the `@Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)` , ie. the `Hibernate CascadeType` instead of `JPA CascadeType`, on the Course entity.

        ```
        // Create a SessionFactory
        var sessionFactory = new Configuration().configure("hibernate.cfg.xml")
                .addAnnotatedClass(Instructor.class)
                .addAnnotatedClass(InstructorDetail.class)
                .addAnnotatedClass(Course.class)
                .addAnnotatedClass(Review.class)
                .addAnnotatedClass(Student.class).buildSessionFactory();
        // Create a Session
        var session = sessionFactory.getCurrentSession();
        // create a Instructor object
        try { 
            session.beginTransaction();
            //create a course
            Course c = new Course("How to fry a streak");
            //save the course
            session.save(c);
            //add some students
            Student s1 = new Student("John","Smith","jsmith@gmail.com");
            Student s2 = new Student("Jane","Smith","jsmith@gmail.com");
            c.addStudent(s1);
            c.addStudent(s2);
            //
            session.save(s1);
            session.save(s2);
            session.getTransaction().commit();
            System.out.println("Done!");
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            session.close();
            sessionFactory.close();
        }
        ```

        ```
        Hibernate: insert into course (instructor_id, title) values (?, ?)
        TRACE BasicBinder - binding parameter [1] as [INTEGER] - [null]
        TRACE BasicBinder - binding parameter [2] as [VARCHAR] - [How to fry a streak]

        Hibernate: insert into student (email, first_name, last_name) values (?, ?, ?)
        TRACE BasicBinder - binding parameter [1] as [VARCHAR] - [jsmith@gmail.com]
        TRACE BasicBinder - binding parameter [2] as [VARCHAR] - [John]
        TRACE BasicBinder - binding parameter [3] as [VARCHAR] - [Smith]

        Hibernate: insert into student (email, first_name, last_name) values (?, ?, ?)
        TRACE BasicBinder - binding parameter [1] as [VARCHAR] - [jsmith@gmail.com]
        TRACE BasicBinder - binding parameter [2] as [VARCHAR] - [Jane]
        TRACE BasicBinder - binding parameter [3] as [VARCHAR] - [Smith]

        Hibernate: insert into course_student (course_id, student_id) values (?, ?)
        TRACE BasicBinder - binding parameter [1] as [INTEGER] - [10]
        TRACE BasicBinder - binding parameter [2] as [INTEGER] - [1]

        Hibernate: insert into course_student (course_id, student_id) values (?, ?)
        TRACE BasicBinder - binding parameter [1] as [INTEGER] - [10]
        TRACE BasicBinder - binding parameter [2] as [INTEGER] - [2]
        ```

    - code for adding Courses for existing student - 1st try

        ```
        Student s1 = session.get(Student.class, 1);
        System.out.println("Student:"+s1);
        System.out.println("Courses:"+s1.getCourses());
        //create more courses
        Course c1 = new Course("Rubik's Cube");
        Course c2 = new Course("Game development");
        c1.addStudent(s1);
        c2.addStudent(s1);
        session.save(c1);
        session.save(c2);
        ```

        ```
        Hibernate: select student0_.id as id1_5_0_, student0_.email as email2_5_0_, student0_.first_name as first_na3_5_0_, student0_.last_name as last_nam4_5_0_ from student student0_ where student0_.id=?

        Hibernate: select courses0_.student_id as student_1_1_0_, courses0_.course_id as course_i2_1_0_, course1_.id as id1_0_1_, course1_.instructor_id as instruct3_0_1_, course1_.title as title2_0_1_, instructor2_.id as id1_2_2_, instructor2_.email as email2_2_2_, instructor2_.first_name as first_na3_2_2_, instructor2_.instructor_detail_id as instruct5_2_2_, instructor2_.last_name as last_nam4_2_2_, instructor3_.id as id1_3_3_, instructor3_.hobby as hobby2_3_3_, instructor3_.youtube_channel as youtube_3_3_3_ from course_student courses0_ inner join course course1_ on courses0_.course_id=course1_.id left outer join instructor instructor2_ on course1_.instructor_id=instructor2_.id left outer join instructor_detail instructor3_ on instructor2_.instructor_detail_id=instructor3_.id where courses0_.student_id=?

        Hibernate: insert into course (instructor_id, title) values (?, ?)
        Hibernate: insert into course (instructor_id, title) values (?, ?)
        Hibernate: insert into course_student (course_id, student_id) values (?, ?)
        Hibernate: insert into course_student (course_id, student_id) values (?, ?)
        ```        

    - code for adding Courses for existing student - 2nd try
        - Here calling s1.add(c1) trigger a deletion of all join entries for this student and then re-enter the entries
        - A very bad approach!!!
        
        ```
        Student s1 = session.get(Student.class, 1);
        System.out.println("Student:"+s1);
        System.out.println("Courses:"+s1.getCourses());
        //create more courses
        Course c1 = new Course("Rubik's Cube");
        Course c2 = new Course("Game development");
        session.save(c1);
		session.save(c2);
		s1.add(c1);
		s1.add(c2);
        ```

        ```
        Hibernate: select student0_.id as id1_5_0_, student0_.email as email2_5_0_, student0_.first_name as first_na3_5_0_, student0_.last_name as last_nam4_5_0_ from student student0_ where student0_.id=?

        Hibernate: select courses0_.student_id as student_1_1_0_, courses0_.course_id as course_i2_1_0_, course1_.id as id1_0_1_, course1_.instructor_id as instruct3_0_1_, course1_.title as title2_0_1_, instructor2_.id as id1_2_2_, instructor2_.email as email2_2_2_, instructor2_.first_name as first_na3_2_2_, instructor2_.instructor_detail_id as instruct5_2_2_, instructor2_.last_name as last_nam4_2_2_, instructor3_.id as id1_3_3_, instructor3_.hobby as hobby2_3_3_, instructor3_.youtube_channel as youtube_3_3_3_ from course_student courses0_ inner join course course1_ on courses0_.course_id=course1_.id left outer join instructor instructor2_ on course1_.instructor_id=instructor2_.id left outer join instructor_detail instructor3_ on instructor2_.instructor_detail_id=instructor3_.id where courses0_.student_id=?

        Hibernate: insert into course (instructor_id, title) values (?, ?)
        Hibernate: insert into course (instructor_id, title) values (?, ?)
        Hibernate: delete from course_student where student_id=?
        Hibernate: insert into course_student (student_id, course_id) values (?, ?)
        Hibernate: insert into course_student (student_id, course_id) values (?, ?)
        Hibernate: insert into course_student (student_id, course_id) values (?, ?)
        ```
        
    - code for adding Courses for existing student - 3rd try
        - **Use Set instead of List**
        - Since a Set will filter duplicate entries, so it doesn't need to do the deletion and re-enter the entries.
        - A much better approach!!!

        ```
        Student s1 = session.get(Student.class, 1);
        System.out.println("Student:"+s1);
        System.out.println("Courses:"+s1.getCourses());
        //create more courses
        Course c1 = new Course("Rubik's Cube");
        Course c2 = new Course("Game development");
        session.save(c1);
		session.save(c2);
		s1.add(c1);
		s1.add(c2);
        ```

        ```
        Hibernate: select student0_.id as id1_5_0_, student0_.email as email2_5_0_, student0_.first_name as first_na3_5_0_, student0_.last_name as last_nam4_5_0_ from student student0_ where student0_.id=?

        Hibernate: select courses0_.student_id as student_1_1_0_, courses0_.course_id as course_i2_1_0_, course1_.id as id1_0_1_, course1_.instructor_id as instruct3_0_1_, course1_.title as title2_0_1_, instructor2_.id as id1_2_2_, instructor2_.email as email2_2_2_, instructor2_.first_name as first_na3_2_2_, instructor2_.instructor_detail_id as instruct5_2_2_, instructor2_.last_name as last_nam4_2_2_, instructor3_.id as id1_3_3_, instructor3_.hobby as hobby2_3_3_, instructor3_.youtube_channel as youtube_3_3_3_ from course_student courses0_ inner join course course1_ on courses0_.course_id=course1_.id left outer join instructor instructor2_ on course1_.instructor_id=instructor2_.id left outer join instructor_detail instructor3_ on instructor2_.instructor_detail_id=instructor3_.id where courses0_.student_id=?

        Hibernate: insert into course (instructor_id, title) values (?, ?)
        Hibernate: insert into course (instructor_id, title) values (?, ?)
        Hibernate: insert into course_student (student_id, course_id) values (?, ?)
        Hibernate: insert into course_student (student_id, course_id) values (?, ?)
        ```

    - code for deleting a Course

        ```
        Course c1 = session.get(Course.class, 29);
		System.out.println("Course to delete:"+c1);
		session.delete(c1);
        ```

        ```
        Hibernate: select course0_.id as id1_0_0_, course0_.instructor_id as instruct3_0_0_, course0_.title as title2_0_0_, instructor1_.id as id1_2_1_, instructor1_.email as email2_2_1_, instructor1_.first_name as first_na3_2_1_, instructor1_.instructor_detail_id as instruct5_2_1_, instructor1_.last_name as last_nam4_2_1_, instructor2_.id as id1_3_2_, instructor2_.hobby as hobby2_3_2_, instructor2_.youtube_channel as youtube_3_3_2_ from course course0_ left outer join instructor instructor1_ on course0_.instructor_id=instructor1_.id left outer join instructor_detail instructor2_ on instructor1_.instructor_detail_id=instructor2_.id where course0_.id=?

        Hibernate: select reviews0_.course_id as course_i3_4_0_, reviews0_.id as id1_4_0_, reviews0_.id as id1_4_1_, reviews0_.comment as comment2_4_1_ from review reviews0_ where reviews0_.course_id=?

        Hibernate: delete from course_student where course_id=?
        Hibernate: delete from course where id=?
        ```

    - code for deleting a Student

        ```
        Student s1 = session.get(Student.class, 1);
		System.out.println("Student to delete:"+s1);
		session.delete(s1);
		
        ```

        ```
        Hibernate: select student0_.id as id1_5_0_, student0_.email as email2_5_0_, student0_.first_name as first_na3_5_0_, student0_.last_name as last_nam4_5_0_ from student student0_ where student0_.id=?

        Hibernate: delete from course_student where student_id=?
        Hibernate: delete from student where id=?
        ```
        
    - code for deleting a Student-Course entry in the join table
        - first add a remove() method in the Student entity

        ```
	    public void remove(Course course) {
            if (courses == null) {
                return;
            }
            courses.remove(course);
        }
        ```

        ```
        // get a Student
        Student s1 = session.get(Student.class, 3);
        // get a Course
        Course c1 = session.get(Course.class, 12);
        s1.remove(c1);
        ```

        ```
        Hibernate: select student0_.id as id1_5_0_, student0_.email as email2_5_0_, student0_.first_name as first_na3_5_0_, student0_.last_name as last_nam4_5_0_ from student student0_ where student0_.id=? 
        
        Hibernate: select course0_.id as id1_0_0_, course0_.instructor_id as instruct3_0_0_, course0_.title as title2_0_0_, instructor1_.id as id1_2_1_, instructor1_.email as email2_2_1_, instructor1_.first_name as first_na3_2_1_, instructor1_.instructor_detail_id as instruct5_2_1_, instructor1_.last_name as last_nam4_2_1_, instructor2_.id as id1_3_2_, instructor2_.hobby as hobby2_3_2_, instructor2_.youtube_channel as youtube_3_3_2_ from course course0_ left outer join instructor instructor1_ on course0_.instructor_id=instructor1_.id left outer join instructor_detail instructor2_ on instructor1_.instructor_detail_id=instructor2_.id where course0_.id=?
        
        Hibernate: select courses0_.student_id as student_1_1_0_, courses0_.course_id as course_i2_1_0_, course1_.id as id1_0_1_, course1_.instructor_id as instruct3_0_1_, course1_.title as title2_0_1_, instructor2_.id as id1_2_2_, instructor2_.email as email2_2_2_, instructor2_.first_name as first_na3_2_2_, instructor2_.instructor_detail_id as instruct5_2_2_, instructor2_.last_name as last_nam4_2_2_, instructor3_.id as id1_3_3_, instructor3_.hobby as hobby2_3_3_, instructor3_.youtube_channel as youtube_3_3_3_ from course_student courses0_ inner join course course1_ on courses0_.course_id=course1_.id left outer join instructor instructor2_ on course1_.instructor_id=instructor2_.id left outer join instructor_detail instructor3_ on instructor2_.instructor_detail_id=instructor3_.id where courses0_.student_id=?
 
        Hibernate: delete from course_student where student_id=? and course_id=?
        ```
 