package com.db_project.iep.Service;
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

	public Map<String, Object> getDoctorByCPF(String cpf) {
		String sql = "SELECT * " + 
		"FROM medico m " +
		"JOIN pessoa pe ON m.cpf_pessoa = pe.cpf " +
		"JOIN email e ON m.cpf_pessoa = e.cpf_pessoa " + 
		"WHERE cpf = ?";
		return jdbcTemplate.queryForMap(sql, cpf);
	}

	public String createDoctor(Map<String, String> doctor) {
		PessoaService pessoaService = new PessoaService(jdbcTemplate);

		String pessoaResult = pessoaService.createPessoa(doctor);
		
		String emailResult = pessoaService.createEmail(doctor);
		if (pessoaResult == null && emailResult == null) {
			String sql = "INSERT INTO medico VALUES (?, ?, ?, ?)";
			try{
				jdbcTemplate.update(sql, doctor.get("cpf"), doctor.get("crm"), doctor.get("especialidade"), doctor.get("rqe"));
			} catch (Exception e){
				return e.getMessage();
			}
		} else {
			return pessoaResult;
		}
		return null;
	}

	public String updateDoctor(Map<String, String> doctor) {
		PessoaService pessoaService = new PessoaService(jdbcTemplate);

		String pessoaResult = pessoaService.updatePessoa(doctor);
		
		pessoaService.createEmail(doctor);
		if (pessoaResult == null) {
			String sql = "UPDATE medico "
			+ "SET crm = ?, rqe = ?, nome_especialidade = ?"
			+ "WHERE cpf_pessoa = ?";
			try{
				jdbcTemplate.update(sql, doctor.get("crm"), doctor.get("rqe"), doctor.get("especialidade"), doctor.get("cpf"));
			} catch (Exception e ){
				return e.getMessage();
			}
		}
		return null;
	}

	public int deleteDoctor(String cpf) {
		PessoaService pessoaService = new PessoaService(jdbcTemplate);
		return pessoaService.deletePessoa(cpf);
	}

}