package com.compass.uninassau.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.compass.uninassau.entity.Usuario;
import com.compass.uninassau.repository.CompassRepository;

@CrossOrigin(origins = "http://localhost:8081") // necessário para o cors
@RestController
public class CompassController {
	@Autowired
	private CompassRepository compassRepository;
	
	@PostMapping("/cadastrar")
	public String cadastarUsuario(@RequestBody Usuario usuario) {
		compassRepository.save(usuario);
		System.out.println(usuario);
		return "usuário salvo";
	}
	
	@GetMapping("/usuarios") 
	public List<Usuario> getUsuariosList() {
		return compassRepository.findAll();
	}
	
	@GetMapping("/verificar_usuario")
	public Boolean getUsuario(@RequestParam String nome, @RequestParam String senha) {
		Boolean isUsuario = verifyUser(nome, senha);
		
		
		return isUsuario;
	}
	
	
	public Boolean verifyUser(String nome, String senha) {
		List<Usuario> usuario = compassRepository.findByNomeAndSenha(nome, senha);
		
		return !usuario.isEmpty();
	}
}