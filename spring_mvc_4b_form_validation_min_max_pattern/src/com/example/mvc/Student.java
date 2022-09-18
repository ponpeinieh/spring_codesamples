package com.example.mvc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Student {
	private String firstName;
	private String lastName;
	private String country;
	private String favoriteLanguage;
	private Map<String, String> countryOptions;
	private Map<String, String> languageOptions;
	private Map<String, String> operatingSystemOptions;
	private String[] operatingSystems;

	//	private List<String> countryOptions;
	public Student() {
		countryOptions = new HashMap<String, String>();
		countryOptions.put("BR", "Brazil");
		countryOptions.put("FR", "France");
		countryOptions.put("DE", "Germany");
		countryOptions.put("IN", "India");
		languageOptions = new HashMap<String, String>();
		languageOptions.put("Java", "Java");
		languageOptions.put("C++", "C++");
		languageOptions.put("Dart", "Dart");
		languageOptions.put("Lua", "Lua");
		operatingSystemOptions = new HashMap<String, String>();
		operatingSystemOptions.put("Windows", "Windows");
		operatingSystemOptions.put("Linux", "Linux");
		operatingSystemOptions.put("FreeBSD", "FreeBSD");
		operatingSystemOptions.put("MacOS", "MacOS");
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

	public Map<String, String> getOperatingSystemOptions() {
		return operatingSystemOptions;
	}

	public String[] getOperatingSystems() {
		return operatingSystems;
	}

	public void setOperatingSystems(String[] operatingSystems) {
		this.operatingSystems = operatingSystems;
	}

	public Map<String, String> getLanguageOptions() {
		return languageOptions;
	}

	public String getFavoriteLanguage() {
		return favoriteLanguage;
	}

	public void setFavoriteLanguage(String favoriteLanguage) {
		this.favoriteLanguage = favoriteLanguage;
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
