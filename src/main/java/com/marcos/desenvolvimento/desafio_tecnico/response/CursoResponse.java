package com.marcos.desenvolvimento.desafio_tecnico.response;

public class CursoResponse {

    private String nome;

    private String descricao;

    private int duracao;

    public CursoResponse() {

    }

    public CursoResponse(String nome, String descricao, int duracao) {
        super();
        this.nome = nome;
        this.descricao = descricao;
        this.duracao = duracao;
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
}
