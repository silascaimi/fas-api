package com.silascaimi.fasapi.repository.lancamento;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.silascaimi.fasapi.dto.LancamentoEstatisticaCategoria;
import com.silascaimi.fasapi.dto.LancamentoEstatisticaDia;
import com.silascaimi.fasapi.dto.LancamentoEstatisticaPessoa;
import com.silascaimi.fasapi.model.Lancamento;
import com.silascaimi.fasapi.repository.filter.LancamentoFilter;

public interface LancamentoRepositoryQuery {

	public Page<Lancamento> filtrar(LancamentoFilter lancamentoFilter, Pageable pageable);
	
	public List<LancamentoEstatisticaCategoria> porCategoria(LocalDate mesReferencia);
	
	public List<LancamentoEstatisticaDia> porDia(LocalDate mesReferencia);
	
	public List<LancamentoEstatisticaPessoa> porPessoa(LocalDate inicio, LocalDate fim);
}
