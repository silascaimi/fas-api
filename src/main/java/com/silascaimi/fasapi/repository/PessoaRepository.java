package com.silascaimi.fasapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.silascaimi.fasapi.model.Pessoa;

public interface PessoaRepository extends JpaRepository<Pessoa, Long> {

}
