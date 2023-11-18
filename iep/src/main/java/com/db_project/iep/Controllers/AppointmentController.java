package com.db_project.iep.Controllers;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.db_project.iep.Service.AppointmentService;

import Utils.Conversion;
import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/appointment")
public class AppointmentController {
	private final AppointmentService appointmentService;
	
	
	public AppointmentController(AppointmentService appointmentService) {
		this.appointmentService = appointmentService;
	}


	@GetMapping("/read")
	public String read(Model model){
		List<Map<String, Object>> appointments = appointmentService.getAppointmentList();
		model.addAttribute("appointments", appointments);
		return "appointment/read";
	}
	
	@GetMapping("/create")
	public String create(HttpServletRequest request) {
		return "appointment/create";
	}

	@GetMapping("/edit/{cpf_paciente}/{cpf_medico}/{data}")
	public String edit(@PathVariable String cpf_paciente, @PathVariable String cpf_medico, @PathVariable String data,  Model model){
		Map<String, Object> appointment = appointmentService.getAppointmentByCPF(cpf_paciente, cpf_medico, data);
		model.addAttribute("appointment", appointment);
		return "appointment/edit";
	}
	
	@PostMapping("/create")
	public String create_form(HttpServletRequest request) {
		String data = Conversion.parseStringOrNull(request.getParameter("data"));
		String descricao = Conversion.parseStringOrNull(request.getParameter("descricao"));
		String confirmada = request.getParameter("confirmada");
		String historia_clinica = Conversion.parseStringOrNull(request.getParameter("historia_clinica"));
		String CID = Conversion.parseStringOrNull(request.getParameter("CID"));
        String cpf = Conversion.parseStringOrNull(request.getParameter("cpf"));
        String cpf_medico = Conversion.parseStringOrNull(request.getParameter("cpf_medico"));
		int appointmentResult = appointmentService.createAppointment(data, descricao, confirmada, historia_clinica, CID, cpf, cpf_medico);
		if (appointmentResult > 0) {
			return "redirect:/appointment/read";			
		} else {
			// return error
			return null;
		}
	}
	
	@PostMapping("/edit/{cpf}/{cpf_medico}/{data_original}")
	public String update(HttpServletRequest request, @PathVariable String cpf, @PathVariable String cpf_medico, @PathVariable String data_original) {
		String data = Conversion.parseStringOrNull(request.getParameter("data"));
		String descricao = Conversion.parseStringOrNull(request.getParameter("descricao"));
		String confirmada = request.getParameter("confirmada");
		String historia_clinica = Conversion.parseStringOrNull(request.getParameter("historia_clinica"));
		String CID = Conversion.parseStringOrNull(request.getParameter("CID"));
		int appointmentResult = 0;
		if(!data.equals(data_original)){
			System.out.println("AAAAAAAA " + data);
			appointmentService.deleteAppointment(cpf, cpf_medico, data_original);
			appointmentResult = appointmentService.createAppointment(data, descricao, confirmada, historia_clinica, CID, cpf, cpf_medico);
		} else{
			appointmentResult = appointmentService.updateAppointment(data_original, descricao, confirmada, historia_clinica, CID, cpf, cpf_medico);
		}
		if (appointmentResult > 0) {
			return "redirect:/appointment/read";			
		}
		return null;
	}
	
	@GetMapping("/delete/{cpf}/{cpf_medico}/{data}")
	public String delete(@PathVariable String cpf, @PathVariable String cpf_medico, @PathVariable String data) {
		int appointmentResult = appointmentService.deleteAppointment(cpf, cpf_medico, data);
		if (appointmentResult > 0) {
			return "redirect:/appointment/read";			
		}
		return null;
	}
}
