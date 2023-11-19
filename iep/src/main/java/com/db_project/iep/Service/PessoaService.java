package com.db_project.iep.Service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class PessoaService {
	
	private final JdbcTemplate jdbcTemplate;
	
	@Autowired
	public PessoaService(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	public String createPessoa(Map<String, String> pessoa) {
			String sql = "INSERT INTO pessoa (cpf, rg, nome, dt_nascimento, sexo, tel_residencial," + 
									"tel_celular, cidade, bairro, rua, numero) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
		try{
			jdbcTemplate.update(sql, pessoa.get("cpf"), pessoa.get("rg"), pessoa.get("nome"),
			pessoa.get("data_nascimento"), pessoa.get("sexo"), pessoa.get("residencial"), pessoa.get("celular"), 
			pessoa.get("cidade"), pessoa.get("bairro"), pessoa.get("rua"), pessoa.get("numero"));
		} catch (DuplicateKeyException d){
			if(d.getMessage().contains("cpf")){
				return "Esse CPF já está registrado.";
			} else if (d.getMessage().contains("rg")){
				return "Esse RG já está registrado.";
			}
		}
		return null;
	}
	
	public String createEmail(Map<String, String> pessoa) {
		String sql = "INSERT INTO email VALUES(?, ?)";
		
		try{
			jdbcTemplate.update(sql, pessoa.get("email") , pessoa.get("cpf"));
		} catch (DuplicateKeyException d){
		} catch(Exception e){
			return e.getMessage();
		}
		return null;
	}
	
	public String updatePessoa(Map<String, String> pessoa) {
		String sql = "UPDATE pessoa p "
				   + "SET nome = ?, dt_nascimento = ?, sexo = ?, tel_residencial = ?, tel_celular = ?, "
				   + "cidade = ?, bairro = ?, rua = ?, numero = ? "
				   + "WHERE cpf = ?";
		try {
			jdbcTemplate.update(sql, pessoa.get("nome"), pessoa.get("data_nascimento"), pessoa.get("sexo"),
			 pessoa.get("residencial"), pessoa.get("celular"), pessoa.get("cidade"), pessoa.get("bairro"), 
			 pessoa.get("rua"), pessoa.get("numero"), pessoa.get("cpf"));
		} catch (Exception e){
			return e.getMessage();
		}
		return null;
	}
	
	public int deleteEmail(String cpf) {
		String sql = "DELETE FROM email e WHERE e.cpf_pessoa = ?";
		
		return jdbcTemplate.update(sql, cpf);
	}
	
	public int deletePessoa(String cpf) {
		String sql = "DELETE FROM pessoa p WHERE p.cpf = ?";
		
		return jdbcTemplate.update(sql, cpf);
	}
}
