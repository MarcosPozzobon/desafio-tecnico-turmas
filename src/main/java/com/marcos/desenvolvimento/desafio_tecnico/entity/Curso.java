package com.marcos.desenvolvimento.desafio_tecnico.entity;

public class Curso {

    private int codigoCurso;

    private String nome;

    private String descricao;

    private int duracao;

    public Curso() {

    }

    public Curso(int codigoCurso, String nome, String descricao, int duracao) {
        this.codigoCurso = codigoCurso;
        this.nome = nome;
        this.descricao = descricao;
        this.duracao = duracao;
    }

    public Curso(String nome, String descricao, int duracao) {
        this.nome = nome;
        this.descricao = descricao;
        this.duracao = duracao;
    }

    public int getCodigoCurso() {
        return codigoCurso;
    }

    public void setCodigoCurso(int codigoCurso) {
        this.codigoCurso = codigoCurso;
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
