package com.marcos.desenvolvimento.desafio_tecnico.request;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TurmaAtualizacaoRequest {
	
	@JsonProperty(value = "dt_inicio")
	private LocalDate dtInicio;

	@JsonProperty(value = "dt_fim")
    private LocalDate dtFim;

    private String local;
    
    public TurmaAtualizacaoRequest() {}
    
	public TurmaAtualizacaoRequest(LocalDate dtInicio, LocalDate dtFim, String local) {
		super();
		this.dtInicio = dtInicio;
		this.dtFim = dtFim;
		this.local = local;
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
    
    

}
