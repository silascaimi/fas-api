package com.silascaimi.fasapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.silascaimi.fasapi.model.Categoria;
import com.silascaimi.fasapi.repository.filter.CategoriaFilter;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

	@Query("SELECT c FROM Categoria c WHERE c.nome LIKE CONCAT('%', COALESCE(:#{#filter.nome}, ''), '%')")
    List<Categoria> pesquisar(@Param("filter") CategoriaFilter categoriaFilter);
}
