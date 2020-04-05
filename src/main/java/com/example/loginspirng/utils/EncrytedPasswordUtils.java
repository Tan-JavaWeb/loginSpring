package com.example.loginspirng.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class EncrytedPasswordUtils {

	 // Encryte Password with BCryptPasswordEncoder
	public static String encrytePassword(String password) {
		return new BCryptPasswordEncoder().encode(password);
	}
	
//	public static void main(String[] args) {
//		System.out.println(encrytePassword("123"));
//	}
}
