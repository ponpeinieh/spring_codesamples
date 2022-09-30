package com.example.aopdemo.dao;

import org.springframework.stereotype.Component;

@Component
public class AccountDAO {
	public void addAccount() {
		System.out.println(getClass()+" doing my db work: add an account!");
	}
	public void goToSleep() {
		System.out.println(getClass()+" ready to sleep!");
	}
	public int getInt() {
		System.out.println(getClass()+" ready to return an int");
		return 1;
	}

	public void setInt(int x) {
		System.out.println(getClass()+" ready to set an int");
	}
}
