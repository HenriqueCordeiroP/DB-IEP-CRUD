package com.db_project.iep.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
		return jdbcTemplate.queryForList("SELECT COALESCE(p.nome_social, pe.nome) AS nome, e.email, pe.tel_celular, p.cpf_pessoa, "
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
	
	public String createPatient(Map<String, String> patient) {
		PessoaService pessoaService = new PessoaService(jdbcTemplate);
		String pessoaResult = pessoaService.createPessoa(patient);
		String emailResult = pessoaService.createEmail(patient);
		if (pessoaResult == null && emailResult == null) {
			String sql = "INSERT INTO paciente VALUES(?, ?, ?, ?, ?, ?)";
			try{
				jdbcTemplate.update(sql, patient.get("alergias"), patient.get("nome_social"),
				patient.get("quem_indicou"), patient.get("convenio"), patient.get("profissao"), patient.get("cpf"));
			} catch(Exception e){
				return e.getMessage();
			}
		} else{
			jdbcTemplate.update("ROLLBACK");
			return pessoaResult != null ? pessoaResult : emailResult;
		}
		if (patient.get("imc") != null || patient.get("cintura") != null || patient.get("peso") != null || patient.get("altura") != null){
			createNewDadosDoPaciente(patient);
		} 
		return null;
	}

	public int createNewDadosDoPaciente(Map<String, String> patient){
		LocalDate currentDate = LocalDate.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); 
		String today = currentDate.format(formatter);
		Float imc = null;
		Float cintura = null;
		Float peso = null;
		Float altura = null;
		Float pressao = null;
		try{
			imc = Float.parseFloat(patient.get("imc")); 
		} catch (Exception e){
		}
		try{
			cintura = Float.parseFloat(patient.get("cintura")); 
		} catch (Exception e){
		}
		try{
			peso = Float.parseFloat(patient.get("peso")); 
		} catch (Exception e){
		}
		try{
			altura = Float.parseFloat(patient.get("altura")); 
		} catch (Exception e){
		}
		try{
			pressao = Float.parseFloat(patient.get("pressao")); 
		} catch (Exception e){
		}
		String sql = "INSERT INTO dados_do_paciente VALUES(?, ?, ?, ? ,?, ?, ?)";
		try{
			jdbcTemplate.update(sql, today, imc, cintura, altura, peso, pressao, patient.get("cpf"));
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
		return 1;
	}
	
	public Map<String, Object> getPacienteByCPF(String cpf) {
		String sql = "SELECT * " + 
		"FROM paciente p " +
		"JOIN pessoa pe ON p.cpf_pessoa = pe.cpf " +
		"JOIN email e ON p.cpf_pessoa = e.cpf_pessoa " + 
		"LEFT JOIN( " +
		"SELECT * FROM dados_do_paciente ddp " +
		"WHERE ddp.dt_atualizacao <= curdate() " +
		"ORDER BY ddp.dt_atualizacao DESC LIMIT 1) ddp ON ddp.cpf_paciente = p.cpf_pessoa "+
		"WHERE cpf = ?";
		return jdbcTemplate.queryForMap(sql, cpf);
	}
	
	public String updatePaciente(Map<String, String> patient) {
		PessoaService pessoaService = new PessoaService(jdbcTemplate);
		String pessoaResult = pessoaService.updatePessoa(patient);
		String emailResult = pessoaService.createEmail(patient);
		if (pessoaResult == null && emailResult == null) {
			String sql = "UPDATE paciente "
			+ "SET alergias = ?, nome_social = ?, quem_indicou = ?, convenio = ?, profissao = ? "
			+ "WHERE cpf_pessoa = ?";
			try{
				jdbcTemplate.update(sql, patient.get("alergias"), patient.get("nome_social"),
				patient.get("quem_indicou"), patient.get("convenio"), patient.get("profissao"), patient.get("cpf"));
			} catch(Exception e){
				return e.getMessage();
			}
		} else {
			return pessoaResult != null ? pessoaResult : emailResult;
		}
		if (patient.get("imc") != null || patient.get("cintura") != null || patient.get("peso") != null || patient.get("altura") != null){
			createNewDadosDoPaciente(patient);
		} 
		return null;
	}
	
	public int deletePatient(String cpf) {
		PessoaService pessoaService = new PessoaService(jdbcTemplate);
		pessoaService.deletePessoa(cpf);		
		return 0;
	}

	public int deleteDadosDoPaciente(String cpf){
		String sql = "DELETE FROM dados_do_paciente WHERE cpf_paciente = ?";

		return jdbcTemplate.update(sql, cpf);
	}

	public List<Map<String, Object>> filterPatients(Map<String, String> patientMap){
		String patient_str = patientMap.get("search") + '%';

		String sql = "SELECT COALESCE(p.nome_social, pe.nome) AS nome, e.email, pe.tel_celular, p.cpf_pessoa, "
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
				+ "WHERE nome LIKE ?"
				+ "GROUP BY COALESCE(p.nome_social, pe.nome), e.email, pe.tel_celular "
				+ "ORDER BY nome ASC";

	return jdbcTemplate.queryForList(sql, patient_str);
	}

}	