This is Spring demo from Udemy Spring & Hibernate course.  

Spring Boot - REST CRUD API with Hibernate
- Various DAO implementation techniques
	- Version 1: Use EntityManager but **leverage native Hibernate API**
	- Version 2: Use EntityManager and standard JPA API
	- Version 3: Use EntityManager and Spring Data JPA 


- Version 2: Use EntityManager and standard JPA API
	- The JPA API methods are similar to Native Hibernate API
	- JPA also supports a query language: JPQL (JPA Query Language)
	- Comparing JPA to Native Hibernate Methods

		|Action|Native Hibernate method|JPA method|
		|Create/save new entity|session.save(...)|entityManager.persist(...)|
		|Retrieve entity by id|session.get(...) / load(...)|entityManager.find(...)|
		|Retrieve list of entities|session.createQuery(...)|entityManager.createQuery(...)|
		|Save or update entity|session.saveOrUpdate(...)|entityManager.merge(...)|
		|Delete entity|session.delete(...)|entityManager.remove(...)|

		- persist() is similar to save (with transaction) and it adds the entity object to the persistent context, so any further changes are tracked. If the object properties are changed before the transaction is committed or session is flushed, it will also be saved into database. Second difference is that we can use persist() method only within the boundary of a transaction, so it’s safe and takes care of any cascaded objects. Finally, persist **doesn’t return anything** so we need to use the persisted object to get the generated identifier value. 

		- Note the specialty about merge(): Hibernate merge can be used to update existing values, however this method **create a copy from the passed entity object and return it**. **The returned object is part of persistent context and tracked for any changes, passed object is not tracked**. This is the major difference with merge() from all other methods. 

- DAO

```
package com.example.springboot.cruddemo.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.example.springboot.cruddemo.entity.Employee;

@Repository
public class EmployeeDAOJpaImpl implements EmployeeDAO {

	// define entityManager
	private EntityManager entityManager;

	// setup constructor injection
	@Autowired
	public EmployeeDAOJpaImpl(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	public List<Employee> findAll() {
		// create a query
		TypedQuery<Employee> query = entityManager.createQuery("from Employee", Employee.class);
		// Query query = entityManager.createQuery("from Employee");
		List<Employee> result = query.getResultList();
		return result;
	}

	@Override
	public void save(Employee e) {
		Employee e2 = entityManager.merge(e); //merge() returns an new Employee object with id set
		e.setId(e2.getId());
	}

	@Override
	public Employee findById(int id) {
		// create a query
		Employee employee = entityManager.find(Employee.class, id);
		return employee;
	}

	@Override
	public void deleteById(int id) {
		// create a query
		Query query = entityManager.createQuery("delete from Employee where id=:id");
		query.setParameter("id", id);
		query.executeUpdate();
	}

}

```








