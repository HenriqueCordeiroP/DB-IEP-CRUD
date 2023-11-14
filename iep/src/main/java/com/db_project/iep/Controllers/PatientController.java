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

import Utils.Conversion;
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

	@GetMapping("/edit/{cpf}")
	public String edit(@PathVariable String cpf, Model model){
		Map<String, Object> patient = patientService.getPacienteByCPF(cpf);
		model.addAttribute("patient", patient);
		return "patient/edit";
	}
	
	@PostMapping("/create")
	public String create_form(HttpServletRequest request) {
		String name = Conversion.parseStringOrNull(request.getParameter("nome"));
		String nome_social = Conversion.parseStringOrNull(request.getParameter("nome_social"));
		String rg = Conversion.parseStringOrNull(request.getParameter("rg"));
		String cpf = Conversion.parseStringOrNull(request.getParameter("cpf"));
		String celular = Conversion.parseStringOrNull(request.getParameter("celular"));
		String residencial = Conversion.parseStringOrNull(request.getParameter("residencial"));
		String email = Conversion.parseStringOrNull(request.getParameter("email"));
		String cidade = Conversion.parseStringOrNull(request.getParameter("cidade"));
		String bairro = Conversion.parseStringOrNull(request.getParameter("bairro"));
		String rua = Conversion.parseStringOrNull(request.getParameter("rua"));
		String numero = Conversion.parseStringOrNull(request.getParameter("numero"));
		String dt_nascimento = Conversion.parseStringOrNull(request.getParameter("data_nascimento"));
		String sexo = Conversion.parseStringOrNull(request.getParameter("sexo"));
		String convenio = Conversion.parseStringOrNull(request.getParameter("convenio"));
		String profissao = Conversion.parseStringOrNull(request.getParameter("profissao"));
		String indicacao =Conversion.parseStringOrNull( request.getParameter("indicacao"));
		String imc = Conversion.parseStringOrNull(request.getParameter("imc"));
		String cintura = Conversion.parseStringOrNull(request.getParameter("cintura"));
		String peso = Conversion.parseStringOrNull(request.getParameter("peso"));
		String altura = Conversion.parseStringOrNull(request.getParameter("altura"));
		String alergias = Conversion.parseStringOrNull(request.getParameter("alergias"));
		String pressao = Conversion.parseStringOrNull(request.getParameter("pressao"));
		
		int patientResult = patientService.createPatient(name, nome_social, cpf, rg, celular, residencial, email, cidade, bairro, rua, numero, 
				dt_nascimento, sexo, convenio, profissao, indicacao, imc, cintura, peso, altura, alergias, pressao);
		if (patientResult > 0) {
			return "redirect:/patient/read";			
		} else {
			// return error
			return null;
		}
	}
	
	@PostMapping("/edit/{cpf}")
	public String update(HttpServletRequest request, @PathVariable String cpf) {
		String name = Conversion.parseStringOrNull(request.getParameter("nome"));
		String nome_social = Conversion.parseStringOrNull(request.getParameter("nome_social"));
		String rg = Conversion.parseStringOrNull(request.getParameter("rg"));
		String celular = Conversion.parseStringOrNull(request.getParameter("celular"));
		String residencial = Conversion.parseStringOrNull(request.getParameter("residencial"));
		String email = Conversion.parseStringOrNull(request.getParameter("email"));
		String cidade = Conversion.parseStringOrNull(request.getParameter("cidade"));
		String bairro = Conversion.parseStringOrNull(request.getParameter("bairro"));
		String rua = Conversion.parseStringOrNull(request.getParameter("rua"));
		String numero = Conversion.parseStringOrNull(request.getParameter("numero"));
		String dt_nascimento = Conversion.parseStringOrNull(request.getParameter("data_nascimento"));
		String sexo = Conversion.parseStringOrNull(request.getParameter("sexo"));
		String convenio = Conversion.parseStringOrNull(request.getParameter("convenio"));
		String profissao = Conversion.parseStringOrNull(request.getParameter("profissao"));
		String indicacao =Conversion.parseStringOrNull( request.getParameter("indicacao"));
		String imc = Conversion.parseStringOrNull(request.getParameter("imc"));
		String cintura = Conversion.parseStringOrNull(request.getParameter("cintura"));
		String peso = Conversion.parseStringOrNull(request.getParameter("peso"));
		String altura = Conversion.parseStringOrNull(request.getParameter("altura"));
		String alergias = Conversion.parseStringOrNull(request.getParameter("alergias"));
		String pressao = Conversion.parseStringOrNull(request.getParameter("pressao"));
		int patientResult = patientService.updatePaciente(name, nome_social, cpf, rg, celular, residencial, email, cidade, bairro, rua, numero, 
				dt_nascimento, sexo, convenio, profissao, indicacao, imc, cintura, peso, altura, alergias, pressao);
		if (patientResult > 0) {
			return "redirect:/patient/read";			
		}
		return null;
	}
	
	@GetMapping("/delete/{cpf}")
	public String delete(@PathVariable String cpf) {
		int patientResult = patientService.deletePatient(cpf);
		if (patientResult > 0) {
			return "redirect:/patient/read";			
		}
		return null;
	}
}
