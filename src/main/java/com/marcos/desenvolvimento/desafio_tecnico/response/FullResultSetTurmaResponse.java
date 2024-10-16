package com.marcos.desenvolvimento.desafio_tecnico.response;

import java.util.List;

public class FullResultSetTurmaResponse {

    private Integer codigoCurso;

    private String nomeCurso;

    private Integer duracao;

    private Integer quantidadeTurmas;

    private List<TurmaResponse> turmas;

    public FullResultSetTurmaResponse(){}

    public FullResultSetTurmaResponse(Integer codigoCurso, String nomeCurso, Integer duracao, Integer quantidadeTurmas, List<TurmaResponse> turmas) {
        this.codigoCurso = codigoCurso;
        this.nomeCurso = nomeCurso;
        this.duracao = duracao;
        this.quantidadeTurmas = quantidadeTurmas;
        this.turmas = turmas;
    }

    public Integer getCodigoCurso() {
        return codigoCurso;
    }

    public void setCodigoCurso(Integer codigoCurso) {
        this.codigoCurso = codigoCurso;
    }

    public String getNomeCurso() {
        return nomeCurso;
    }

    public void setNomeCurso(String nomeCurso) {
        this.nomeCurso = nomeCurso;
    }

    public Integer getDuracao() {
        return duracao;
    }

    public void setDuracao(Integer duracao) {
        this.duracao = duracao;
    }

    public Integer getQuantidadeTurmas() {
        return quantidadeTurmas;
    }

    public void setQuantidadeTurmas(Integer quantidadeTurmas) {
        this.quantidadeTurmas = quantidadeTurmas;
    }

    public List<TurmaResponse> getTurmas() {
        return turmas;
    }

    public void setTurmas(List<TurmaResponse> turmas) {
        this.turmas = turmas;
    }
}
