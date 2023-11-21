package com.db_project.iep.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class AppointmentService {
	
	private final JdbcTemplate jdbcTemplate;
	
	@Autowired
	public AppointmentService(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	public List<Map<String, Object>> getAppointmentList(){
		String sql = "SELECT COALESCE(p.nome_social, pe.nome, '---') AS nome, COALESCE(c.cpf_paciente, '---') AS cpf_paciente, " +
					 "c.dt_consulta, c.confirmada, c.descricao, c.cid, COALESCE(pm.nome, '---') as nome_medico, " +
					 "COALESCE(c.cpf_medico, '---') as cpf_medico, c.id " +
					 "FROM consulta c " +
					 "LEFT JOIN paciente p ON p.cpf_pessoa = c.cpf_paciente " +
					 "LEFT JOIN pessoa pe ON p.cpf_pessoa = pe.cpf " +
					 "LEFT JOIN (" +
					 "    SELECT m.cpf_pessoa, p2.nome FROM medico m JOIN pessoa p2 ON p2.cpf = m.cpf_pessoa" +
					 ") pm ON pm.cpf_pessoa = c.cpf_medico " +
					 "ORDER BY c.dt_consulta ASC";
		return jdbcTemplate.queryForList(sql);
	}
	
	public String createAppointment(Map<String, String> appointment) {
			Boolean confirmed = Boolean.parseBoolean(appointment.get("confirmada"));
	        String sql = "INSERT INTO consulta (dt_consulta, descricao, confirmada, historia_clinica, cid, cpf_paciente, cpf_medico) VALUES (?,?,?,?,?,?,?)";
		try{
			jdbcTemplate.update(sql, appointment.get("data"), appointment.get("descricao"), confirmed, 
		appointment.get("historia_clinica"),appointment.get("CID"), appointment.get("cpf"), appointment.get("cpf_medico"));
		} catch (UncategorizedSQLException u){
			if(u.getMessage().contains("Invalid date for Consulta")){
				return "A data deve ser posterior a hoje.";
			}
		} catch (DataIntegrityViolationException d){
			if(d.getMessage().contains("consulta_paciente_cpf_fk")){
				return "Paciente não encontrado.";
			}
			else if(d.getMessage().contains("consulta_medico_cpf_fk")){
				return "Médico não encontrado.";
			}
			else{
				return "Houve um erro. Tente novamente.";
			}
		} 
		return null;	
	}
	
	public Map<String, Object> getAppointmentByID(String id) {
		String sql = "SELECT * " + 
		"FROM consulta c " +
		"WHERE c.id = ? ";
		return jdbcTemplate.queryForMap(sql, id);
	}
	
	public String updateAppointment(Map<String, String> appointment, String id) {
		Boolean confirmed = Boolean.parseBoolean(appointment.get("confirmada"));
		String sql = "UPDATE consulta " +
	                 "SET descricao = ?, confirmada = ?, historia_clinica = ?, " +
	                 "cid = ? " +
	                 "WHERE id = ?";
		try {
			jdbcTemplate.update(sql, appointment.get("descricao"), confirmed, 
			appointment.get("historia_clinica"), appointment.get("CID"), id);
		} catch (UncategorizedSQLException u){
			if(u.getMessage().contains("Invalid date for Consulta")){
				return "A data deve ser posterior a hoje.";
			}
		}
		return null;
	}
	
	public int deleteAppointment(String id) {
		String sql = "DELETE FROM consulta WHERE id = ?";
		
		return jdbcTemplate.update(sql, id);
	}

	public int removeDoctorFromAppointments(String cpf_medico){
		String sql = "UPDATE consulta SET cpf_medico = NULL WHERE cpf_medico = ?";

		return jdbcTemplate.update(sql, cpf_medico);
	}

	public int removePatientFromAppointments(String cpf_paciente){
		String sql = "UPDATE consulta SET cpf_paciente = NULL WHERE cpf_paciente = ?";

		return jdbcTemplate.update(sql, cpf_paciente);
	}

	public List<Map<String, Object>> filterAppointments(Map<String, String> dateMap){
		if(dateMap.get("start_date") == null){
			dateMap.put("start_date", "0001/01/01");
		}
		if(dateMap.get("end_date") == null){
			dateMap.put("end_date", "9999/12/31");
		}
		String sql = "SELECT COALESCE(p.nome_social, pe.nome, '---') AS nome, COALESCE(c.cpf_paciente, '---') AS cpf_paciente, " +
					 "c.dt_consulta, c.confirmada, c.descricao, c.cid, COALESCE(pm.nome, '---') as nome_medico, " +
					 "COALESCE(c.cpf_medico, '---') as cpf_medico, c.id " +
					 "FROM consulta c " +
					 "LEFT JOIN paciente p ON p.cpf_pessoa = c.cpf_paciente " +
					 "LEFT JOIN pessoa pe ON p.cpf_pessoa = pe.cpf " +
					 "LEFT JOIN (" +
					 "    SELECT m.cpf_pessoa, p2.nome FROM medico m JOIN pessoa p2 ON p2.cpf = m.cpf_pessoa" +
					 ") pm ON pm.cpf_pessoa = c.cpf_medico " +
					 "WHERE c.dt_consulta < ? AND c.dt_consulta > ? " +
					 "ORDER BY c.dt_consulta ASC";
		return jdbcTemplate.queryForList(sql, dateMap.get("end_date"), dateMap.get("start_date"));
	}

	public Map<String, String> generateReport(){
		Map<String, String> report = new HashMap<>();
		report.put("average_return", getAverageReturn()); 
		report.put("average_appointments_patients", getAverageAppointmentsPatients());
		report.put("average_appointments_doctors", getAverageAppointmentsDoctor());
		report.put("total_appointments", getTotalAppointments());
		return report;
	}

	public String getAverageReturn(){
		String sql = "SELECT COALESCE(ROUND(AVG(a.return_time), 2), 'Não há retorno') AS average_return "+
		"FROM ( "+
		"SELECT cpf_paciente, dt_consulta, DATEDIFF(dt_consulta, LAG(dt_consulta) OVER (PARTITION BY cpf_paciente ORDER BY dt_consulta)) AS return_time " +
		"FROM consulta " +
		") a";
		Map<String, Object> result = jdbcTemplate.queryForMap(sql); 
		String avg_Result = result.get("average_return").toString();
		String avg_return = avg_Result.substring(0, avg_Result.length() - 3); // remove .00
		return avg_return;
	}

	public String getAverageAppointmentsPatients(){
		String sql = "SELECT coalesce(round(AVG(appointments_count)), '0') AS average_appointments_per_patient "+
					 "FROM ( " +
					 "SELECT cpf_paciente, COUNT(*) AS appointments_count "+
					 "FROM consulta "+
					 "GROUP BY cpf_paciente "+
					 ") AS patient_counts";
		Map<String, Object> result = jdbcTemplate.queryForMap(sql); 
		String avg_appointment = result.get("average_appointments_per_patient").toString();
		return avg_appointment; 
	}

	public String getAverageAppointmentsDoctor(){
		String sql = "SELECT COALESCE(ROUND(AVG(appointments_count)), '0') AS average_appointments_per_doctor "+
					 "FROM ( " +
					 "SELECT cpf_medico, COUNT(*) AS appointments_count "+
					 "FROM consulta "+
					 "GROUP BY cpf_medico "+
					 ") AS doctor_counts";
		Map<String, Object> result = jdbcTemplate.queryForMap(sql); 
		String avg_appointment = result.get("average_appointments_per_doctor").toString();
		return avg_appointment; 
	}

	public String getTotalAppointments(){
		String sql = "SELECT COUNT(*) AS total " +
					 "FROM consulta " +
					 "WHERE YEAR(dt_consulta) = YEAR(CURDATE()) AND " +
					 "MONTH(dt_consulta) = MONTH(CURDATE())";
		Map<String, Object> result = jdbcTemplate.queryForMap(sql); 
		String total = result.get("total").toString();
		return total;
	}
}