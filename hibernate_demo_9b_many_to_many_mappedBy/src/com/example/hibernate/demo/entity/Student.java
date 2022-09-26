package com.example.hibernate.demo.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "student")
public class Student {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(name = "first_name")
	private String firstName;
	@Column(name = "last_name")
	private String lastName;
	@Column(name = "email")
	private String email;

	@ManyToMany(mappedBy = "students")
	private Set<Course> courses;

	//
	public Student() {
	}

	public Student(String firstName, String lastName, String email) {
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

	public int getId() {
		return id;
	}

	public Set<Course> getCourses() {
		if (courses == null)
			courses = new HashSet<Course>();
		return courses;
	}

	public void setCourses(Set<Course> courses) {
		this.courses = courses;
	}

	// add the convenience method for bi-directional relationship
	public void add(Course course) {
		if (courses == null) {
			courses = new HashSet<>();
		}
		courses.add(course);
		course.getStudents().add(this);
	}

	public void remove(Course course) {
		if (courses == null) {
			return;
		}
		courses.remove(course);
		course.getStudents().remove(this);
	}

	@Override
	public String toString() {
		return "Instructor [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", email=" + email
				+ "]";
	}

}
