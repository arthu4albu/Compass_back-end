package com.compass.uninassau.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.compass.uninassau.entity.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // NÃO usar mais findByNomeAndSenha (vai buscar a imagem e dar erro)
    // List<Usuario> findByNomeAndSenha(String nome, String senha);

    Optional<Usuario> findById(Long id);

    Optional<Usuario> findByEmail(String email);

    // login seguro (NÃO busca a imagem)
    @Query("SELECT u.id FROM Usuario u WHERE u.nome = :nome AND u.senha = :senha")
    Long login(@Param("nome") String nome, @Param("senha") String senha);
}
