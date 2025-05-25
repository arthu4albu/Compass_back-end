package com.compass.uninassau.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.compass.uninassau.entity.Usuario;
import com.compass.uninassau.repository.CompassRepository;

@RestController
public class CompassController {
	@Autowired
	private CompassRepository compassRepository;
	
	@PostMapping("/criar")
	public String save(@RequestBody Usuario usuario) {
		compassRepository.save(usuario);
		return "Algo salvo com sucesso";
	}
	
	@GetMapping("/pegar") 
	public List<Usuario> get() {
		return compassRepository.findAll();
	}
}