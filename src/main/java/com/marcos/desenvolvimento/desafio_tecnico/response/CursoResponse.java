package com.marcos.desenvolvimento.desafio_tecnico.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CursoResponse {

    private String nome;

    private String descricao;

    private int duracao;

    @JsonProperty("ativo")
    private String isAtivo;

    public CursoResponse() {

    }
    
    public CursoResponse(String nome, String descricao, int duracao) {
        super();
        this.nome = nome;
        this.descricao = descricao;
        this.duracao = duracao;
    }

    public CursoResponse(String nome, String descricao, int duracao, String isAtivo) {
        super();
        this.nome = nome;
        this.descricao = descricao;
        this.duracao = duracao;
        this.isAtivo = isAtivo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getDuracao() {
        return duracao;
    }

    public void setDuracao(int duracao) {
        this.duracao = duracao;
    }

    public String getIsAtivo() {
        return isAtivo;
    }

    public void setIsAtivo(String isAtivo) {
        this.isAtivo = isAtivo;
    }
}
