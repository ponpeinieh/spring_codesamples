package com.example.mvc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Student {
	private String firstName;
	private String lastName;
	private String country;
	private Map<String,String> countryOptions;
//	private List<String> countryOptions;
	public Student() {
		countryOptions = new HashMap<String,String>();
		countryOptions.put("BR", "Brazil");
		countryOptions.put("FR", "France");
		countryOptions.put("DE", "Germany");
		countryOptions.put("IN", "India");
//		countryOptions = new ArrayList<String>();
//		countryOptions.add("Brazil");
//		countryOptions.add("France");
	}
//public List<String> getCountryOptions() {
//		return countryOptions;
//	}
		public Map<String, String> getCountryOptions() {
		return countryOptions;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
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
