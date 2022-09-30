package com.example.aopdemo.dao;

import org.springframework.stereotype.Component;

@Component
public class AccountDAO {
	public void addAccount(int x, float y) {
		System.out.println(getClass()+" doing my db work: add an account!");
	}
}
