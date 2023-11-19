package com.db_project.iep.Controllers;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.db_project.iep.Service.PatientService;

import Utils.Parser;
import jakarta.servlet.http.HttpServletRequest;

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
	
	@GetMapping("/read/{cpf}")
	public String read(Model model, @PathVariable String cpf){
		Map<String, Object> patient = patientService.getPacienteByCPF(cpf);
		model.addAttribute("patient", patient);
		return "patient/read_one.html";
	}

	@GetMapping("/edit/{cpf}")
	public String edit(@PathVariable String cpf, Model model){
		Map<String, Object> patient = patientService.getPacienteByCPF(cpf);
		model.addAttribute("patient", patient);
		return "patient/edit";
	}

	@GetMapping("/create")
	public String create() {
		return "patient/create";
	}
	
	@PostMapping("/create")
	public String create_form(HttpServletRequest request, Model model) {
		Map<String, String> patient = Parser.parsePatientFromRequest(request);
		String patientResult = patientService.createPatient(patient);
		if (patientResult == null) {
			return "redirect:/patient/read";			
		} else {
			model.addAttribute("errorMessage", patientResult);
			model.addAttribute("patient", patient);
			return "patient/create";
		}
	}
	
	@PostMapping("/edit/{cpf}")
	public String update(HttpServletRequest request, @PathVariable String cpf, Model model) {
		Map<String, String> patient = Parser.parsePatientFromRequest(request);
		System.out.println(patient.get("nome"));
		String patientResult = patientService.updatePaciente(patient);
		if (patientResult == null) {
			return "redirect:/patient/read";			
		} else {
			model.addAttribute("errorMessage", patientResult);
			model.addAttribute("patient", patient);
			return "patient/edit";
		}
	}
	
	@GetMapping("/delete/{cpf}")
	public String delete(@PathVariable String cpf) {
		int patientResult = patientService.deletePatient(cpf);
		if (patientResult > 0) {
			return "redirect:/patient/read";			
		}
		return null;
	}

	@GetMapping("/report")
	public String report(){
		return "/patient/report";
	}
}
