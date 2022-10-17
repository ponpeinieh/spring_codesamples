Spring Data JPA 
- Create Queries
	- Reference: https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods
	- Distinct
		- findDistinctByLastnameAndFirstname
		- select distinct …​ where x.lastname = ?1 and x.firstname = ?2
	- And
		- findByLastnameAndFirstname
		- … where x.lastname = ?1 and x.firstname = ?2
	- Or
		- findByLastnameOrFirstname
		- … where x.lastname = ?1 or x.firstname = ?2
	- Is, Equals
		- findByFirstname,findByFirstnameIs,findByFirstnameEquals
		- … where x.firstname = ?1
	- Between
		- findByStartDateBetween
		- … where x.startDate between ?1 and ?2
	- LessThan/LessThanEqual/GreaterThan/GreaterThanEqual
		- findByAgeLessThan
		- … where x.age < ?1 
	- After/Before
		- findByStartDateAfter
		- … where x.startDate > ?1
	- IsNull, Null/IsNotNull, NotNull
		- findByAge(Is)Null
		- … where x.age is null
	- Like/NotLike
		- findByFirstnameLike
		- … where x.firstname like ?1
	- StartingWith
		- findByFirstnameStartingWith
		- … where x.firstname like ?1 (parameter bound with appended %)
	- EndingWith
		- findByFirstnameEndingWith
		- … where x.firstname like ?1 (parameter bound with prepended %)
	- Containing
		- findByFirstnameContaining
		- … where x.firstname like ?1 (parameter bound wrapped in %)
	- OrderBy
		- findByAgeOrderByLastnameDesc
		- … where x.age = ?1 order by x.lastname desc
	- Not
		- findByLastnameNot
		- … where x.lastname <> ?1
	- In/NotIn
		- findByAgeIn(Collection<Age> ages)
		- … where x.age in ?1
	- True/False
		- findByActiveTrue()
		- … where x.active = true
	- IgnoreCase
		- findByFirstnameIgnoreCase
		- … where UPPER(x.firstname) = UPPER(?1)

