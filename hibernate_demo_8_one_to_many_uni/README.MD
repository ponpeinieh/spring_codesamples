This is Spring demo from Udemy Spring & Hibernate course. It includes hibernate demo. 
- One-To-Many mapping with uni-directional relationship
    - A Course entity has many Reviews entities
    - Course entity:
        - Use @OneToMany and @JoinColumn
        - Here @JoinColumn annotation specifies the foreign key column name of the many side (ie. Review entity)
        - The default fetch type is LAZY for one-to-many relationship, so we can omit that attribute
        - **The cascase type ALL is used, but for one-to-many, it is not efficient for deletion, especially when there are lots of reviews.**

        ```
        @OneToMany(fetch=FetchType.LAZY, cascade=CascadeType.ALL)
        @JoinColumn(name="course_id")
        private List<Review> reviews;
        ```

        - add an utility method for adding a review to the course

        ```
        public void addReview(Review r) {
            if (reviews == null)
                reviews = new ArrayList<Review>();
            reviews.add(r);
        }
        ```
        
        - Review entity

        ```
        import javax.persistence.Column;
        import javax.persistence.Entity;
        import javax.persistence.GeneratedValue;
        import javax.persistence.GenerationType;
        import javax.persistence.Id;
        import javax.persistence.Table;

        @Entity
        @Table(name = "review")
        public class Review {
            @Id
            @Column(name = "id")
            @GeneratedValue(strategy = GenerationType.IDENTITY)
            private int id;
            
            @Column(name = "comment")
            private String comment;

            public Review(String comment) {
                super();
                this.comment = comment;
            }

            public Review() {
            }

            public String getComment() {
                return comment;
            }

            public void setComment(String comment) {
                this.comment = comment;
            }

            public int getId() {
                return id;
            }

            @Override
            public String toString() {
                return "Review [id=" + id + ", comment=" + comment + "]";
            }

        }
        ```

        - Create Course and Reviews
            - Since CasecadeType.ALL is used, we only need to save the Course entity.

            ```
            // Create a SessionFactory
            var sessionFactory = new Configuration().configure("hibernate.cfg.xml")
                    .addAnnotatedClass(Instructor.class)
                    .addAnnotatedClass(InstructorDetail.class)
                    .addAnnotatedClass(Course.class)
                    .addAnnotatedClass(Review.class).buildSessionFactory();
            // Create a Session
            var session = sessionFactory.getCurrentSession();
            // create a Instructor object
            try { 
                session.beginTransaction();
                //create a course
                Course c = new Course("How to bake bread");
                //add some reviews
                c.addReview(new Review("Excellent course! Well done!"));
                c.addReview(new Review("Will recommend to others!"));
                c.addReview(new Review("Love the course! Thump up!"));
                session.save(c);
                session.getTransaction().commit();
                System.out.println("Done!");
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                session.close();
                sessionFactory.close();
            }
            ```

        - Note that if forgot to add the annotated class, exception throwed:

            ```
            Exception in thread "main" org.hibernate.AnnotationException: Use of @OneToMany or @ManyToMany targeting an unmapped class: com.example.hibernate.demo.entity.Course.reviews[com.example.hibernate.demo.entity.Review]

            ```

        - Success log message

            ```
            Hibernate: insert into course (instructor_id, title) values (?, ?)
            TRACE BasicBinder - binding parameter [1] as [INTEGER] - [null]
            TRACE BasicBinder - binding parameter [2] as [VARCHAR] - [How to fry a streak]

            Hibernate: insert into review (comment) values (?)
            TRACE BasicBinder - binding parameter [1] as [VARCHAR] - [Excellent course! Well done!]
            
            Hibernate: update review set course_id=? where id=?
            TRACE BasicBinder - binding parameter [1] as [INTEGER] - [12]
            TRACE BasicBinder - binding parameter [2] as [INTEGER] - [4]
            ```

        - Get Course and Reviews
            - Since FetchType.LAZY(the default fetch type for one-to-many) is used, a separate query is issued for getting Review entities

            ```
            // Create a SessionFactory
            var sessionFactory = new Configuration().configure("hibernate.cfg.xml")
                    .addAnnotatedClass(Instructor.class)
                    .addAnnotatedClass(InstructorDetail.class)
                    .addAnnotatedClass(Course.class)
                    .addAnnotatedClass(Review.class).buildSessionFactory();
            // Create a Session
            var session = sessionFactory.getCurrentSession();
            // create a Instructor object
            try { 
                session.beginTransaction();
                //get the course
                int id = 10;
                Course c = session.get(Course.class, id);
                System.out.println("Course:"+c);
                //get the reviews
                System.out.println("Reviews:"+c.getReviews());
                //	
                session.getTransaction().commit();
                System.out.println("Done!");
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                session.close();
                sessionFactory.close();
            }
            ```

        - log messages
            - note that for many-to-one relationship, the default fetch type is EAGER,  so the Instructor is fetched eagerly.
            - note that for one-to-one relationship, the default fetch type is EAGER,  so the InstructorDetail is fetched eagerly along with Instructor.
            - note that for one-to-many relationship, the default fetch type is LAZY,  so the Review is fetched separately.

            ```
            Hibernate: select course0_.id as id1_0_0_, course0_.instructor_id as instruct3_0_0_, course0_.title as title2_0_0_, instructor1_.id as id1_1_1_, instructor1_.email as email2_1_1_, instructor1_.first_name as first_na3_1_1_, instructor1_.instructor_detail_id as instruct5_1_1_, instructor1_.last_name as last_nam4_1_1_, instructor2_.id as id1_2_2_, instructor2_.hobby as hobby2_2_2_, instructor2_.youtube_channel as youtube_3_2_2_ from course course0_ left outer join instructor instructor1_ on course0_.instructor_id=instructor1_.id left outer join instructor_detail instructor2_ on instructor1_.instructor_detail_id=instructor2_.id where course0_.id=?

            Hibernate: select reviews0_.course_id as course_i3_3_0_, reviews0_.id as id1_3_0_, reviews0_.id as id1_3_1_, reviews0_.comment as comment2_3_1_ from review reviews0_ where reviews0_.course_id=?
            ```

        - Delete Course and Reviews

            ```
            // Create a SessionFactory
            var sessionFactory = new Configuration().configure("hibernate.cfg.xml")
                    .addAnnotatedClass(Instructor.class)
                    .addAnnotatedClass(InstructorDetail.class)
                    .addAnnotatedClass(Course.class)
                    .addAnnotatedClass(Review.class).buildSessionFactory();
            // Create a Session
            var session = sessionFactory.getCurrentSession();
            // create a Instructor object
            try { 
                session.beginTransaction();
                //get the course
                int id = 12;
                Course c = session.get(Course.class, id);
                session.delete(c);
                //	
                session.getTransaction().commit();
                System.out.println("Done!");
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                session.close();
                sessionFactory.close();
            } 
            ```

        - log messages: **important!!!**
            - First it retrieves Course entity(along with Instructor and InstructorDetail, since fetch type is EAGER)
            - Then it retrieves the Review entities for the Course entity.
            - Then it update the Review entities with their Course references set to null 
            - Then it runs a loop to delete each Review entity.
            - Finally it deletes the Course entity.
            - See reference on its drawbacks: https://thorben-janssen.com/avoid-cascadetype-delete-many-assocations/

        ```
        Hibernate: select course0_.id as id1_0_0_, course0_.instructor_id as instruct3_0_0_, course0_.title as title2_0_0_, instructor1_.id as id1_1_1_, instructor1_.email as email2_1_1_, instructor1_.first_name as first_na3_1_1_, instructor1_.instructor_detail_id as instruct5_1_1_, instructor1_.last_name as last_nam4_1_1_, instructor2_.id as id1_2_2_, instructor2_.hobby as hobby2_2_2_, instructor2_.youtube_channel as youtube_3_2_2_ from course course0_ left outer join instructor instructor1_ on course0_.instructor_id=instructor1_.id left outer join instructor_detail instructor2_ on instructor1_.instructor_detail_id=instructor2_.id where course0_.id=?

        Hibernate: select reviews0_.course_id as course_i3_3_0_, reviews0_.id as id1_3_0_, reviews0_.id as id1_3_1_, reviews0_.comment as comment2_3_1_ from review reviews0_ where reviews0_.course_id=?
        TRACE BasicBinder - binding parameter [1] as [INTEGER] - [13]
        TRACE BasicExtractor - extracted value ([id1_3_1_] : [INTEGER]) - [7]
        TRACE BasicExtractor - extracted value ([comment2_3_1_] : [VARCHAR]) - [Excellent course! Well done!]
        TRACE BasicExtractor - extracted value ([course_i3_3_0_] : [INTEGER]) - [13]
        TRACE BasicExtractor - extracted value ([id1_3_0_] : [INTEGER]) - [7]
        TRACE BasicExtractor - extracted value ([id1_3_1_] : [INTEGER]) - [8]
        TRACE BasicExtractor - extracted value ([comment2_3_1_] : [VARCHAR]) - [Will recommend to others!]
        TRACE BasicExtractor - extracted value ([course_i3_3_0_] : [INTEGER]) - [13]
        TRACE BasicExtractor - extracted value ([id1_3_0_] : [INTEGER]) - [8]
        TRACE BasicExtractor - extracted value ([id1_3_1_] : [INTEGER]) - [9]
        TRACE BasicExtractor - extracted value ([comment2_3_1_] : [VARCHAR]) - [Love the course! Thump up!]
        TRACE BasicExtractor - extracted value ([course_i3_3_0_] : [INTEGER]) - [13]
        TRACE BasicExtractor - extracted value ([id1_3_0_] : [INTEGER]) - [9]
        DEBUG SQL - update review set course_id=null where course_id=?
        Hibernate: update review set course_id=null where course_id=?
        TRACE BasicBinder - binding parameter [1] as [INTEGER] - [13]
        DEBUG SQL - delete from review where id=?
        Hibernate: delete from review where id=?
        TRACE BasicBinder - binding parameter [1] as [INTEGER] - [7]
        DEBUG SQL - delete from review where id=?
        Hibernate: delete from review where id=?
        TRACE BasicBinder - binding parameter [1] as [INTEGER] - [8]
        DEBUG SQL - delete from review where id=?
        Hibernate: delete from review where id=?
        TRACE BasicBinder - binding parameter [1] as [INTEGER] - [9]
        DEBUG SQL - delete from course where id=?
        Hibernate: delete from course where id=?
        TRACE BasicBinder - binding parameter [1] as [INTEGER] - [13]

        ```

