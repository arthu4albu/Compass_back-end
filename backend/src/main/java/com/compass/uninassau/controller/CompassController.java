package com.compass.uninassau.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.compass.uninassau.entity.Conta;
import com.compass.uninassau.entity.Usuario;
import com.compass.uninassau.repository.ContaRepository;
import com.compass.uninassau.repository.UsuarioRepository;

@CrossOrigin(origins = "http://localhost:8081") // necessário para o cors
@RestController
public class CompassController {
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private ContaRepository contaRepository;
	
	// CRUD do Usuário
	@PostMapping("/cadastrar_usuario")
	public String cadastarUsuario(@RequestBody Usuario usuario) {
		usuarioRepository.save(usuario);
		
		Usuario usuarioFromTable = usuarioRepository.findByNomeAndSenha(usuario.getNome(), usuario.getSenha()).get(0);
		Conta conta = new Conta(usuario.getNome(), 0.00, usuarioFromTable);
		
		contaRepository.save(conta);
		return "usuário salvo e conta criada";
	}
	
	@GetMapping("/usuarios") 
	public List<Usuario> getUsuariosList() {
		return usuarioRepository.findAll();
	}
	
	@GetMapping("/usuario/{idUsuario}") 
	public Usuario getUsuario(@PathVariable Long idUsuario) {
		Usuario usuario = usuarioRepository.findById(idUsuario).get();
		
		return usuario;
	}
	
	@PutMapping("/atualizar_usuario/{idUsuario}")
	public String atualizarUsuario(@PathVariable Long idUsuario, @RequestBody Usuario usuario) {
		Optional<Usuario> usuarioOpt = usuarioRepository.findById(idUsuario);
		Usuario usuarioFromTable = usuarioOpt.get();
		
		usuarioFromTable.setNome(usuario.getNome());
		usuarioFromTable.setEmail(usuario.getEmail());
		usuarioFromTable.setSenha(usuario.getSenha());
		
		usuarioRepository.save(usuarioFromTable);
		
		return "Usuario atualizado com sucesso";
	}
	
	@DeleteMapping("/deletar_usuario/{idUsuario}")
	public String deletarUsuario(@PathVariable Long idUsuario) {
		Optional<Usuario> usuarioOpt = usuarioRepository.findById(idUsuario);
		Usuario usuarioFromTable = usuarioOpt.get();
		
		Optional<Conta> contaOpt = contaRepository.findByUsuario(usuarioFromTable);
		Conta conta = contaOpt.get();
		
		
		contaRepository.delete(conta);
		usuarioRepository.delete(usuarioFromTable);
		
		return "Usuario deletado com sucesso";
	}
	
	@GetMapping("/verificar_usuario")
	public Boolean getUsuario(@RequestParam String nome, @RequestParam String senha) {
		Boolean isUsuario = verifyUser(nome, senha);
		
		
		return isUsuario;
	}
	
	// CRUD da Conta
	@PostMapping("/cadastrar_conta/{idUsuario}")
	public ResponseEntity<String> salvarRenda(@RequestBody Conta conta, @PathVariable Long idUsuario){
		Optional<Usuario> usuarioOpt = usuarioRepository.findById(idUsuario);
		
		if (usuarioOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Usuário não encontrado.");
        }
		
		conta.setUsuario(usuarioOpt.get());
        contaRepository.save(conta);

		return ResponseEntity.ok("Conta criada com sucesso!");
	}
	
	@PutMapping("/atualizar_conta/{idConta}") 
	public String atualizarRenda(@PathVariable long idConta, @RequestBody Conta conta) {
		Optional<Conta> contaOpt = contaRepository.findById(idConta);
		Conta contaFromTable = contaOpt.get();
		
		contaFromTable.setNome(conta.getNome());
		contaFromTable.setRenda(conta.getRenda());
		
		contaRepository.save(contaFromTable);
		
		return "Conta atualizada";
	}
	
	@GetMapping("/conta/{idConta}")
	public Conta getConta(@PathVariable Long idConta) {
		Optional<Conta> contaOpt = contaRepository.findById(idConta);
		Conta contaFromTable = contaOpt.get();
		
		return contaFromTable;
	}
	
	@GetMapping("/contas")
	public List<Conta> getContas() {
		List<Conta> contas = contaRepository.findAll();
		
		return contas;
	}
	
	@DeleteMapping("/deletar_conta/{idConta}")
	public String deletarConta(@PathVariable Long idConta) {
		Optional<Conta> contaOpt = contaRepository.findById(idConta);
		Conta contaFromTable = contaOpt.get();
		
		contaRepository.delete(contaFromTable);
		
		return "Conta deletada com sucesso";
	}
	
	
	public Boolean verifyUser(String nome, String senha) {
		List<Usuario> usuario = usuarioRepository.findByNomeAndSenha(nome, senha);
		
		return !usuario.isEmpty();
	}
}