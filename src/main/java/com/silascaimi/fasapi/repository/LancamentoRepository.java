package com.silascaimi.fasapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.silascaimi.fasapi.model.Lancamento;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long> {

	 @Query("    select l " +
	           "      from Lancamento l " +
	           "join fetch l.categoria c " +
	           "join fetch l.pessoa p ")
	    List<Lancamento> findAll();
	 
}
