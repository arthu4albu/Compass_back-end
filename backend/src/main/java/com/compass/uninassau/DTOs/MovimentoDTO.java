package com.compass.uninassau.DTOs;

import java.util.Date;

import com.compass.uninassau.entity.Movimento;

public class MovimentoDTO {
	private Long id;
	private Date data;
	private double valor;
	private String tipo_movimento;
	private Long idConta;
	private Long idCategoria;
	
	public MovimentoDTO(Movimento movimento) {
		this.id = movimento.getId();
		this.data = movimento.getData();
		this.valor = movimento.getValor();
		this.tipo_movimento = movimento.getTipo_movimento();
		this.idConta = movimento.getConta().getId();
		this.idCategoria = movimento.getCategoria().getId();
	}
	
	public Long getIdCategoria() {
		return idCategoria;
	}

	public void setIdCategoria(Long idCategoria) {
		this.idCategoria = idCategoria;
	}

	public Long getIdConta() {
		return idConta;
	}

	public void setIdConta(Long idConta) {
		this.idConta = idConta;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public double getValor() {
		return valor;
	}

	public void setValor(double valor) {
		this.valor = valor;
	}

	public String getTipo_movimento() {
		return tipo_movimento;
	}

	public void setTipo_movimento(String tipo_movimento) {
		this.tipo_movimento = tipo_movimento;
	}
	
}
