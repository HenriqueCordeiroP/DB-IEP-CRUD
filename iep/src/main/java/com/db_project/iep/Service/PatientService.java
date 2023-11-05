package com.db_project.iep.Service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class PatientService {
	
	private final JdbcTemplate jdbcTemplate;
	
	@Autowired
	public PatientService(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	public List<Map<String, Object>> getPatientList(){
		return jdbcTemplate.queryForList("SELECT COALESCE(p.nome_social, pe.nome) as nome, "
										+ "e.email, "
										+ "pe.tel_celular, "
										+ "COALESCE(MAX(c.dt_consulta), '---') as dt_consulta, "
										+ "COALESCE(MIN(a.dt_agendamento), '---') as dt_agendamento "
										+ "FROM paciente p "
										+ "JOIN pessoa pe ON p.cpf_pessoa = pe.cpf "
										+ "JOIN email e ON p.cpf_pessoa = e.cpf_pessoa "
										+ "JOIN consulta c ON p.cpf_pessoa = c.cpf_paciente "
										+ "JOIN agendamento a ON p.cpf_pessoa = a.cpf_paciente "
										+ "GROUP BY nome, e.email, pe.tel_celular, dt_consulta, dt_agendamento "
										+ "ORDER BY nome ASC");
	}
	
}
