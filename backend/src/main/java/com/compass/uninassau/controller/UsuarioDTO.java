package com.compass.uninassau.controller;

public class UsuarioDTO {
	private String nome;
	private String email;
	
	public UsuarioDTO(String nome, String email){
		setNome(nome);
		setEmail(email);
	}
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
}
