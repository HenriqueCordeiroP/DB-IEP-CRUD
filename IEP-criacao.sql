use iep;

create table Pessoa (
cpf varchar(20) primary key,
RG varchar(20) not null unique,
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

alter table pessoa
add constraint cpf_pessoa_ck check (cpf like "___.___.___-__");

create table Email(
email varchar(100) primary key,
cpf_pessoa varchar(20),

alter table email
add constraint email_email_ck check (email like "%@%.%");

constraint fk_pessoa_cpf foreign key(cpf_pessoa) references Pessoa(cpf)
);

create table Paciente(
alergias varchar(255),
nome_social varchar(100),
quem_indicou varchar(100),
convenio varchar(100),
profissao varchar(100),
cpf_pessoa varchar(20),

constraint paciente_cpf_pessoa_fk foreign key(cpf_pessoa) references Pessoa(cpf)
);

create table Responsavel(
cpf_pessoa varchar(20),
cpf_paciente varchar(20),

constraint responsavel_cpf_pessoa_fk foreign key (cpf_pessoa) references Pessoa(cpf),
constraint responsavel_cpf_paciente_fk foreign key (cpf_paciente) references Paciente (cpf_pessoa)
);

create table Servicos_Gerais(
cargo varchar(127),
cpf_pessoa varchar(20),
cpf_supervisor varchar(20) null,

constraint servicosgerais_cpf_pessoa_fk foreign key (cpf_pessoa) references Pessoa(cpf),
constraint servicosgerais_cpf_supervisor_fk foreign key (cpf_supervisor) references Servicos_Gerais(cpf_pessoa)
);

create table Nutricionista(
cpf_pessoa varchar(20),
crn varchar(20) not null,

constraint nutricionista_cpf_pessoa_fk foreign key (cpf_pessoa) references Pessoa(cpf)
);

create table Medico(
cpf_pessoa varchar(20),
crm varchar(20) not null,
nome_especialidade varchar(255),
rqe varchar(20),

constraint medico_cpf_pessoa_fk foreign key (cpf_pessoa) references Pessoa(cpf)
);

create table Agendamento(
dt_agendamento date not null,
descricao varchar(255),
cpf_paciente varchar(20),

constraint agendamento_cpf_paciente_fk foreign key(cpf_paciente) references Paciente(cpf_pessoa)
);

alter table Agendamento
add constraint agendamento_pk primary key (dt_agendamento, cpf_paciente);

create table Dados_Do_Paciente(
dt_atualizacao date default (current_date),
IMC float default 0.0,
cintura_abdominal float default 0.0,
altura float default 0.0,
peso float default 0.0,
pressao float default 0.0,
cpf_paciente varchar(20),

constraint dados_paciente_cpf_fk foreign key (cpf_paciente) references Paciente(cpf_pessoa)
); 	

create table Consulta(
dt_consulta date,
confirmada bool default false,
historia_clinica varchar(512),
CID varchar(100),
descricao varchar(255),
cpf_paciente varchar(20),
cpf_medico varchar(20) null default null,
cpf_nutricionista varchar(20) null default null,

constraint consulta_pk unique (dt_consulta, cpf_paciente, cpf_medico, cpf_nutricionista),
constraint consulta_paciente_cpf_fk foreign key (cpf_paciente) references Paciente(cpf_pessoa),
constraint consulta_medico_cpf_fk foreign key (cpf_medico) references Medico(cpf_pessoa),
constraint consulta_nutricionista_cpf_fk foreign key (cpf_nutricionista) references Nutricionista(cpf_pessoa)
);

create table Exame(
codigo int auto_increment primary key,
dt_final date,
descricao varchar(200),
resultado varchar(200)
);

create table Requisicao(
dt_requisicao date default (current_date),
dt_consulta date,
cpf_paciente varchar(20),
codigo_exame int,

constraint consulta_requisicao_fk foreign key (dt_consulta, cpf_paciente) references Consulta(dt_consulta, cpf_paciente),
constraint exame_requisicao_fk foreign key (codigo_exame) references Exame(codigo)
);

create table Atestado(
dt_emissao date,
descricao varchar(200),
dt_consulta date,
cpf_paciente varchar(20),

constraint atestado_consulta_dt_consulta_fk foreign key (dt_consulta, cpf_paciente) references Consulta(dt_consulta, cpf_paciente)
);

create table Prontuario(
codigo int auto_increment primary key,
primeira_vez bool,
retorno_previsto date,
dt_consulta date,
cpf_paciente varchar(20),

constraint prontuario_consulta_dt_consulta_fk foreign key (dt_consulta, cpf_paciente) references Consulta(dt_consulta, cpf_paciente)
);

create table Receita(
codigo int primary key,
posologia varchar(100),
instrucoes varchar(200)
);

create table Emissao(
dt_consulta date,
cpf_paciente varchar(20),
codigo_receita int,

constraint emissao_consulta_dt_consulta_fk foreign key (dt_consulta, cpf_paciente) references Consulta(dt_consulta, cpf_paciente),
constraint receita_emissao_fk foreign key (codigo_receita) references Receita(codigo)
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

constraint receita_remedio_fk foreign key (nome_remedio, dosagem_remedio) references Remedio(nome, dosagem),
constraint receita_fk foreign key (codigo_receita) references Receita(codigo)
);

insert into dados_do_paciente values(curdate(), null, 85, null, null, null, "098.035.454-42");
select *
FROM paciente p 
JOIN pessoa pe ON p.cpf_pessoa = pe.cpf 
JOIN email e ON p.cpf_pessoa = e.cpf_pessoa 
LEFT JOIN( 
SELECT * FROM dados_do_paciente ddp 
WHERE ddp.dt_atualizacao <= curdate() 
ORDER BY ddp.dt_atualizacao desc limit 1) ddp ON ddp.cpf_paciente = p.cpf_pessoa 
WHERE cpf = "098.035.454-42";