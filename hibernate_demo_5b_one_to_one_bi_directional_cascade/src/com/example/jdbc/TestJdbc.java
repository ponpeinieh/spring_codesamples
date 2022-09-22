package com.example.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;

public class TestJdbc {

	public static void main(String[] args) {
		String jdbcUrl = "jdbc:mysql://localhost:3306/hb-01-one-to-one-uni?useSSL=false&serverTimezone=UTC";
		String user="hbstudent";
		String passwd="hbstudent";
		try {
			System.out.println("Connect to db:"+jdbcUrl);
			Connection conn = DriverManager.getConnection(jdbcUrl,user,passwd);
			System.out.println("Successful connect to db!");
			
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}

}
