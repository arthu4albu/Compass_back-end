package com.compass.uninassau.controller;

import java.time.LocalDateTime;

public class NotificacaoDTO {
    private String expoPushToken;
    private String titulo;
    private String mensagem;
    private LocalDateTime dataHora;

    // Getters e setters
    public String getExpoPushToken() { return expoPushToken; }
    public void setExpoPushToken(String expoPushToken) { this.expoPushToken = expoPushToken; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getMensagem() { return mensagem; }
    public void setMensagem(String mensagem) { this.mensagem = mensagem; }
    public LocalDateTime getDataHora() { return dataHora; }
    public void setDataHora(LocalDateTime dataHora) { this.dataHora = dataHora; }
}
