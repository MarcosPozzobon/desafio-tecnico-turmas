package com.marcos.desenvolvimento.desafio_tecnico.usecases;

import org.springframework.stereotype.Service;

import com.marcos.desenvolvimento.desafio_tecnico.repository.DAOTurmas;
import com.marcos.desenvolvimento.desafio_tecnico.request.TurmaAtualizacaoRequest;
import com.marcos.desenvolvimento.desafio_tecnico.response.TurmaInformacoesBasicasResponse;

@Service
public class UpdateTurmaUseCase {
	
	private final DAOTurmas daoTurmas;
	
	public UpdateTurmaUseCase(DAOTurmas daoTurmas) {
		this.daoTurmas = daoTurmas;
	}
	
	public TurmaInformacoesBasicasResponse atualizarTurma(int codigoTurma, final TurmaAtualizacaoRequest turmaAtualizacaoRequest) {
		return daoTurmas.atualizarTurma(turmaAtualizacaoRequest, codigoTurma);
	}

}
