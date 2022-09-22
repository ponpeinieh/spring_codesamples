This is Spring demo from Udemy Spring & Hibernate course. It includes hibernate demo.
- Update and Delete operations

- Update a specific object
    - retrieve the object and then just call setter methods and commit the transaction
- Bulk update objects
    - call createQuery() to create the query and then call executeUpdate() to update
    - commit the transaction

```
var sessionFactory = new Configuration().configure("hibernate.cfg.xml")
    .addAnnotatedClass(Student.class).buildSessionFactory();
// Create a Session
var session = sessionFactory.getCurrentSession();
try {
    int studentId = 1;
    session.beginTransaction();
    // update the student
    Student s = session.get(Student.class, studentId);
    System.out.println("Update student object ...");
    s.setEmail("superman@gmail.com"); 
    session.getTransaction().commit();
    //
    session = sessionFactory.getCurrentSession();
    session.beginTransaction();
    //
    System.out.println("Bulk update student objects ...");
    session.createQuery("update Student set email='foo@gmail.com'").executeUpdate();
    session.getTransaction().commit();
    System.out.println("Done!");
} finally {
    sessionFactory.close();
}
```

- Delete a specific object
    - retrieve the object and then call the session.delete() method and commit the transaction
- Bulk delete objects
    - call createQuery() to create the query and then call executeUpdate() to delete
    - commit the transaction

```
var sessionFactory = new Configuration().configure("hibernate.cfg.xml")
    .addAnnotatedClass(Student.class).buildSessionFactory();
// Create a Session
var session = sessionFactory.getCurrentSession();
try {
    int studentId=1;
    session.beginTransaction();
    //delete the student
    Student s = session.get(Student.class, studentId);
    System.out.println("Delete student object ...");
    session.delete(s);
    session.getTransaction().commit();
    
    System.out.println("Done!");
} finally {
    sessionFactory.close();
}
```

```
var sessionFactory = new Configuration().configure("hibernate.cfg.xml")
    .addAnnotatedClass(Student.class).buildSessionFactory();
// Create a Session
var session = sessionFactory.getCurrentSession();
try {
    session.beginTransaction();
    //
    System.out.println("Delete students object ...");
    session.createQuery("delete from Student where id=2").executeUpdate();
    session.getTransaction().commit();
    
    System.out.println("Done!");
} finally {
    sessionFactory.close();
}
``` 