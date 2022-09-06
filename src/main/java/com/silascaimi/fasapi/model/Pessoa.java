package com.silascaimi.fasapi.model;

import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.silascaimi.fasapi.validation.Group;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Entity
@Table(name = "pessoa")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Pessoa {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@NotNull(groups = Group.Id.class)
	private Long codigo;
	
	@NotNull
	@Size(max = 50, message = "{validation.maxSize}")
	private String nome;
	
	@NotNull
	private Boolean ativo;

	@Valid
	@Embedded
	private Endereco endereco;
	
	@JsonIgnore
	@OneToMany(mappedBy = "pessoa")
	private List<Lancamento> lancamentos;

	@JsonIgnoreProperties("pessoa") // ignora a propriedade pessoa dentro de contato e evita stackoverflow
	@Valid
	@OneToMany(mappedBy = "pessoa", cascade = CascadeType.ALL, // alteraçoes em pessoa refletem em contatos
			orphanRemoval = true) // tudo que tiver na base e não tiver na lista contida no corpo ao atualizar será removido
	private List<Contato> contatos;

	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Boolean isAtivo() {
		return ativo;
	}

	@JsonIgnore // Faz o Jackson ignorar na serialização do json
	// @Transient // Faz o hibernate ignorar propriedade quando usado mapeamento
	// Property Access Mode (pelos getters)
	public Boolean isInativo() {
		log.info("Status {}", ativo);
		return !ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

	public List<Lancamento> getLancamentos() {
		return lancamentos;
	}

	public void setLancamentos(List<Lancamento> lancamentos) {
		this.lancamentos = lancamentos;
	}

	public List<Contato> getContatos() {
		return contatos;
	}

	public void setContatos(List<Contato> contatos) {
		this.contatos = contatos;
	}

	@Override
	public int hashCode() {
		return Objects.hash(codigo);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Pessoa other = (Pessoa) obj;
		return Objects.equals(codigo, other.codigo);
	}

}
