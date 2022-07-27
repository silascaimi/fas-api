CREATE TABLE pessoa (
	codigo BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
	nome VARCHAR(50) NOT NULL,
	logradouro VARCHAR(30),
	numero VARCHAR(30),
	complemento VARCHAR(30),
	bairro VARCHAR(30),
	cep VARCHAR(30),
	cidade VARCHAR(30),
	estado VARCHAR(30),
	ativo BOOLEAN NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO pessoa
	(nome, ativo, logradouro, numero, complemento, bairro, cep, cidade, estado)
values
	('João Felipe Scollari', 1, 'Rua Eduardo Guimarães', '906', '"1440 Aenean Rd', 'Mauris Av.', '67123-411', 'Campinas', 'Minas Gerais');
INSERT INTO pessoa
	(nome, ativo, logradouro, numero, complemento, bairro, cep, cidade, estado)
values
	('Sade Ingra', 1, '278-2132 Mi. Street', '397', 'P.O. Box 253, 928 Pretium Rd.', 'Non, Rd.', '58086-622', 'Juiz de For', 'Goiás');
INSERT INTO pessoa
	(nome, ativo, logradouro, numero, complemento, bairro, cep, cidade, estado)
values
	('Chava Robinson', 1, '405-7279 Est Ave', '519', '933-5890 Vulputate St', 'Mauris Av.', '67123-411', 'Anápolis', 'Paraíba');
INSERT INTO pessoa
	(nome, ativo, logradouro, numero, complemento, bairro, cep, cidade, estado)
values
	('Priscilla Burns', 1, '279-4399 Nulla Rd.', '927', '595-1722 Consectetuer St', 'Nibh St.', '88808-374', 'Belém', 'Ceará');
	