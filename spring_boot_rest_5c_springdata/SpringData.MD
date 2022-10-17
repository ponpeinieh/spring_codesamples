Spring Data JPA 
- Spring Data JPA provides repository support for the Java Persistence API (JPA). It eases development of applications that need to access JPA data sources.
- Dependencies
	- Due to the different inception dates of individual Spring Data modules, most of them carry different major and minor version numbers. The easiest way to find compatible ones is to rely on the **Spring Data Release Train BOM** that we ship with the compatible versions defined. 
		- In a Maven project, you would declare this dependency in the <dependencyManagement /> section of your POM as follows:

		```
		<dependencyManagement>
			<dependencies>
				<dependency>
				<groupId>org.springframework.data</groupId>
				<artifactId>spring-data-bom</artifactId>
				<version>2021.2.5</version>
				<scope>import</scope>
				<type>pom</type>
				</dependency>
			</dependencies>
		</dependencyManagement>
		```

		- With that in place, you can declare the Spring Data modules you would like to use without a version in the <dependencies /> block, as follows:

		```
		<dependencies>
			<dependency>
				<groupId>org.springframework.data</groupId>
				<artifactId>spring-data-jpa</artifactId>
			</dependency>
		<dependencies>
		```
		- Dependency Management with Spring Boot: Spring Boot selects a recent version of Spring Data modules for you.
- Core concepts
	- The central interface in the Spring Data repository abstraction is `Repository`. 
	- It takes the domain class to manage as well as the ID type of the domain class as type arguments. 
	- The `CrudRepository` interface provides sophisticated CRUD functionality for the entity class that is being managed.

	```
	public interface CrudRepository<T, ID> extends Repository<T, ID> {
		<S extends T> S save(S entity);      
		Optional<T> findById(ID primaryKey); 
		Iterable<T> findAll();               
		long count();                        
		void delete(T entity);               
		boolean existsById(ID primaryKey);   
		// … more functionality omitted.
	}
	```

	- We also provide persistence technology-specific abstractions, such as `JpaRepository` or `MongoRepository`. Those interfaces extend CrudRepository and expose the capabilities of the underlying persistence technology in addition to the rather generic persistence technology-agnostic interfaces such as CrudRepository.
	- `PagingAndSortingRepository` abstraction that adds additional methods to ease paginated access to entities

	```
	public interface PagingAndSortingRepository<T, ID> extends CrudRepository<T, ID> {
		Iterable<T> findAll(Sort sort);
		Page<T> findAll(Pageable pageable);
	}
	```
	- In addition to query methods, query derivation for both count and delete queries is available. 
		- long countByLastname(String lastname);
		- long deleteByLastname(String lastname);
		- List<User> removeByLastname(String lastname);
- Query Methods: 4-steps process
	1. Declare an interface extending Repository or one of its subinterfaces and type it to the domain class and ID type that it should handle, as shown in the following example:
		- interface PersonRepository extends Repository<Person, Long> { … }
	2. Declare query methods on the interface.
		- List<Person> findByLastname(String lastname);
	3. Set up Spring to create proxy instances for those interfaces, either with JavaConfig or with XML configuration.
		- To use Java configuration, create a class similar to the following:

		```
		import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

		@EnableJpaRepositories
		class Config { … }
		```
	4. Inject the repository instance and use it, as shown in the following example:

	```
	class SomeClient {

		private final PersonRepository repository;

		SomeClient(PersonRepository repository) {
			this.repository = repository;
		}

		void doSomething() {
			List<Person> persons = repository.findByLastname("Matthews");
		}
	}
	```
