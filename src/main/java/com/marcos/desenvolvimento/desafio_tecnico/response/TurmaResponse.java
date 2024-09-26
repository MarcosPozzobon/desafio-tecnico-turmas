package com.marcos.desenvolvimento.desafio_tecnico.response;

import com.marcos.desenvolvimento.desafio_tecnico.entity.Turma;

import java.util.Date;
import java.util.List;

public class TurmaResponse {

    private Integer codigoTurma;

    private Date dataInicio;

    private Date dataFim;

    private String local;

    private Integer quantidadeParticipantes;

    private List<TurmaParticipanteResponse> participantes;

    public TurmaResponse(){}

    public TurmaResponse(Integer codigoTurma, Date dataInicio, Date dataFim, String local, Integer quantidadeParticipantes, List<TurmaParticipanteResponse> participantes) {
        this.codigoTurma = codigoTurma;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.local = local;
        this.quantidadeParticipantes = quantidadeParticipantes;
        this.participantes = participantes;
    }

    public Integer getCodigoTurma() {
        return codigoTurma;
    }

    public void setCodigoTurma(Integer codigoTurma) {
        this.codigoTurma = codigoTurma;
    }

    public Date getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }

    public Date getDataFim() {
        return dataFim;
    }

    public void setDataFim(Date dataFim) {
        this.dataFim = dataFim;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public Integer getQuantidadeParticipantes() {
        return quantidadeParticipantes;
    }

    public void setQuantidadeParticipantes(Integer quantidadeParticipantes) {
        this.quantidadeParticipantes = quantidadeParticipantes;
    }

    public List<TurmaParticipanteResponse> getParticipantes() {
        return participantes;
    }

    public void setParticipantes(List<TurmaParticipanteResponse> participantes) {
        this.participantes = participantes;
    }
}
