package com.db_project.iep.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class PessoaService {
	
	private final JdbcTemplate jdbcTemplate;
	
	@Autowired
	public PessoaService(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	public int createPessoa(String cpf, String rg, String nome, String dt_nascimento, String sexo, String residencial, String celular,
			String cidade, String bairro, String rua, String numero) {
		// create pessoa
		return 0;
	}
	
	public int createEmail(String cpf, String email) {
		// create email
		return 0;
	}
}
