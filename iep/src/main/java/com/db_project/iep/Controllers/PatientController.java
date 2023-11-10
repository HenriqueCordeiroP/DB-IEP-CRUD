package com.db_project.iep.Controllers;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.db_project.iep.Service.PatientService;

@Controller
@RequestMapping("/patient")
public class PatientController {
	private final PatientService patientService;
	
	
	public PatientController(PatientService patientService) {
		this.patientService = patientService;
	}


	@GetMapping("/read")
	public String read(Model model){
		List<Map<String, Object>> patients = patientService.getPatientList();
		model.addAttribute("patients", patients);
		return "patient/read";
	}
	
	@GetMapping("/create")
	public String create(Model model) {
		return "patient/create";
	}
}
