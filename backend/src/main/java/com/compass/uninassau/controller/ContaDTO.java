package com.compass.uninassau.controller;

public class ContaDTO {
	private Long id;
	private String nome;
	private double renda;
	
	public ContaDTO(Long id, String nome, double renda) {
		setId(id);
		setNome(nome);
		setRenda(renda);
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

	public double getRenda() {
		return renda;
	}

	public void setRenda(double renda) {
		this.renda = renda;
	}
}
