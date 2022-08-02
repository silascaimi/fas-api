package com.silascaimi.fasapi;

import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FasApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(FasApiApplication.class, args);
	}
	
	@PostConstruct
	private void started() {
	    TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
	  }

}
