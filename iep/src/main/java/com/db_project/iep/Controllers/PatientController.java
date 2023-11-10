package com.db_project.iep.Controllers;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.db_project.iep.Service.PatientService;

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
	
	@GetMapping("/create")
	public String create() {
		return "patient/create";
	}
	
	@PostMapping("/create")
	public String create_form(HttpServletRequest request) {
		String name = request.getParameter("nome");
		String nome_social = request.getParameter("nome_social");
		String rg = request.getParameter("rg");
		String cpf = request.getParameter("cpf");
		String celular = request.getParameter("celular");
		String residencial = request.getParameter("residencial");
		String email = request.getParameter("email");
		String cidade = request.getParameter("cidade");
		String bairro = request.getParameter("bairro");
		String rua = request.getParameter("rua");
		String numero = request.getParameter("numero");
		String dt_nascimento = request.getParameter("data_nascimento");
		String sexo = request.getParameter("sexo");
		String convenio = request.getParameter("convenio");
		String indicacao = request.getParameter("indicacao");
		String imc = request.getParameter("imc");
		String cintura = request.getParameter("cintura");
		String peso = request.getParameter("peso");
		String altura = request.getParameter("altura");
		
		int patientResult = patientService.createPatient(name, nome_social, cpf, rg, celular, residencial, email, cidade, bairro, rua, numero, 
				dt_nascimento, sexo, convenio, indicacao, imc, cintura, peso, altura);
		if (patientResult > 0) {
			return "redirect:/patient/read";			
		} else {
			// return error
			return null;
		}
	}
}
