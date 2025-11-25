package com.compass.uninassau.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.compass.uninassau.entity.Movimento;

public interface MovimentoRepository extends JpaRepository<Movimento, Long>{
	@Query("SELECT m FROM Movimento m WHERE EXTRACT(MONTH FROM m.data) = :mes")
	List<Movimento> buscarPorMes(@Param("mes") int mes);	
}
