package com.marcos.desenvolvimento.desafio_tecnico.usecases;

import org.springframework.stereotype.Service;

import com.marcos.desenvolvimento.desafio_tecnico.repository.DAOTurmas;
import com.marcos.desenvolvimento.desafio_tecnico.request.TurmaRequest;

@Service
public class InsertTurmaUseCase {
	
	private final DAOTurmas daoTurmas;
	
	public InsertTurmaUseCase(DAOTurmas daoTurmas) {
		this.daoTurmas = daoTurmas;
	}
	
	public void inserir(final TurmaRequest turmaRequestJson) {
		if(turmaRequestJson != null) {
			daoTurmas.cadastrarTurma(turmaRequestJson);
		}
	}

}
