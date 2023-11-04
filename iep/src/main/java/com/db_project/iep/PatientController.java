package com.db_project.iep;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/patient")
public class PatientController {

	@GetMapping("/read")
	public String read(){
		return "patient/read";
	}
}