package com.compass.uninassau.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.compass.uninassau.entity.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
	List<Usuario> findByNomeAndSenha(String nome, String senha);
	Optional<Usuario> findById(Long id);
    Optional<Usuario> findByEmail(String email);
}
