package com.silascaimi.fasapi.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class GeradorDeSenha {

	public static void main(String[] args) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		System.out.println(encoder.encode("admin"));
	}
}
