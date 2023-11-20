create database iep;
use iep;

create table Pessoa (
cpf varchar(20) primary key check (cpf like '___.___.___-__'),
rg varchar(20) not null unique check (rg like '_.___.___'),
nome varchar(100),
dt_nascimento date,
sexo char,
tel_residencial varchar(25),
tel_celular varchar(25),
foto varchar(1024),
cidade varchar(100),
bairro varchar(100),
rua varchar(100),
numero varchar(100)
);


create table Email(
email varchar(100) primary key check (email like '%@%.%'),
cpf_pessoa varchar(20),

constraint fk_pessoa_cpf foreign key(cpf_pessoa) references Pessoa(cpf) on delete cascade
);

create table Paciente(
alergias varchar(255),
nome_social varchar(100),
quem_indicou varchar(100),
convenio varchar(100),
profissao varchar(100),
cpf_pessoa varchar(20),

constraint paciente_cpf_pessoa_fk foreign key(cpf_pessoa) references Pessoa(cpf) on delete cascade
);

create table Responsavel(
cpf_pessoa varchar(20),
cpf_paciente varchar(20),

constraint responsavel_cpf_pessoa_fk foreign key (cpf_pessoa) references Pessoa(cpf) on delete cascade,
constraint responsavel_cpf_paciente_fk foreign key (cpf_paciente) references Paciente (cpf_pessoa)
);

create table Servicos_Gerais(
cargo varchar(127),
cpf_pessoa varchar(20),
cpf_supervisor varchar(20) null,

constraint servicosgerais_cpf_pessoa_fk foreign key (cpf_pessoa) references Pessoa(cpf) on delete cascade,
constraint servicosgerais_cpf_supervisor_fk foreign key (cpf_supervisor) references Servicos_Gerais(cpf_pessoa)
);

create table Nutricionista(
cpf_pessoa varchar(20),
crn varchar(20) not null,

constraint nutricionista_cpf_pessoa_fk foreign key (cpf_pessoa) references Pessoa(cpf) on delete cascade
);

create table Medico(
cpf_pessoa varchar(20),
crm varchar(20) not null,
nome_especialidade varchar(255),
rqe varchar(20),

constraint medico_cpf_pessoa_fk foreign key (cpf_pessoa) references Pessoa(cpf) on delete cascade
);

create table Agendamento(
dt_agendamento date,
descricao varchar(255),
cpf_paciente varchar(20),

constraint agendamento_cpf_paciente_fk foreign key(cpf_paciente) references Paciente(cpf_pessoa) on delete cascade,
primary key (dt_agendamento, cpf_paciente)
);

DELIMITER //
CREATE TRIGGER check_agendamento_date
BEFORE INSERT ON Agendamento
FOR EACH ROW
BEGIN
  IF NEW.dt_agendamento < CURDATE() THEN
    SIGNAL SQLSTATE '45000'
    SET MESSAGE_TEXT = 'Invalid date for Agendamento';
  END IF;
END;
//
DELIMITER ;

create table Dados_Do_Paciente(
dt_atualizacao date ,
IMC float default 0.0,
cintura_abdominal float default 0.0,
altura float default 0.0,
peso float default 0.0,
pressao float default 0.0,
cpf_paciente varchar(20),

constraint dados_paciente_cpf_fk foreign key (cpf_paciente) references Paciente(cpf_pessoa) on delete cascade
); 	

create table Consulta(
id int auto_increment primary key,
dt_consulta date,
confirmada bool default false,
historia_clinica varchar(512),
CID varchar(100),
descricao varchar(255),
cpf_paciente varchar(20),
cpf_medico varchar(20) null default null,
cpf_nutricionista varchar(20) null default null,

constraint consulta_paciente_cpf_fk foreign key (cpf_paciente) references Paciente(cpf_pessoa) on delete set null,
constraint consulta_medico_cpf_fk foreign key (cpf_medico) references Medico(cpf_pessoa) on delete set null,
constraint consulta_nutricionista_cpf_fk foreign key (cpf_nutricionista) references Nutricionista(cpf_pessoa) on delete set null
);

