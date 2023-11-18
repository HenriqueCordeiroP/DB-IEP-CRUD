package com.db_project.iep.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
@Service
public class DoctorService {
	
	private final JdbcTemplate jdbcTemplate;
	
	@Autowired
	public DoctorService(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	public List<Map<String, Object>> getDoctorList(){
		return jdbcTemplate.queryForList("SELECT pe.nome, e.email, pe.tel_celular, m.nome_especialidade,pe.cpf "  
		+"FROM medico m "
		+"JOIN pessoa pe ON m.cpf_pessoa = pe.cpf "
		+"JOIN email e ON m.cpf_pessoa = e.cpf_pessoa "
		+"GROUP BY pe.nome, e.email, pe.tel_celular , m.nome_especialidade "
		+"ORDER BY nome ASC");
	}

	public int createDoctor(String name, String cpf, String rg, String celular,String residencial,String email,String cidade, String bairro, String rua, String numero, String dt_nascimento, String sexo, String crm, String rqe, String especialidade) {
		PessoaService pessoaService = new PessoaService(jdbcTemplate);
		int pessoaResult = pessoaService.createPessoa(cpf, rg, name, dt_nascimento, sexo, residencial, celular, cidade, bairro, rua, numero);
		int emailResult = pessoaService.createEmail(cpf, email);
		int doctorResult = 0;
		if (pessoaResult > 0 && emailResult > 0) {
			String sql = "INSERT INTO medico VALUES(?, ?, ?, ?, ?)";
			return jdbcTemplate.update(sql, crm, rqe, especialidade, cpf);
		}
		return doctorResult;
	}

	public int updateDoctor(String name, String cpf, String rg, String celular,String residencial,String email,
							  String cidade, String bairro, String rua, String numero, String dt_nascimento, String sexo,String crm, String rqe, String especialidade) {
		PessoaService pessoaService = new PessoaService(jdbcTemplate);
		int pessoaResult = pessoaService.updatePessoa(cpf, rg, name, dt_nascimento, sexo, residencial, celular, cidade, bairro, rua, numero);
		pessoaService.createEmail(cpf, email);
		int doctorResult = 0;
		if (pessoaResult > 0) {
			String sql = "UPDATE medico "
			+ "SET crm = ?, rqe = ?, nome_especialidade = ?"
			+ "WHERE cpf_pessoa = ?";
			doctorResult = jdbcTemplate.update(sql, crm, rqe, especialidade, cpf);
		}
		return doctorResult;
	}

	public int deleteDoctor(String cpf) {
		PessoaService pessoaService = new PessoaService(jdbcTemplate);
		int emailResult = pessoaService.deleteEmail(cpf); 
		String sql = "DELETE FROM medico m WHERE p.cpf_pessoa = ?";
		int doctorResult = jdbcTemplate.update(sql, cpf);
		if (doctorResult > 0 && emailResult > 0) {
			return pessoaService.deletePessoa(cpf);
		}
		
		return 0;
	}

}
