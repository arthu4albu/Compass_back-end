package com.compass.uninassau.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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
import org.springframework.web.multipart.MultipartFile;

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
    public String cadastarUsuario(@RequestBody CriarUsuarioDTO usuarioDTO) {


        // Salva o usuário
    	Usuario usuario =  new Usuario();
    	usuario.setNome(usuarioDTO.getNome());
    	usuario.setEmail(usuarioDTO.getEmail());
    	usuario.setNascimento(usuarioDTO.getNascimento());
    	usuario.setTelefone(usuarioDTO.getTelefone());
    	usuario.setSenha(usuarioDTO.getSenha());
    	
        // Ao cadastrar, sempre começa sem imagem
    	usuario.setImagem(null);
    	
        Usuario usuarioSalvo = usuarioRepository.save(usuario);

        // Cria conta automaticamente
        Conta conta = new Conta(usuarioSalvo.getNome(), 0.00, usuarioSalvo);
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
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // Converter imagem em Base64
        String fotoBase64 = null;
        if (usuario.getImagem() != null) {
            fotoBase64 = java.util.Base64.getEncoder().encodeToString(usuario.getImagem());
        }

        return new UsuarioDTO(
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getNascimento(),
                usuario.getTelefone(),
                fotoBase64
        );
    }

    @PutMapping("/atualizar_usuario/{idUsuario}")
    public ResponseEntity<String> atualizarUsuario(
            @PathVariable Long idUsuario,
            @RequestBody AtualizarUsuarioDTO dto) {

        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        usuario.setNascimento(dto.getNascimento());
        usuario.setTelefone(dto.getTelefone());

        usuarioRepository.save(usuario);

        return ResponseEntity.ok("Usuário atualizado com sucesso");
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
    public ResponseEntity<?> getUsuario(@RequestBody WrapperNomeSenha wrapper) {
        String nome = wrapper.getNome();
        String senha = wrapper.getSenha();

        //login usando a query que NÃO busca imagem
        Long usuarioId = usuarioRepository.login(nome, senha);

        if (usuarioId == null) {
            return ResponseEntity.status(401).body("Usuário ou senha inválidos");
        }

        return ResponseEntity.ok(usuarioId);
    }


//    public Usuario verifyUser(String nome, String senha) {
//        List<Usuario> usuarioList = usuarioRepository.findByNomeAndSenha(nome, senha);
//        Usuario usuario = usuarioList.get(0);
//
//        return usuario;
//    }

    // Controle da Imagem salva no banco

    // Upload de imagem para o banco.
    @PostMapping(value = "/usuario/{id}/imagem", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadImagem(
            @PathVariable Long id,
            @RequestParam("imagem") MultipartFile imagem) throws IOException {

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        usuario.setImagem(imagem.getBytes());
        usuarioRepository.save(usuario);

        return ResponseEntity.ok("Imagem salva com sucesso!");
    }


    // Download de imagem
    @GetMapping("/usuario/{id}/imagem")
    public ResponseEntity<byte[]> getImagem(@PathVariable Long id) {

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        byte[] imagem = usuario.getImagem();
        if (imagem == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity
                .ok()
                .contentType(MediaType.IMAGE_JPEG) // ou PNG
                .body(imagem);
    }


    // CRUD da Conta
    @PostMapping("/cadastrar_conta/{idUsuario}")
    public ResponseEntity<String> salvarRenda(@RequestBody Conta conta, @PathVariable Long idUsuario) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(idUsuario);

        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Usuário não encontrado.");
        }

        conta.setUsuario(usuarioOpt.get());
        contaRepository.save(conta);

        return ResponseEntity.ok("Conta criada com sucesso!");
    }

    @PutMapping("/atualizar_conta/{idConta}")
    public ResponseEntity<String> atualizarRenda(
            @PathVariable long idConta,
            @RequestBody AtualizarContaDTO dto) {

        Conta conta = contaRepository.findById(idConta)
                .orElseThrow(() -> new RuntimeException("Conta não encontrada"));

        if (dto.getNome() != null) {
            conta.setNome(dto.getNome());
        }

        if (dto.getRenda() != null) {
            conta.setRenda(dto.getRenda());
        }

        contaRepository.save(conta);
        return ResponseEntity.ok("Conta atualizada");
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
//	@PostMapping("/criar_movimentacao/{idConta}")
//	public String criarCategoriaEMovimento(@RequestBody WrapperCategoriaMovimento wrapper, @PathVariable Long idConta) {
//		Optional<Conta> contaOpt = contaRepository.findById(idConta);
//		
//		// Se a categoria não existir ainda, será criada uma nova
//		
//		
//		Conta conta = contaOpt.get();
//		
//		Categoria categoria = wrapper.getCategoria();
//		Movimento movimento = wrapper.getMovimento();
//		
//		movimento.setCategoria(categoria);
//		movimento.setConta(conta);
//		movimento.setData(new Date());
//		
//		categoriaRepository.save(categoria);
//		movimentoRepository.save(movimento);
//		
//		
//		return "Movimento e categoria criados com sucesso";
//	}


    @PostMapping("/criar_movimentacao/{idConta}")
    public String criarCategoriaEMovimento(
            @RequestBody WrapperCategoriaMovimento wrapper,
            @PathVariable Long idConta) {

        Conta conta = contaRepository.findById(idConta)
                .orElseThrow(() -> new RuntimeException("Conta não encontrada"));

        Categoria categoriaRequest = wrapper.getCategoria();
        Movimento movimento = wrapper.getMovimento();

        // 🔍 1. Verificar se a categoria já existe no banco (pelo nome)
        Optional<Categoria> categoriaExistenteOpt =
                categoriaRepository.findByNome(categoriaRequest.getNome());

        Categoria categoria;

        if (categoriaExistenteOpt.isPresent()) {
            // ✔ A categoria já existe → reutiliza
            categoria = categoriaExistenteOpt.get();
        } else {
            // ✔ Não existe → cria nova
            categoria = new Categoria();
            categoria.setNome(categoriaRequest.getNome());
            // categoria.setConta(conta);  // caso sua modelagem use relacionamento
            categoriaRepository.save(categoria);
        }

        // 🔗 2. Relacionar movimento com categoria e conta
        movimento.setCategoria(categoria);
        movimento.setConta(conta);
        movimento.setData(new Date());

        // 💾 3. Salvar movimento
        movimentoRepository.save(movimento);

        return "Movimento e categoria processados com sucesso";
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

        return new CategoriaDTO(categoria);
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
    public List<MovimentoDTO> getMovimentos() {
        List<Movimento> movimentos = movimentoRepository.findAll();

        List<MovimentoDTO> dtos = new ArrayList<>();
        for (Movimento m : movimentos) {
            dtos.add(new MovimentoDTO(m));
        }

        return dtos;
    }

    @GetMapping("/movimentos/mes/{mes}")
    public List<MovimentoDTO> getMovimentosPorMes(@PathVariable int mes) {
        List<Movimento> movimentos = movimentoRepository.buscarPorMes(mes);

        List<MovimentoDTO> dtos = new ArrayList<>();
        for (Movimento m : movimentos) {
            dtos.add(new MovimentoDTO(m));
        }

        return dtos;
    }
}