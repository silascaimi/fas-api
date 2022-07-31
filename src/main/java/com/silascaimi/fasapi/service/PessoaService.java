package com.silascaimi.fasapi.service;

import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.silascaimi.fasapi.model.Pessoa;
import com.silascaimi.fasapi.repository.PessoaRepository;

@Service
public class PessoaService {

	@Autowired
	PessoaRepository pessoaRepository;
	
	@Autowired
	UtilsService utilsService;

	public Pessoa atualizar(Long codigo, Pessoa pessoa) {
		Pessoa pessoaSalva = buscarPessoaPeloCodigo(codigo);

		BeanUtils.copyProperties(pessoa, pessoaSalva, "codigo");

		pessoaRepository.save(pessoaSalva);
		return pessoaSalva;
	}

	public void atualizarProriedadeAtivo(Long codigo, Boolean ativo) {
		Pessoa pessoaSalva = buscarPessoaPeloCodigo(codigo);

		pessoaSalva.setAtivo(ativo);

		pessoaRepository.save(pessoaSalva);
	}

	public Pessoa atualizarParcial(Long codigo, Map<String, Object> campos) {
		Pessoa pessoaSalva = buscarPessoaPeloCodigo(codigo);
		
		Pessoa pessoaMergeada = utilsService.merge(campos, pessoaSalva, Pessoa.class);
		
		return atualizar(codigo, pessoaMergeada);
	}

	private Pessoa buscarPessoaPeloCodigo(Long codigo) {
		Pessoa pessoaSalva = pessoaRepository.findById(codigo).orElseThrow(() -> new EmptyResultDataAccessException(1));
		return pessoaSalva;
	}
}
