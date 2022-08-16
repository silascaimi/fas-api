package com.silascaimi.fasapi.util;

import java.text.ParseException;

import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTParser;

public class DecodeToken {

	public static void main(String[] args) {

		try {
			JWT jwt = JWTParser.parse(
					"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c");

			jwt.getHeader(); // Header do token
			jwt.getJWTClaimsSet(); // Payload do token
		} catch (ParseException e) {
			e.printStackTrace();
		}

	}
}
