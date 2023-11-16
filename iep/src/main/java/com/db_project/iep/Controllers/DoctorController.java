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
import com.db_project.iep.Service.PatientService;

import Utils.Conversion;
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

	@PostMapping("/create")
	public String create_form(HttpServletRequest request) {
		String name = Conversion.parseStringOrNull(request.getParameter("nome"));
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
		String crm = Conversion.parseStringOrNull(request.getParameter("crm"));
		String rqe = Conversion.parseStringOrNull(request.getParameter("rqe"));
		String especialidade =Conversion.parseStringOrNull( request.getParameter("especialidade"));
		
		int patientResult = doctorService.createDoctor(name, cpf, rg, celular, residencial, email, cidade, bairro, rua, numero, 
				dt_nascimento, sexo, crm, rqe, especialidade);
		if (patientResult > 0) {
			return "redirect:/doctor/read";			
		} else {
			// return error
			return null;
		}
	}
	
	@GetMapping("/delete/{cpf}")
	public String delete(@PathVariable String cpf) {
		int patientResult = doctorService.deleteDoctor(cpf);
		if (patientResult > 0) {
			return "redirect:/doctor/read";			
		}
		return null;
	}

}