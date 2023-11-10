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
		return jdbcTemplate.queryForList("SELECT COALESCE(p.nome_social, pe.nome) AS nome, e.email, pe.tel_celular, "
				+ "COALESCE(MIN(c.dt_consulta), '---') AS consulta_dt, COALESCE(MIN(a.dt_agendamento), '---') AS agendamento_dt "
				+ "FROM paciente p "
				+ "JOIN pessoa pe ON p.cpf_pessoa = pe.cpf "
				+ "JOIN email e ON p.cpf_pessoa = e.cpf_pessoa  "
				+ "LEFT JOIN("
				+ "SELECT cons.cpf_paciente, MAX(cons.dt_consulta) AS dt_consulta "
				+ "FROM consulta cons "
				+ "WHERE cons.dt_consulta <= CURDATE() "
				+ "GROUP BY cons.cpf_paciente "
				+ ") c ON p.cpf_pessoa = c.cpf_paciente "
				+ "LEFT JOIN ("
				+ "SELECT ag.cpf_paciente, MIN(ag.dt_agendamento) AS dt_agendamento "
				+ "FROM agendamento ag "
				+ "WHERE ag.dt_agendamento >= CURDATE() "
				+ "GROUP BY ag.cpf_paciente "
				+ ") a ON p.cpf_pessoa = c.cpf_paciente "
				+ "GROUP BY COALESCE(p.nome_social, pe.nome), e.email, pe.tel_celular "
				+ "ORDER BY nome ASC");
	}
	
	public int createPatient(String name, String nome_social, String cpf, String rg, String celular,String residencial,String email,
							  String cidade, String bairro, String rua, String numero, String dt_nascimento, String sexo,String convenio, 
							  String indicacao, String imc,String cintura,String peso,String altura) {
		PessoaService pessoaService = new PessoaService(jdbcTemplate);
		int pessoaResult = pessoaService.createPessoa(cpf, altura, nome_social, rg, sexo, residencial, celular, cidade, bairro, rua, numero);
		int emailResult = pessoaService.createEmail(cpf, email);
		if (pessoaResult > 0 && emailResult > 0) {
			// create patient
		}
		return 0;
	}

}