package com.compass.uninassau.controller;

import java.util.ArrayList;
import java.util.Date;
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

import com.compass.uninassau.entity.Categoria;
import com.compass.uninassau.entity.Conta;
import com.compass.uninassau.entity.Movimento;
import com.compass.uninassau.entity.Usuario;
import com.compass.uninassau.repository.CategoriaRepository;
import com.compass.uninassau.repository.ContaRepository;
import com.compass.uninassau.repository.MovimentoRepository;
import com.compass.uninassau.repository.UsuarioRepository;

@CrossOrigin(origins = "http://localhost:8081") // necessário para o cors
@RestController
public class CompassController {

	@Autowired
    private CategoriaRepository categoriaRepository;
	
	@Autowired
    private MovimentoRepository movimentoRepository;
    
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
		List<Usuario> usuarios = usuarioRepository.findAll();
		
		
		return usuarios;
	}
	
	@GetMapping("/usuario/{idUsuario}") 
	public UsuarioDTO getUsuario(@PathVariable Long idUsuario) {
		Usuario usuario = usuarioRepository.findById(idUsuario).get();
		
		return new UsuarioDTO(usuario.getNome(), usuario.getEmail());
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
	
	@PostMapping("/verificar_usuario")
	public Long getUsuario(@RequestBody WrapperNomeSenha wrapper) {
		String nome = wrapper.getNome();
		String senha = wrapper.getSenha();
		
		Long usuarioId = verifyUser(nome, senha).getId();
		
		return usuarioId;
	}
	
	public Usuario verifyUser(String nome, String senha) {
		List<Usuario> usuarioList = usuarioRepository.findByNomeAndSenha(nome, senha);
		Usuario usuario = usuarioList.get(0);
		
		return usuario;
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
	public ContaDTO getConta(@PathVariable Long idConta) {
		Optional<Conta> contaOpt = contaRepository.findById(idConta);
		Conta contaFromTable = contaOpt.get();
		
		return new ContaDTO(contaFromTable.getId(), contaFromTable.getNome(), contaFromTable.getRenda());
	}
	
	@GetMapping("/conta/por_usuario/{idUsuario}") 
	public ContaDTO getContaPorUsuario(@PathVariable Long idUsuario) {
		Usuario usuario = usuarioRepository.findById(idUsuario).get();
		
		Optional<Conta> contaOpt = contaRepository.findByUsuario(usuario);
		Conta conta = contaOpt.get();
		
		return new ContaDTO(conta.getId(), conta.getNome(), conta.getRenda());
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
	
	// CRUD da Categoria e Movimento
	@PostMapping("/criar_movimentacao/{idConta}")
	public String criarCategoriaEMovimento(@RequestBody WrapperCategoriaMovimento wrapper, @PathVariable Long idConta) {
		Optional<Conta> contaOpt = contaRepository.findById(idConta);
		Conta conta = contaOpt.get();
		
		Categoria categoria = wrapper.getCategoria();
		Movimento movimento = wrapper.getMovimento();
		
		movimento.setCategoria(categoria);
		movimento.setConta(conta);
		movimento.setData(new Date());
		
		categoriaRepository.save(categoria);
		movimentoRepository.save(movimento);
		
		
		return "Movimento e categoria criados com sucesso";
	}
	
	@GetMapping("/categorias")
	public List<Categoria> todasCategorias() {
		List<Categoria> categorias = categoriaRepository.findAll();
		
		return categorias;
	}
	
	@GetMapping("/categoria/{idCategoria}")
	public CategoriaDTO pegarCategoria(@PathVariable Long idCategoria) {
		Optional<Categoria> categoriaOpt = categoriaRepository.findById(idCategoria);
		Categoria categoria = categoriaOpt.get();
		
		return new CategoriaDTO(categoria.getId(), categoria.getNome());
	}
	
	@DeleteMapping("deletar_categoria/{idCategoria}")
	public String deletarCategora(@PathVariable Long idCategoria) {
		Optional<Categoria> categoriaOpt = categoriaRepository.findById(idCategoria);
		Categoria categoriaFromTable = categoriaOpt.get();
		
		categoriaRepository.delete(categoriaFromTable);
		
		return "Categoria deletada com sucesso";
	}
	
	// CRUD do Movimento
	
	@GetMapping("/movimentos")
	public List<Movimento> getMovimentos() {
		List<Movimento> movimentos = movimentoRepository.findAll();
		
		return movimentos;
	}
}