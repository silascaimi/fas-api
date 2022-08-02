package com.silascaimi.fasapi.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.silascaimi.fasapi.model.Lancamento;
import com.silascaimi.fasapi.model.Pessoa;
import com.silascaimi.fasapi.repository.LancamentoRepository;
import com.silascaimi.fasapi.repository.PessoaRepository;
import com.silascaimi.fasapi.service.exception.PessoaInexistenteOuInativaException;

@Service
public class LancamentoService {
	
	@Autowired
	PessoaRepository pessoaRepositoy;
	
	@Autowired
	LancamentoRepository lancamentoRepository;

	public Lancamento salvar(Lancamento lancamento) {
		Optional<Pessoa> pessoa = pessoaRepositoy.findById(lancamento.getPessoa().getCodigo());
		
		if(!pessoa.isPresent() || pessoa.get().isInativo()) {
			throw new PessoaInexistenteOuInativaException();
		}
		return lancamentoRepository.save(lancamento);
	}

}
