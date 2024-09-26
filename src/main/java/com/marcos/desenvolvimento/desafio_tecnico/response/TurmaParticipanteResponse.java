package com.marcos.desenvolvimento.desafio_tecnico.response;

public class TurmaParticipanteResponse {

    private Integer codigoParticipanteTurma;

    private String nome;

    public TurmaParticipanteResponse(){}

    public TurmaParticipanteResponse(Integer codigoParticipanteTurma, String nome) {
        this.codigoParticipanteTurma = codigoParticipanteTurma;
        this.nome = nome;
    }

    public Integer getCodigoParticipanteTurma() {
        return codigoParticipanteTurma;
    }

    public void setCodigoParticipanteTurma(Integer codigoParticipanteTurma) {
        this.codigoParticipanteTurma = codigoParticipanteTurma;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
