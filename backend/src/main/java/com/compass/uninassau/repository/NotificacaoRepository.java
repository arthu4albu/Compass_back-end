package com.compass.uninassau.repository;

import com.compass.uninassau.entity.NotificacaoAgendada;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface NotificacaoRepository extends JpaRepository<NotificacaoAgendada, Long> {
    List<NotificacaoAgendada> findByEnviadaFalseAndDataHoraBefore(LocalDateTime now);
}
