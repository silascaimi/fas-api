package com.silascaimi.fasapi.repository.lancamento;

import java.util.List;

import com.silascaimi.fasapi.model.Lancamento;
import com.silascaimi.fasapi.repository.filter.LancamentoFilter;

public interface LancamentoRepositoryQuery {

	public List<Lancamento> filtrar(LancamentoFilter lancamentoFilter);
}
