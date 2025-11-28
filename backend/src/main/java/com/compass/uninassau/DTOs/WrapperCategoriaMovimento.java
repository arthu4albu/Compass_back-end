package com.compass.uninassau.DTOs;

import com.compass.uninassau.entity.Categoria;
import com.compass.uninassau.entity.Movimento;

public class WrapperCategoriaMovimento {
	private Categoria categoria;
	private Movimento movimento;
	
	public Categoria getCategoria() {
		return categoria;
	}
	
	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}
	
	public Movimento getMovimento() {
		return movimento;
	}
	
	public void setMovimento(Movimento movimento) {
		this.movimento = movimento;
	}
}
