This is Spring demo from Udemy Spring & Hibernate course.   
- Thymeleaf -  Bootstrap
    - visit https://getbootstrap.com/ to include Bootstrap’s production-ready CSS and JavaScript via CDN without the need for any build steps. 

```
<! doctype html>
<html xmlns:th="http://www.thymeleaf.org">
<head>

<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.2/dist/css/bootstrap.min.css"
	rel="stylesheet"
	integrity="sha384-Zenh87qX5JnK2Jl0vWa8Ck2rdkQ2Bzep5IDxbcnCeuOxjzrPF/et3URy9Bv1WTRi"
	crossorigin="anonymous">


<title>Thymeleaf list employees demo</title>

</head>
<body>
	<div class="container">
		<h3>Below are the list of our employees:</h3>

		<table class="table table-border table-striped">
			<thead class="thread-dark">
				<tr>
					<th>First Name</th>
					<th>Last Name</th>
					<th>Email</th>
				</tr>
			</thead>
			<tbody>
				<tr th:each="tempEmployee : ${employees}">
					<td th:text="${tempEmployee.firstName}" />
					<td th:text="${tempEmployee.lastName}" />
					<td th:text="${tempEmployee.email}" />
				</tr>
			</tbody>
		</table>
	</div>
</body>
</html>
```