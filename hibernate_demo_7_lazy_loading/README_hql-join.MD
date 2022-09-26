- HQL and JPQL
	- The Hibernate Query Language (HQL) and Java Persistence Query Language (JPQL) are both object model focused query languages similar in nature to SQL. 
	- JPQL is a heavily-inspired-by subset of HQL. A JPQL query is always a valid HQL query, the reverse is not true however.
	- Both HQL and JPQL are non-type-safe ways to perform query operations. Criteria queries offer a type-safe approach to querying. 

- What’s the Difference between JOIN, LEFT JOIN and JOIN FETCH ?
	- Reference: https://thorben-janssen.com/hibernate-tips-difference-join-left-join-fetch-join/
	- In JPQL, you can define a `JOIN` statement based on a specified association between 2 entities. Your persistence provider, e.g., Hibernate, translates this into an SQL JOIN statement.
		- Example : a JPQL query that returns all Author entities who’ve written a Book which title contains the word “Hibernate”. It joins the Author entity with the Book entity and uses the title attribute of the Book in the WHERE clause.
		- List<Author> authors = em.createQuery("SELECT a FROM Author a JOIN a.books b WHERE b.title LIKE '%Hibernate%'", Author.class).getResultList();
		-  It uses the defined many-to-many association to join the Author table with the association table BookAuthor. It then joins the association table with the Book table.
			```
				select
					author0_.id as id1_0_,
					author0_.firstName as firstNam2_0_,
					author0_.lastName as lastName3_0_,
					author0_.version as version4_0_ 
				from
					Author author0_ 
				inner join
					BookAuthor books1_ 
						on author0_.id=books1_.authorId 
				inner join
					Book book2_ 
						on books1_.bookId=book2_.id 
				where
					book2_.title like '%Hibernate%'
			```
	- The `LEFT JOIN` statement is similar to the JOIN statement. The main difference is that a LEFT JOIN statement includes all rows of the entity or table referenced on the left side of the statement.
		- Example: select all Authors with the lastName “Janssen” and their Books. If the database contains a Book for a specific Author, the query returns it as the second element in the Object[]. Otherwise, that array element is null.
		- A simple JOIN statement would only return the Authors who have written a Book. The second element of the Object[] would never be null.
		- List<Object[]> authors = em.createQuery("SELECT a, b FROM Author a LEFT JOIN a.books b WHERE a.lastName = 'Janssen'").getResultList();
			```
				select
					author0_.id as id1_0_0_,
					book2_.id as id1_1_1_,
					author0_.firstName as firstNam2_0_0_,
					author0_.lastName as lastName3_0_0_,
					author0_.version as version4_0_0_,
					book2_.publisherid as publishe5_1_1_,
					book2_.publishingDate as publishi2_1_1_,
					book2_.title as title3_1_1_,
					book2_.version as version4_1_1_ 
				from
					Author author0_ 
				left outer join
					BookAuthor books1_ 
						on author0_.id=books1_.authorId 
				left outer join
					Book book2_ 
						on books1_.bookId=book2_.id 
				where
					author0_.lastName='Janssen'
			```
	- The FETCH keyword of the `JOIN FETCH` statement is JPA-specific. 
		- It tells the persistence provider to not only join the 2 database tables within the query but to also initialize the association on the returned entity. You can use it with a JOIN and a LEFT JOIN statement.
		- List<Author> authors = em.createQuery("SELECT a FROM Author a JOIN FETCH a.books b WHERE b.title LIKE '%Hibernate%'", Author.class).getResultList();
		- The JPQL query selects Author entities. But as you can see in the SQL statement, Hibernate now selects all columns mapped by the Author and the Book entity. Hibernate then maps the result set to Author and Book entities. It uses the Book entities to initialize the books attribute of each Author entity before it returns a List of Author entities.
			```
				select
					author0_.id as id1_0_0_,
					book2_.id as id1_1_1_,
					author0_.firstName as firstNam2_0_0_,
					author0_.lastName as lastName3_0_0_,
					author0_.version as version4_0_0_,
					book2_.publisherid as publishe5_1_1_,
					book2_.publishingDate as publishi2_1_1_,
					book2_.title as title3_1_1_,
					book2_.version as version4_1_1_,
					books1_.authorId as authorId2_2_0__,
					books1_.bookId as bookId1_2_0__ 
				from
					Author author0_ 
				inner join
					BookAuthor books1_ 
						on author0_.id=books1_.authorId 
				inner join
					Book book2_ 
						on books1_.bookId=book2_.id 
				where
					book2_.title like '%Hibernate%'
			```
