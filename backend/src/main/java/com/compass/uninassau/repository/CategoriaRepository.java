package com.compass.uninassau.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.compass.uninassau.entity.Categoria;


public interface CategoriaRepository extends JpaRepository<Categoria, Long>{
	Optional<Categoria> findByNome(String nome);
	Optional<Categoria> findById(Long id);
}
