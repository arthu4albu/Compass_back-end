package com.compass.uninassau.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.compass.uninassau.entity.Conta;
import com.compass.uninassau.entity.Usuario;

@Repository
public interface ContaRepository extends JpaRepository<Conta, Long> {
	Optional<Conta> findById(Long id);
	Optional<Conta> findByUsuario(Usuario usuario);
}