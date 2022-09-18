package com.example.mvc;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class Customer {
	private String firstName;
	@NotNull(message = "is required")
	@Size(min = 1, message = "minimum size is 1")
	private String lastName;
	@Min(value = 0, message = "greater or equal to 0")
	@Max(value = 10, message = "less or equal to 10")
	private int freePasses;
	@Pattern(regexp = "^[A-Z0-9a-z]{5}", message = "only 5 alpha numericals")
	private String postalCode;

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public int getFreePasses() {
		return freePasses;
	}

	public void setFreePasses(int freePasses) {
		this.freePasses = freePasses;
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

}
