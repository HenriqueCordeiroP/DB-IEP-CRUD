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
	
	public int createAppointment(String data, String descricao, String confirmada, String historia_clinica, String CID, String cpf) {
		
	        String sql = "INSERT INTO agendamento (dt_agendamento, descricao, confirmada, historia_clinica, CID, cpf_paciente) VALUES (?,?,?,?,?,?)";
		
			int AppointmentResult = jdbcTemplate.update(sql, data, descricao, confirmada, historia_clinica, CID, cpf);

		return AppointmentResult;
	}
	
	public Map<String, Object> getAppointmentByCPF(String cpf) {
		String sql = "SELECT * " + 
		"FROM consulta c " +
		"JOIN pessoa pe ON c.cpf_pessoa = pe.cpf " +
		"JOIN email e ON c.cpf_pessoa = e.cpf_pessoa " + 
		"LEFT JOIN( " +
		"SELECT * FROM dados_do_paciente ddp " +
		"WHERE ddp.dt_atualizacao <= curdate() " +
		"ORDER BY ddp.dt_atualizacao DESC LIMIT 1) ddp ON ddp.cpf_paciente = c.cpf_pessoa "+
		"WHERE cpf = ?";
		return jdbcTemplate.queryForMap(sql, cpf);
	}
	
	public int updateAppointment(String data, String descricao, String confirmada, String historia_clinica, String CID, String cpf) {
		int appointmentResult = createAppointment(data, descricao, confirmada, historia_clinica, CID, cpf);
		if (appointmentResult > 0) {
			String sql = "UPDATE consulta "
			+ "SET data = ?, confirmada = ?, historia_clinica = ?, CID = ? "
			+ "WHERE cpf_pessoa = ?";
			appointmentResult = jdbcTemplate.update(sql, data, descricao, confirmada, historia_clinica, CID, cpf);
		}
		return appointmentResult;
	}
	
	public int deleteAppointment(String cpf) {
		String sql = "DELETE FROM agendamento WHERE cpf_paciente =?";
		
		return jdbcTemplate.update(sql, cpf);
	}

	public int deleteDadosDoAppointment(String cpf){
		String sql = "DELETE FROM dados_do_paciente WHERE cpf_paciente = ?";

		return jdbcTemplate.update(sql, cpf);
	}

}