DELIMITER $$
CREATE TRIGGER check_consulta_date
BEFORE INSERT ON Consulta
FOR EACH ROW
BEGIN
  IF NEW.dt_consulta < CURDATE() THEN
    SIGNAL SQLSTATE '45000'
    SET MESSAGE_TEXT = 'Invalid date for Consulta';
  END IF;
END;
$$
DELIMITER ;

create table Exame(
codigo int auto_increment primary key,
dt_final date,
descricao varchar(200),
resultado varchar(200)
);

create table Requisicao(
dt_requisicao date default (current_date),
id_consulta int,
codigo_exame int,

constraint consulta_requisicao_fk foreign key (id_consulta) references Consulta(id) on delete set null,
constraint exame_requisicao_fk foreign key (codigo_exame) references Exame(codigo) on delete cascade
);

create table Atestado(
dt_emissao date,
descricao varchar(200),
id_consulta int,

constraint atestado_consulta_fk foreign key (id_consulta) references Consulta(id) on delete cascade
);

create table Prontuario(
codigo int auto_increment primary key,
primeira_vez bool,
retorno_previsto date,
id_consulta int,

constraint prontuario_consulta_consulta_fk foreign key (id_consulta) references Consulta(id) on delete cascade
);

create table Receita(
codigo int primary key,
posologia varchar(100),
instrucoes varchar(200)
);

create table Emissao(
id_consulta int,
codigo_receita int,

constraint emissao_consulta_consulta_fk foreign key (id_consulta) references Consulta(id) on delete cascade,
constraint receita_emissao_fk foreign key (codigo_receita) references Receita(codigo) on delete cascade
);

create table Remedio(
nome varchar(50),
dosagem varchar(50),

constraint remedio_pk primary key (nome, dosagem)
);

create table Receita_Remedio(
nome_remedio varchar(50),
dosagem_remedio varchar(50),
codigo_receita int,

constraint receita_remedio_fk foreign key (nome_remedio, dosagem_remedio) references Remedio(nome, dosagem) on delete cascade,
constraint receita_fk foreign key (codigo_receita) references Receita(codigo) on delete cascade
);


# povoamento
insert into Pessoa values("098.035.454-42", "9.624.347", "Henrique Cordeiro", "2004-05-28", "M", null, "81 9 87594540", null, "Recife", "Boa Viagem", "Francisco da Cunha", "142"),
									   ("123.456.789-10", "1.123.123", "João da Silva", "1997-08-11", "M", null, "81 9 12344321", null, "Recife", "Recife Antigo", "Cais do Apolo", "55"),
									   ("098.765.432-21", "0.987.654", "Maria das Dores", "1900-12-21", "F", "81 34332323", "81 9 99019-1221", null, "Jaboatão dos Guararapes", "Piedade", "Vinicius de Moraes", "22"),
									   ("019.192.124-22", "1.123.294", "Luiza Omena", "2004-03-05", "F", null, "81 996821711", null, "Recife", "Poço da Panela", "Antonio Vitruvio", "49");
									   
insert into Email values ("henrique@gmail.com", "098.035.454-42"),
									 ("joao@gmail.com", "123.456.789-10"),
									 ("maria@gmail.com", "098.765.432-21"),
									 ("luiza@gmail.com", "019.192.124-22");
									  
insert into Paciente values("Poeira, Desodorante, Abelha", null, "Dra. Lúcia Cordeiro", "SAFRA", "Estagiária de Desenvolvimento", "019.192.124-22"),
										 (null, "Sandra Mattos", "Dra. Lúcia Cordeiro", "Sulamerica", "Estagiário de Desenvolvimento", "098.035.454-42");
										 
insert into Medico values("123.456.789-10", "12312","Endocrinologista", "12321321"),
									  ("098.765.432-21", "98042", "Oftalmologista", "10990129");
									  