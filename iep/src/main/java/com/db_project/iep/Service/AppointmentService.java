package com.db_project.iep.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
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
		return jdbcTemplate.queryForList("SELECT COALESCE(p.nome_social, pe.nome) AS nome, c.cpf_paciente, c.dt_consulta, c.confirmada, c.descricao, c.cid, pm.nome as nome_medico, c.cpf_medico " +
										 "FROM consulta c " + 
										 "JOIN paciente p ON p.cpf_pessoa = c.cpf_paciente " + 
										 "JOIN pessoa pe ON p.cpf_pessoa = pe.cpf " + 
										 "JOIN ( " +
											"SELECT m.cpf_pessoa, p2.nome FROM medico m JOIN pessoa p2 ON p2.cpf = m.cpf_pessoa " +
										 ") pm ON pm.cpf_pessoa = c.cpf_medico " +
										 "WHERE c.dt_consulta >= curdate() " + 
										 "ORDER BY c.dt_consulta asc");
	}
	
	public int createAppointment(String data, String descricao, String confirmada, String historia_clinica, String CID, String cpf, String cpf_medico) {
			confirmada = confirmada == "on" ? "true" : "false";
			Boolean confirmed = Boolean.parseBoolean(confirmada);
	        String sql = "INSERT INTO consulta (dt_consulta, descricao, confirmada, historia_clinica, cid, cpf_paciente, cpf_medico) VALUES (?,?,?,?,?,?,?)";
		
		return	jdbcTemplate.update(sql, data, descricao, confirmed, historia_clinica, CID, cpf, cpf_medico);
	}
	
	public Map<String, Object> getAppointmentByCPF(String cpf_paciente, String cpf_medico, String data) {
		String sql = "SELECT * " + 
		"FROM consulta c " +
		"WHERE c.cpf_paciente = ? AND " + 
		"c.cpf_medico = ? AND " +
		"c.dt_consulta = ?";
		return jdbcTemplate.queryForMap(sql, cpf_paciente, cpf_medico, data);
	}
	
	public int updateAppointment(String data, String descricao, String confirmada, String historia_clinica, String CID, String cpf, String cpf_medico) {
		confirmada = "on".equals(confirmada) ? "true" : "false";
		Boolean confirmed = Boolean.parseBoolean(confirmada);
		System.out.println("AAAAAaaaaaaaaaaaa "+ confirmed);
		String sql = "UPDATE consulta " +
	                 "SET descricao = ?, confirmada = ?, historia_clinica = ?, " +
	                 "cid = ? " +
	                 "WHERE cpf_paciente = ? AND cpf_medico = ? AND dt_consulta = ?";
		return jdbcTemplate.update(sql, descricao, confirmed, historia_clinica, CID, cpf, cpf_medico, data );
	}
	
	public int deleteAppointment(String cpf, String cpf_medico,String data) {
		String sql = "DELETE FROM consulta WHERE cpf_paciente = ? AND cpf_medico = ? AND dt_consulta = ?";
		
		return jdbcTemplate.update(sql, cpf, cpf_medico, data);
	}

}