package com.db_project.iep;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@SpringBootApplication
@Controller
public class IepApplication {

	public static void main(String[] args) {
		SpringApplication.run(IepApplication.class, args);
	}
	
	@GetMapping("/")
	public String greet() {
		return "redirect:/patient/read";
	}
}
