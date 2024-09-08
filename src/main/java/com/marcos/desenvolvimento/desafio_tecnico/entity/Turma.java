package com.marcos.desenvolvimento.desafio_tecnico.entity;

import java.util.Date;

public class Turma {

    private int codigoTurma;

    private Date dtInicio;

    private Date dtFim;

    private String local;

    private int cursoIdFk;

    public Turma() {

    }

    public Turma(int codigoTurma, Date dtInicio, Date dtFim, String local, int cursoIdFk) {
        this.codigoTurma = codigoTurma;
        this.dtInicio = dtInicio;
        this.dtFim = dtFim;
        this.local = local;
        this.cursoIdFk = cursoIdFk;
    }

    public int getCodigoTurma() {
        return codigoTurma;
    }

    public void setCodigoTurma(int codigoTurma) {
        this.codigoTurma = codigoTurma;
    }

    public Date getDtInicio() {
        return dtInicio;
    }

    public void setDtInicio(Date dtInicio) {
        this.dtInicio = dtInicio;
    }

    public Date getDtFim() {
        return dtFim;
    }

    public void setDtFim(Date dtFim) {
        this.dtFim = dtFim;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public int getCursoIdFk() {
        return cursoIdFk;
    }

    public void setCursoIdFk(int cursoIdFk) {
        this.cursoIdFk = cursoIdFk;
    }

}
