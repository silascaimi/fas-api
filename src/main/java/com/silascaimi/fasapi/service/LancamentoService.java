package com.silascaimi.fasapi.service;

import java.util.Optional;

import org.springframework.beans.BeanUtils;
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
	private PessoaRepository pessoaRepositoy;
	
	@Autowired
	private LancamentoRepository lancamentoRepository;

	public Lancamento salvar(Lancamento lancamento) {
		Optional<Pessoa> pessoa = pessoaRepositoy.findById(lancamento.getPessoa().getCodigo());
		
		if(!pessoa.isPresent() || pessoa.get().isInativo()) {
			throw new PessoaInexistenteOuInativaException();
		}
		return lancamentoRepository.save(lancamento);
	}

	public Lancamento atualizar(Long codigo, Lancamento lancamento) {
		Lancamento lancamentoSalvo = lancamentoRepository.findById(codigo)
				.orElseThrow(() -> new IllegalArgumentException());
		if(!lancamento.getPessoa().equals(lancamentoSalvo.getPessoa())) {
			validarPessoa(lancamento.getPessoa());
		}
		
		BeanUtils.copyProperties(lancamento, lancamentoSalvo, "codigo");
		
		return lancamentoRepository.save(lancamentoSalvo);
	}

	private void validarPessoa(Pessoa pessoa) {
		Optional<Pessoa> pessoaSalva = null;
		
		if(pessoa.getCodigo() != null) {
			pessoaSalva = pessoaRepositoy.findById(pessoa.getCodigo());
		}
		
		if (pessoaSalva.isEmpty() || pessoaSalva.get().isInativo()) {
			throw new PessoaInexistenteOuInativaException();
		}
	}

}
