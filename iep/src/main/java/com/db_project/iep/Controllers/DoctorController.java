package com.db_project.iep.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.db_project.iep.Service.DoctorService;

@Controller
@RequestMapping("/doctor")
public class DoctorController {
	private final DoctorService doctorService;
	
	
	public DoctorController(DoctorService doctorService) {
		this.doctorService = doctorService;
	}


	@GetMapping("/read")
	public String read(Model model){
		return "doctor/read";
	}
}

@GetMapping("/create")
	public String create() {
		return "doctor/create";
	}
 