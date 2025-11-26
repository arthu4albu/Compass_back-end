package com.compass.uninassau.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class NotificacaoAgendada {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String expoPushToken;
    private String titulo;
    private String mensagem;
    private LocalDateTime dataHora; // quando disparar
    private boolean enviada = false;

    // Getters e setters
    public Long getId() { return id; }
    public String getExpoPushToken() { return expoPushToken; }
    public void setExpoPushToken(String expoPushToken) { this.expoPushToken = expoPushToken; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getMensagem() { return mensagem; }
    public void setMensagem(String mensagem) { this.mensagem = mensagem; }
    public LocalDateTime getDataHora() { return dataHora; }
    public void setDataHora(LocalDateTime dataHora) { this.dataHora = dataHora; }
    public boolean isEnviada() { return enviada; }
    public void setEnviada(boolean enviada) { this.enviada = enviada; }
}
