package com.marcos.desenvolvimento.desafio_tecnico.response;

import java.time.LocalDate;

public class TurmaInformacoesBasicasResponse {
	
	private LocalDate dataInicio;
	
	private LocalDate dataFim;
	
	private String local;
	
	private CursoResponse cursoAssociado;
	
	public TurmaInformacoesBasicasResponse() {};

	public TurmaInformacoesBasicasResponse(LocalDate dataInicio, LocalDate dataFim, String local,
			CursoResponse cursoAssociado) {
		super();
		this.dataInicio = dataInicio;
		this.dataFim = dataFim;
		this.local = local;
		this.cursoAssociado = cursoAssociado;
	}

	public LocalDate getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(LocalDate dataInicio) {
		this.dataInicio = dataInicio;
	}

	public LocalDate getDataFim() {
		return dataFim;
	}

	public void setDataFim(LocalDate dataFim) {
		this.dataFim = dataFim;
	}

	public String getLocal() {
		return local;
	}

	public void setLocal(String local) {
		this.local = local;
	}

	public CursoResponse getCursoAssociado() {
		return cursoAssociado;
	}

	public void setCursoAssociado(CursoResponse cursoAssociado) {
		this.cursoAssociado = cursoAssociado;
	}
}
