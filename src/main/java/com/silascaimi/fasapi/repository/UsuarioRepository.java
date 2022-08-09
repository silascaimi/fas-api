package com.silascaimi.fasapi.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.silascaimi.fasapi.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

	public Optional<Usuario> findByEmail(String email);
}
