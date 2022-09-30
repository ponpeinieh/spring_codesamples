package com.example.aopdemo.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.example.aopdemo.entity.Account;

@Component
public class AccountDAO {

	public List<Account> findAccounts(boolean tripWire) {
		if (tripWire)
			throw new RuntimeException("Exception happens in findAccount()");
		List<Account> myAccounts = new ArrayList<>();

		// create sample accounts
		Account temp1 = new Account("John", "Silver");
		Account temp2 = new Account("Madhu", "Platinum");
		Account temp3 = new Account("Luca", "Gold");

		// add them to our accounts list
		myAccounts.add(temp1);
		myAccounts.add(temp2);
		myAccounts.add(temp3);

		return myAccounts;
	}

}
