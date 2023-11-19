package com.db_project.iep.Controllers;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.db_project.iep.Service.DoctorService;

import Utils.Conversion;
import Utils.Parser;
import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/doctor")
public class DoctorController {
	private final DoctorService doctorService;
	
	
	public DoctorController(DoctorService doctorService) {
		this.doctorService = doctorService;
	}


	@GetMapping("/read")
	public String read(Model model){
		List<Map<String, Object>> doctors = doctorService.getDoctorList();
		model.addAttribute("doctors", doctors);
		return "doctor/read";
	}


	@GetMapping("/create")
	public String create() {
			return "doctor/create";
	}

	@GetMapping("/edit/{cpf}")
	public String edit(@PathVariable String cpf, Model model){
		Map<String, Object> doctor = doctorService.getDoctorByCPF(cpf);
		model.addAttribute("doctor", doctor);
		return "doctor/edit";
	}

	@PostMapping("/edit/{cpf}")
	public String edit_form(HttpServletRequest request, @PathVariable String cpf, Model model){
		Map<String, String> doctor = Parser.parseDoctorFromRequest(request);
		
		String doctorResult = doctorService.updateDoctor(doctor);
		if (doctorResult == null) {
			return "redirect:/doctor/read";			
		} else {
			model.addAttribute("errorMessage", doctorResult);
			model.addAttribute("appointment", doctor);
			return null;
		}
	}

	@PostMapping("/create")
	public String create_form(HttpServletRequest request, Model model) {
		Map<String, String> doctor = Parser.parseDoctorFromRequest(request);
		
		String patientResult = doctorService.createDoctor(doctor);
		if (patientResult == null) {
			return "redirect:/doctor/read";			
		} else {
			model.addAttribute("errorMessage", patientResult);
			model.addAttribute("appointment", doctor);
			return "/doctor/create";
		}
	}
	
	@GetMapping("/delete/{cpf}")
	public String delete(@PathVariable String cpf) {
		int doctorResult = doctorService.deleteDoctor(cpf);
		if (doctorResult > 0) {
			return "redirect:/doctor/read";			
		}
		return null;
	}

}