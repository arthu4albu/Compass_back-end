package com.compass.uninassau.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity 
@Table(name="Movimento")
public class Movimento {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO) 
	private Long id;
	
	@Column(nullable = false)
	private Date data;
	
	@Column(nullable = false)
	private double valor;

	@Column(columnDefinition = "CHECK (tipo_movimento IN ('CREDITAR', 'DEBITAR'))")
	private String tipo_movimento;
	
	@ManyToOne
	@JoinColumn(name = "idCategoria")
	private Categoria categoria;
}
