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
import com.db_project.iep.Service.DoctorService;
import com.db_project.iep.Service.PatientService;

import Utils.Parser;
import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/appointment")
public class AppointmentController {
	private final AppointmentService appointmentService;
	private final PatientService patientService;
	private final DoctorService doctorService;
	
	
	public AppointmentController(AppointmentService appointmentService, PatientService patientService, DoctorService doctorService) {
		this.appointmentService = appointmentService;
		this.patientService = patientService;
		this.doctorService = doctorService;
	}


	@GetMapping("/read")
	public String read(Model model){
		List<Map<String, Object>> appointments = appointmentService.getAppointmentList();
		List<Map<String, Object>> patients = patientService.getPatientList();
		List<Map<String, Object>> doctors = doctorService.getDoctorList();
		
		model.addAttribute("appointments", appointments);
		model.addAttribute("patients", patients);
		model.addAttribute("doctors", doctors);
		return "appointment/read";
	}

	@GetMapping("/read/{cpf_paciente}/{cpf_medico}/{data}")
	public String read(Model model, @PathVariable String cpf_paciente, @PathVariable String cpf_medico, @PathVariable String data  ){
		Map<String, Object> appointment = appointmentService.getAppointmentByCPF(cpf_paciente, cpf_medico, data);
		
		
		model.addAttribute("appointment", appointment);
		return "appointment/read_one";
	}
	
	@GetMapping("/create")
	public String create(HttpServletRequest request, Model model) {
		List<Map<String, Object>> patients = patientService.getPatientList();
		List<Map<String, Object>> doctors = doctorService.getDoctorList();

		model.addAttribute("patients", patients);
		model.addAttribute("doctors", doctors);
		return "appointment/create";
	}

	@PostMapping("/create")
	public String create_form(HttpServletRequest request, Model model) {
		Map<String, String> appointment = Parser.parseAppointmentFromRequest(request);
		System.out.println(appointment);
		String appointmentResult = appointmentService.createAppointment(appointment);
		
		if (appointmentResult == null) {
			return "redirect:/appointment/read";			
		} else {
			List<Map<String, Object>> patients = patientService.getPatientList();
			List<Map<String, Object>> doctors = doctorService.getDoctorList();
			model.addAttribute("patients", patients);
			model.addAttribute("doctors", doctors);
			model.addAttribute("errorMessage", appointmentResult);
			model.addAttribute("appointment", appointment);
			System.out.println(appointment);
			return "appointment/create";
		}
	}

	@GetMapping("/edit/{cpf_paciente}/{cpf_medico}/{data}")
	public String edit(@PathVariable String cpf_paciente, @PathVariable String cpf_medico, @PathVariable String data,  Model model){
		if(cpf_medico == "---"){
			cpf_medico = null;
		}
		if(cpf_paciente == "---"){
			cpf_paciente = null;
		}
		Map<String, Object> appointment = appointmentService.getAppointmentByCPF(cpf_paciente, cpf_medico, data);
		model.addAttribute("appointment", appointment);
		return "appointment/edit";
	}
	
	@PostMapping("/edit/{cpf}/{cpf_medico}/{data_original}")
	public String update(Model model, HttpServletRequest request, @PathVariable String cpf, @PathVariable String cpf_medico, @PathVariable String data_original) {
		Map<String, String> appointment = Parser.parseAppointmentFromRequest(request);
		String appointmentResult = null;
		if(!appointment.get("data").equals(data_original)){
			appointmentService.deleteAppointment(cpf, cpf_medico, data_original);
			appointmentResult = appointmentService.createAppointment(appointment);
		} else{
			appointmentResult = appointmentService.updateAppointment(appointment);
		}
		if (appointmentResult == null) {
			return "redirect:/appointment/read";			
		} else {
			model.addAttribute("errorMessage", appointmentResult);
			model.addAttribute("appointment", appointment);
			return "/appointment/edit";
		}
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
