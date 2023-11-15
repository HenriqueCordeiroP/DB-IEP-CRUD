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
@RequestMapping("/appointment")
public class AppointmentController {
	private final PatientService appointmentService;
	
	
	public AppointmentController(AppointmentService appointmentService) {
		this.appointmentService = appointmentService;
	}


	@GetMapping("/read")
	public String read(Model model){
		List<Map<String, Object>> appointment = appointmentService.getAppointmentList();
		model.addAttribute("appointments", appointment);
		return "appointment/read";
	}
	
	@GetMapping("/create")
	public String create() {
		return "appointment/create";
	}

	@GetMapping("/edit/{cpf}")
	public String edit(@PathVariable String cpf, Model model){
		Map<String, Object> appointment = appointmentService.getAppointmentByCPF(cpf);
		model.addAttribute("appointment", appointment);
		return "appointment/edit";
	}
	
	@PostMapping("/create")
	public String create_form(HttpServletRequest request) {
		String data = Conversion.parseStringOrNull(request.getParameter("data"));
		String descricao = Conversion.parseStringOrNull(request.getParameter("descricao"));
		String confirmada = Conversion.parseStringOrNull(request.getParameter("confirmada"));
		String historia_clinica = Conversion.parseStringOrNull(request.getParameter("historia_clinica"));
		String CID = Conversion.parseStringOrNull(request.getParameter("CID"));
        String cpf = Conversion.parseStringOrNull(request.getParameter("cpf"));
		int appointmentResult = appointmentService.updateAppointment(data, descricao, confirmada, historia_clinica, CID, cpf);
		if (appointmentResult > 0) {
			return "redirect:/appointment/read";			
		} else {
			// return error
			return null;
		}
	}
	
	@PostMapping("/edit/{cpf}")
	public String update(HttpServletRequest request, @PathVariable String cpf) {
		String data = Conversion.parseStringOrNull(request.getParameter("data"));
		String descricao = Conversion.parseStringOrNull(request.getParameter("descricao"));
		String confirmada = Conversion.parseStringOrNull(request.getParameter("confirmada"));
		String historia_clinica = Conversion.parseStringOrNull(request.getParameter("historia_clinica"));
		String CID = Conversion.parseStringOrNull(request.getParameter("CID"));
		int appointmentResult = appointmentService.updateAppointment(data, descricao, confirmada, historia_clinica, CID, cpf);
		if (appointmentResult > 0) {
			return "redirect:/appointment/read";			
		}
		return null;
	}
	
	@GetMapping("/delete/{cpf}")
	public String delete(@PathVariable String cpf) {
		int appointmentResult = appointmentService.deleteAppointment(cpf);
		if (appointmentResult > 0) {
			return "redirect:/appointment/read";			
		}
		return null;
	}
}