- Query Creation:
	- Examples:

	```
	interface PersonRepository extends Repository<Person, Long> {

		List<Person> findByEmailAddressAndLastname(EmailAddress emailAddress, String lastname);

		// Enables the distinct flag for the query
		List<Person> findDistinctPeopleByLastnameOrFirstname(String lastname, String firstname);
		List<Person> findPeopleDistinctByLastnameOrFirstname(String lastname, String firstname);

		// Enabling ignoring case for an individual property
		List<Person> findByLastnameIgnoreCase(String lastname);
		// Enabling ignoring case for all suitable properties
		List<Person> findByLastnameAndFirstnameAllIgnoreCase(String lastname, String firstname);

		// Enabling static ORDER BY for a query
		List<Person> findByLastnameOrderByFirstnameAsc(String lastname);
		List<Person> findByLastnameOrderByFirstnameDesc(String lastname);
	}
	```
	- Special parameter handling
		- To handle parameters in your query, define method parameters as already seen in the preceding examples. 
		- Besides that, the infrastructure recognizes certain specific types like `Pageable` and `Sort`, to apply pagination and sorting to your queries dynamically. 
			- APIs taking Sort and Pageable expect non-null values to be handed into methods. If you do not want to apply any sorting or pagination, use `Sort.unsorted()`and `Pageable.unpaged()`.

		```
		Page<User> findByLastname(String lastname, Pageable pageable);
		Slice<User> findByLastname(String lastname, Pageable pageable);
		List<User> findByLastname(String lastname, Sort sort);
		List<User> findByLastname(String lastname, Pageable pageable);
		```

		- The first method lets you pass an `org.springframework.data.domain.Pageable` instance to the query method to dynamically add paging to your statically defined query. 
		- A Page knows about the total number of elements and pages available.
		- It does so by the infrastructure **triggering a count query to calculate the overall number**. 
		- As this might be expensive (depending on the store used), you can instead return a `Slice`. 
			- A Slice knows only about **whether a next Slice is available**, which might be sufficient when walking through a larger result set.
		- Sorting options are handled through the Pageable instance, too. 
		- If you need only sorting, add an `org.springframework.data.domain.Sort` parameter to your method. 
		- Returning a List is also possible. 
			- The additional metadata required to build the actual Page instance is not created (which, in turn, means that the additional count query that would have been necessary is not issued). 
			- Rather, it restricts the query to look up only the given range of entities.
	- Paging and Sorting
		- You can define simple sorting expressions by using property names. You can concatenate expressions to collect multiple criteria into one expression.

		```
		Sort sort = Sort.by("firstname").ascending()
  			.and(Sort.by("lastname").descending());
		```

		- or Defining sort expressions by using the type-safe API

		```
		TypedSort<Person> person = Sort.sort(Person.class);
		Sort sort = person.by(Person::getFirstname).ascending()
			.and(person.by(Person::getLastname).descending());

		```
	- Limiting Query Results
		- You can limit the results of query methods by using the `first` or `top` keywords
		- You can append an optional numeric value to top or first to specify the maximum result size to be returned. 

		```
		User findFirstByOrderByLastnameAsc();
		User findTopByOrderByAgeDesc();
		Page<User> queryFirst10ByLastname(String lastname, Pageable pageable);
		Slice<User> findTop3ByLastname(String lastname, Pageable pageable);
		List<User> findFirst10ByLastname(String lastname, Sort sort);
		List<User> findTop10ByLastname(String lastname, Pageable pageable);
		```
	- Repository Methods Returning Collections or Iterables
		- Query methods that return multiple results can use standard Java Iterable, List, and Set. 
		- Beyond that, we support returning **Spring Data’s `Streamable`**, a custom extension of Iterable, as well as collection types provided by `Vavr`.
	- Using `Streamable` as Query Method Return Type
		- You can use Streamable as alternative to Iterable or any collection type. 
		- It provides convenience methods to access a non-parallel Stream (missing from Iterable) and the ability to **directly ….filter(…) and ….map(…) over the elements and concatenate the Streamable to others:**
		- Example: Using Streamable to combine query method results

		```
		interface PersonRepository extends Repository<Person, Long> {
			Streamable<Person> findByFirstnameContaining(String firstname);
			Streamable<Person> findByLastnameContaining(String lastname);
		}

		Streamable<Person> result = repository.findByFirstnameContaining("av")
			.and(repository.findByLastnameContaining("ea"));
		```

	- Returning Custom Streamable Wrapper Types
		- Providing dedicated wrapper types for collections is a commonly used pattern to provide an API for a query result that **returns multiple elements.** 
		- Usually, these types are used by invoking a repository method returning a collection-like type and creating an instance of the wrapper type manually. 
		- You can avoid that additional step as Spring Data lets you use these wrapper types as query method return types if they meet the following criteria:
			- The type implements Streamable.
			- The type exposes either a constructor or a static factory method named of(…) or valueOf(…) that takes Streamable as an argument.
			- Example:
				- A Product entity that exposes API to access the product’s price.
				- A wrapper type for a Streamable<Product> that can be constructed by using `Products.of(…)` (factory method created with the `Lombok annotation`).
					- A standard constructor taking the Streamable<Product> will do as well.
				- The wrapper type exposes an additional API, calculating new values on the Streamable<Product>.
				- Implement the Streamable interface and delegate to the actual result. (ie. the iterator())
				- That wrapper type `Products` can be used directly as a query method return type.
					- You do not need to return Streamable<Product> and manually wrap it after the query in the repository client.

			```
			class Product {                                         
				MonetaryAmount getPrice() { … }
			}

			@RequiredArgsConstructor(staticName = "of")
			class Products implements Streamable<Product> {      //Wrapper type implementing Streamable   

				private final Streamable<Product> streamable;

				public MonetaryAmount getTotal() {                    
					return streamable.stream()
					.map(Priced::getPrice)
					.reduce(Money.of(0), MonetaryAmount::add);
				}


				@Override
				public Iterator<Product> iterator() {                 
					return streamable.iterator();
				}
			}

			interface ProductRepository implements Repository<Product, Long> {
				Products findAllByDescriptionContaining(String text); //return the Wrapper type
			}
			```
- Note: 
	- Repository CRUD methods that return an individual aggregate instance use Java 8’s `Optional` to indicate the potential absence of a value.
	- Repository methods returning collections, collection alternatives, wrappers, and streams are guaranteed **never to return null** but rather the corresponding empty representation.
	-  Streaming Query Results
		- You can process the results of query methods incrementally by using a Java 8 Stream<T> as the return type. 
		- Instead of wrapping the query results in a Stream, data store-specific methods are used to perform the streaming

		```
		@Query("select u from User u")
		Stream<User> findAllByCustomQueryAndStream();

		Stream<User> readAllByFirstnameNotNull();

		@Query("select u from User u")
		Stream<User> streamAllPaged(Pageable pageable);
		```

		- A Stream potentially wraps underlying data store-specific resources and must, therefore, be closed after usage.
		- You can either manually close the Stream by using the close() method or by using a Java 7 try-with-resources block 

