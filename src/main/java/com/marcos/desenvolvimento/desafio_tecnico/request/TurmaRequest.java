package com.marcos.desenvolvimento.desafio_tecnico.request;

import java.time.LocalDate;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TurmaRequest {
	
	@JsonProperty(value = "dt_inicio")
	private LocalDate dtInicio;

	@JsonProperty(value = "dt_fim")
    private LocalDate dtFim;

    private String local;

	@JsonProperty(value = "curso_id")
    private int cursoIdFk;
    
    public TurmaRequest() {}
    

	public TurmaRequest(LocalDate dtInicio, LocalDate dtFim, String local, int cursoIdFk) {
		super();
		this.dtInicio = dtInicio;
		this.dtFim = dtFim;
		this.local = local;
		this.cursoIdFk = cursoIdFk;
	}



	public LocalDate getDtInicio() {
		return dtInicio;
	}

	public void setDtInicio(LocalDate dtInicio) {
		this.dtInicio = dtInicio;
	}

	public LocalDate getDtFim() {
		return dtFim;
	}

	public void setDtFim(LocalDate dtFim) {
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
