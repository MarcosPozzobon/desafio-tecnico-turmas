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


}
