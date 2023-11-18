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
	
	public int createPatient(String name, String nome_social, String cpf, String rg, String celular,String residencial,String email,
							  String cidade, String bairro, String rua, String numero, String dt_nascimento, String sexo,String convenio, 
							  String profissao, String indicacao, String imc,String cintura,String peso,String altura, String alergias, String pressao) {
		PessoaService pessoaService = new PessoaService(jdbcTemplate);
		int pessoaResult = pessoaService.createPessoa(cpf, rg, name, dt_nascimento, sexo, residencial, celular, cidade, bairro, rua, numero);
		int emailResult = pessoaService.createEmail(cpf, email);
		int patientResult = 0;
		if (pessoaResult > 0 && emailResult > 0) {
			String sql = "INSERT INTO paciente VALUES(?, ?, ?, ?, ?, ?)";
			patientResult = jdbcTemplate.update(sql, alergias, nome_social, indicacao, convenio, profissao, cpf);
		}
		if (imc != null || cintura != null || peso != null || altura != null){
			createNewDadosDoPaciente(imc, cintura, peso, altura, pressao, cpf);
		} 
		return patientResult;
	}

	public int createNewDadosDoPaciente(String imc_string,String cintura_string ,String peso_string,String altura_string,
			String pressao_string, String cpf){
		LocalDate currentDate = LocalDate.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); 
		String today = currentDate.format(formatter);
		Float imc = null;
		Float cintura = null;
		Float peso = null;
		Float altura = null;
		Float pressao = null;
		try{
			imc = Float.parseFloat(imc_string); 
		} catch (Exception e){
		}
		try{
			cintura = Float.parseFloat(cintura_string); 
		} catch (Exception e){
		}
		try{
			peso = Float.parseFloat(peso_string); 
		} catch (Exception e){
		}
		try{
			altura = Float.parseFloat(altura_string); 
		} catch (Exception e){
		}
		try{
			pressao = Float.parseFloat(pressao_string); 
		} catch (Exception e){
		}
		String sql = "INSERT INTO dados_do_paciente VALUES(?, ?, ?, ? ,?, ?, ?)";
		return jdbcTemplate.update(sql, today, imc, cintura, peso, altura, pressao, cpf);
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
	
	public int updatePaciente(String name, String nome_social, String cpf, String rg, String celular,String residencial,String email,
							  String cidade, String bairro, String rua, String numero, String dt_nascimento, String sexo,String convenio, 
							  String profissao, String indicacao, String imc,String cintura,String peso,String altura, String alergias, String pressao) {
		PessoaService pessoaService = new PessoaService(jdbcTemplate);
		int pessoaResult = pessoaService.updatePessoa(cpf, rg, name, dt_nascimento, sexo, residencial, celular, cidade, bairro, rua, numero);
		pessoaService.createEmail(cpf, email);
		int patientResult = 0;
		if (pessoaResult > 0) {
			String sql = "UPDATE paciente "
			+ "SET alergias = ?, nome_social = ?, quem_indicou = ?, convenio = ?, profissao = ? "
			+ "WHERE cpf_pessoa = ?";
			patientResult = jdbcTemplate.update(sql, alergias, nome_social, indicacao, convenio, profissao, cpf);
		}
		if (imc != null || cintura != null || peso != null || altura != null){
			createNewDadosDoPaciente(imc, cintura, peso, altura, pressao, cpf);
		} 
		return patientResult;
	}
	
	public int deletePatient(String cpf) {
		PessoaService pessoaService = new PessoaService(jdbcTemplate);
		AppointmentService appointmentService = new AppointmentService(jdbcTemplate);
		int emailResult = pessoaService.deleteEmail(cpf); 
		int dadosResult = deleteDadosDoPaciente(cpf);
		String sql = "DELETE FROM paciente p WHERE p.cpf_pessoa = ?";
		appointmentService.removePatientFromAppointments(cpf);
		int patientResult = jdbcTemplate.update(sql, cpf);
		if (patientResult > 0 && emailResult > 0 && dadosResult > 0) {
			return pessoaService.deletePessoa(cpf);
		}
		
		return 0;
	}

	public int deleteDadosDoPaciente(String cpf){
		String sql = "DELETE FROM dados_do_paciente WHERE cpf_paciente = ?";

		return jdbcTemplate.update(sql, cpf);
	}

}