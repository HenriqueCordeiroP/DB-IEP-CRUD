package com.db_project.iep.Service;

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
					 "WHERE c.dt_consulta >= CURDATE() " +
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
}