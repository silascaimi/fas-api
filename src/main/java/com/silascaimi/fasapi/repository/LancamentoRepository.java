package com.silascaimi.fasapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.silascaimi.fasapi.model.Lancamento;
import com.silascaimi.fasapi.repository.lancamento.LancamentoRepositoryQuery;

@Repository
public interface LancamentoRepository extends JpaRepository<Lancamento, Long>, LancamentoRepositoryQuery {

	 @Query("    select l " +
	           "      from Lancamento l " +
	           "join fetch l.categoria c " +
	           "join fetch l.pessoa p ")
	    List<Lancamento> findAll();
	 
}
