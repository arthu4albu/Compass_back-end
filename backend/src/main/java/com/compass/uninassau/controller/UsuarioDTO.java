package com.compass.uninassau.controller;

public class UsuarioDTO {

    private String nome;
    private String email;
    private String nascimento;
    private String telefone;
    private String foto; // base64 OU null

    public UsuarioDTO(String nome, String email, String nascimento, String telefone, String foto) {
        this.nome = nome;
        this.email = email;
        this.nascimento = nascimento;
        this.telefone = telefone;
        this.foto = foto;
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

    public String getNascimento() {
        return nascimento;
    }

    public void setNascimento(String nascimento) {
        this.nascimento = nascimento;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
}