- Using @Query
	- Example

	```
	public interface UserRepository extends JpaRepository<User, Long> {

		@Query("select u from User u where u.emailAddress = ?1")
		User findByEmailAddress(String emailAddress);
	}
	```

	- Native Queries
		- The @Query annotation allows for running native queries by setting the **nativeQuery** flag to true

		```
		public interface UserRepository extends JpaRepository<User, Long> {

			@Query(value = "SELECT * FROM USERS WHERE EMAIL_ADDRESS = ?1", nativeQuery = true)
			User findByEmailAddress(String emailAddress);
		}
		```

		- Spring Data JPA does not currently support dynamic sorting for native queries
		- But it supports pagination
			- add the `countQuery`

		```
		public interface UserRepository extends JpaRepository<User, Long> {

			@Query(value = "SELECT * FROM USERS WHERE LASTNAME = ?1",
				countQuery = "SELECT count(*) FROM USERS WHERE LASTNAME = ?1",
				nativeQuery = true)
			Page<User> findByLastname(String lastname, Pageable pageable);
		}
		```
	- @Query with sort
		- By default, Spring Data JPA rejects any Order instance containing function calls, but you can use JpaSort.unsafe to add potentially unsafe ordering.

	```
	public interface UserRepository extends JpaRepository<User, Long> {

		@Query("select u from User u where u.lastname like ?1%")
		List<User> findByAndSort(String lastname, Sort sort);

		@Query("select u.id, LENGTH(u.firstname) as fn_len from User u where u.lastname like ?1%")
		List<Object[]> findByAsArrayAndSort(String lastname, Sort sort);
	}


	repo.findByAndSort("lannister", Sort.by("firstname"));                
	repo.findByAndSort("stark", Sort.by("LENGTH(firstname)")); //not allowed, invalid            
	repo.findByAndSort("targaryen", JpaSort.unsafe("LENGTH(firstname)")); //ok
	repo.findByAsArrayAndSort("bolton", Sort.by("fn_len"));      
	```
	
	- Using Named Parameters
		- By default, Spring Data JPA uses position-based parameter binding, 
		- You can use `@Param` annotation to give a method parameter a concrete name and bind the name in the query 

		```
		public interface UserRepository extends JpaRepository<User, Long> {

			@Query("select u from User u where u.firstname = :firstname or u.lastname = :lastname")
			User findByLastnameOrFirstname(@Param("lastname") String lastname,
										@Param("firstname") String firstname);
		}
		```

	- Modifying Queries
		- The `@Modifying` annotation is used to enhance the @Query annotation so that we can execute not only SELECT queries, but also INSERT, UPDATE, DELETE, and even DDL queries.

		```		
		@Modifying
		@Query("update User u set u.firstname = ?1 where u.lastname = ?2")
		int setFixedFirstnameFor(String firstname, String lastname);
		```
		- This method returns an integer. It's a feature of Spring Data JPA @Modifying queries that provides us with the number of updated entities.

		- Note that **executing a delete query with @Query works differently from Spring Data JPA's deleteBy name-derived query methods.** The latter first fetches the entities from the database, and then **deletes them one by one.** This means that the life-cycle method `@PreRemove` will be called on those entities. However, with the former, a single query is executed against the database.

	- Using modifying queries leaves the **underlying persistence context outdated.**
		- If our modifying query changes entities contained in the persistence context, then this context becomes outdated. 
		- One way to manage this situation is to clear the persistence context. 
			- By doing that, we make sure that the persistence context will fetch the entities from the database next time.
			- However, we don't have to explicitly call the clear() method on the EntityManager. We can just use the `clearAutomatically` property from the @Modifying annotation:
				- @Modifying(clearAutomatically = true)
				- This way, we make sure that the persistence context is cleared after our query execution.
		- However, if our persistence context contained unflushed changes, clearing it would mean dropping the unsaved changes. Fortunately, there's another property of the annotation we can use in this case, `flushAutomatically`:
			- @Modifying(flushAutomatically = true)
			- Now the EntityManager is flushed before our query is executed.

	- Entity Graphs
		- In Spring Data JPA, we can define an entity graph using a combination of `@NamedEntityGraph` and `@EntityGraph` annotations. 
		- Or, we can also define **ad-hoc entity graphs** with just the `attributePaths` argument of the `@EntityGraph` annotation.
		- With @NamedEntityGraph
			- We can use JPA's `@NamedEntityGraph` annotation directly on our Item entity:

			```
			@Entity
			@NamedEntityGraph(name = "Item.characteristics",
				attributeNodes = @NamedAttributeNode("characteristics")
			)
			public class Item {
				//...
			}
			```

			- We attach the `@EntityGraph` annotation to one of our repository methods:

			```
			public interface ItemRepository extends JpaRepository<Item, Long> {

				@EntityGraph(value = "Item.characteristics")
				Item findByName(String name);
			}
			```
			- The default value of the type argument of the @EntityGraph annotation is `EntityGraphType.FETCH`. 
			- So the characteristics property will be loaded eagerly, even though the default fetch strategy of the @OneToMany annotation is lazy.
			- One catch here is that if the defined fetch strategy is EAGER, then we cannot change its behavior to LAZY. 
			
		-  Without @NamedEntityGraph
			- Or, we can define an ad-hoc entity graph with `attributePaths`.

			```
			public interface CharacteristicsRepository 
			extends JpaRepository<Characteristic, Long> {
				
				@EntityGraph(attributePaths = {"item"})
				Characteristic findByType(String type);    
			}
			```

			- This will load the item property of the Characteristic entity eagerly, even though our entity declares a lazy-loading strategy for this property.
			- This is handy since we can define the entity graph inline instead of referring to an existing named entity graph.
	- Enum EntityGraph.EntityGraphType
		- FETCH
			- When the javax.persistence.fetchgraph property is used to specify an entity graph, attributes that are specified by attribute nodes of the entity graph are treated as FetchType.EAGER and attributes that are not specified are treated as FetchType.LAZY
		- LOAD
			- When the javax.persistence.loadgraph property is used to specify an entity graph, attributes that are specified by attribute nodes of the entity graph are treated as FetchType.EAGER and attributes that are not specified are treated according to their specified or default FetchType.


