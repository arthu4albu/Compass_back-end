package com.compass.uninassau.DTOs;

import java.util.List;

import com.compass.uninassau.DTOs.MovimentoDTO;
import com.compass.uninassau.entity.Categoria;
import com.compass.uninassau.entity.Movimento;

public class CategoriaDTO {
	private Long id;
	private String nome;
	
	private List<MovimentoDTO> movimentos;
	
	public CategoriaDTO(Categoria categoria) {
		this.id = categoria.getId();
		this.nome = categoria.getNome();
		
		this.movimentos = categoria.getMovimentos()
				.stream()
				.map(MovimentoDTO::new)
				.toList();
		setNome(nome);
	}
	
	public List<MovimentoDTO> getMovimentos() {
		return movimentos;
	}

	public void setMovimentos(List<MovimentoDTO> movimentos) {
		this.movimentos = movimentos;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
}
