package com.marcos.desenvolvimento.desafio_tecnico.entity;

public class TurmaParticipante {

    private int codigoTurmaParticipante;

    private int turmaIdFk;

    private int funcionarioIdFk;

    public TurmaParticipante() {

    }


    public TurmaParticipante(int codigoTurmaParticipante, int turmaIdFk, int funcionarioIdFk) {
        this.codigoTurmaParticipante = codigoTurmaParticipante;
        this.turmaIdFk = turmaIdFk;
        this.funcionarioIdFk = funcionarioIdFk;
    }


    public int getCodigoTurmaParticipante() {
        return codigoTurmaParticipante;
    }


    public void setCodigoTurmaParticipante(int codigoTurmaParticipante) {
        this.codigoTurmaParticipante = codigoTurmaParticipante;
    }


    public int getTurmaIdFk() {
        return turmaIdFk;
    }


    public void setTurmaIdFk(int turmaIdFk) {
        this.turmaIdFk = turmaIdFk;
    }


    public int getFuncionarioIdFk() {
        return funcionarioIdFk;
    }


    public void setFuncionarioIdFk(int funcionarioIdFk) {
        this.funcionarioIdFk = funcionarioIdFk;
    }

}

