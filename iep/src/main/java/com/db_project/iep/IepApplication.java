package com.db_project.iep;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class IepApplication {

	public static void main(String[] args) {
		SpringApplication.run(IepApplication.class, args);
	}
	
	@GetMapping("/")
	public String greet() {
		return "";
	}
}
