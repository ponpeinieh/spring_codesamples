This is Spring demo from Udemy Spring & Hibernate course. It includes hibernate demo. 

- This project is rewriting the previous many-to-many mapping (bi-directional) project code
- Basically, it uses the `mappedBy` attribute in @ManyToMany in the Student entity (ie. the Student entity is the referencing side, the owning side is the Course entity )

    ```
   	@ManyToMany(mappedBy="students")
	private Set<Course> courses;
    ```

- This is the standard way to specify bi-directional many-to-many relationship, instead of using two uni-directional annotations (as previous project). But we need to **update both ends of a bidirectional association when you want to add or remove an entity.** , ie. rewrite the following add(), remove() methods for both Course and Student entity classes.
    - also make sure the getStudents() doesn't return null object, similarly for getCourses()

    ```
    // add the convenience method for bi-directional relationship
	public void add(Course course) {
		if (courses == null) {
			courses = new HashSet<>();
		}
		courses.add(course);
		course.getStudents().add(this); //important
	}
	
	public void remove(Course course) {
		if (courses == null) {
			return;
		}
		courses.remove(course);
		course.getStudents().remove(this); //important
	}
    ```
    - Deleting the entity at `the owning side` is easy.
        -  Just call the session.delete(), it will automatically delete the join entries first.

        ```
        // get a Course
        Course c1 = session.get(Course.class, 40);
        System.out.println("Course to delete:"+c1);
        session.delete(c1);
        ```

    - But when deleting the entity at `the referencing side` (eg. the Student entity) is problematic, we need to
        - manually delete the join table entries before we delete the entity
            - Very inefficient, because it issues a separate delete statement for each join entry.
        - to avoid the ConcurrentModificationException, we need to make a copy of the Set object.

        ```
        Student s1 = session.get(Student.class, 7);
        System.out.println("Student to delete:"+s1);
        Set<Course> copySet = new HashSet<>(s1.getCourses()); //make a copy of the Set object
        for(var c : copySet) {
            s1.remove(c); //This way, we avoid the ConcurrentModificationException, but very inefficient, because it issues a separate delte statement for each join entry.
        }
        session.delete(s1);
        ```

        ```
        Hibernate: select student0_.id as id1_5_0_, student0_.email as email2_5_0_, student0_.first_name as first_na3_5_0_, student0_.last_name as last_nam4_5_0_ from student student0_ where student0_.id=?

        Hibernate: select courses0_.student_id as student_2_1_0_, courses0_.course_id as course_i1_1_0_, course1_.id as id1_0_1_, course1_.instructor_id as instruct3_0_1_, course1_.title as title2_0_1_, instructor2_.id as id1_2_2_, instructor2_.email as email2_2_2_, instructor2_.first_name as first_na3_2_2_, instructor2_.instructor_detail_id as instruct5_2_2_, instructor2_.last_name as last_nam4_2_2_, instructor3_.id as id1_3_3_, instructor3_.hobby as hobby2_3_3_, instructor3_.youtube_channel as youtube_3_3_3_ from course_student courses0_ inner join course course1_ on courses0_.course_id=course1_.id left outer join instructor instructor2_ on course1_.instructor_id=instructor2_.id left outer join instructor_detail instructor3_ on instructor2_.instructor_detail_id=instructor3_.id where courses0_.student_id=?

        Hibernate: select students0_.course_id as course_i1_1_0_, students0_.student_id as student_2_1_0_, student1_.id as id1_5_1_, student1_.email as email2_5_1_, student1_.first_name as first_na3_5_1_, student1_.last_name as last_nam4_5_1_ from course_student students0_ inner join student student1_ on students0_.student_id=student1_.id where students0_.course_id=?

        Hibernate: delete from course_student where course_id=? and student_id=?

        Hibernate: delete from student where id=?
        ```


- In summary,  using two uni-directional annotations (as previous project) is much easier!!!!