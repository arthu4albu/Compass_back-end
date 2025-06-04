package com.compass.uninassau.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.compass.uninassau.entity.Usuario;

public interface CompassRepository extends JpaRepository<Usuario, Long> {
	List<Usuario> findByNomeAndSenha(String nome, String senha);
}