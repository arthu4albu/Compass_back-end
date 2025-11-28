package com.compass.uninassau.DTOs;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.compass.uninassau.entity.Usuario;
import com.compass.uninassau.repository.UsuarioRepository;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/senha")
public class SenhaController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Resetar senha direto pelo email
    @PostMapping("/reset")
    public ResponseEntity<String> resetarSenha(@RequestBody ResetWrapper body) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(body.getEmail());

        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Usuário não encontrado.");
        }

        Usuario usuario = usuarioOpt.get();
        usuario.setSenha(body.getNovaSenha());
        usuarioRepository.save(usuario);

        return ResponseEntity.ok("Senha atualizada com sucesso!");
    }

    // Wrapper simples para receber email + nova senha
    static class ResetWrapper {
        private String email;
        private String novaSenha;

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public String getNovaSenha() { return novaSenha; }
        public void setNovaSenha(String novaSenha) { this.novaSenha = novaSenha; }
    }
}
