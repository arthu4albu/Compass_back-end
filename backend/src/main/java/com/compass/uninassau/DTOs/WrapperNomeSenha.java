package com.compass.uninassau.DTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class WrapperNomeSenha {
	@NotBlank(message = "Nome é obrigatório")
	@Size(min = 3, max = 50)
	private String nome;
	
	@NotBlank(message = "Senha é obrigatória")
	@Size(min = 6, max = 100)
	private String senha;
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}	
}
