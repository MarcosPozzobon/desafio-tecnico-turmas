package com.marcos.desenvolvimento.desafio_tecnico.usecases;

import java.util.HashMap;

import org.springframework.stereotype.Service;

import com.marcos.desenvolvimento.desafio_tecnico.repository.DAOTurmas;

@Service
public class DeleteTurmaUseCase {
	
	private final DAOTurmas daoTurmas;
	
	public DeleteTurmaUseCase(DAOTurmas daoTurmas) {
		this.daoTurmas = daoTurmas;
	}
	
	public HashMap<String, Object> deletarTurma(final int codigoTurma){
		if(codigoTurma > 0) {
			return daoTurmas.deletarTurma(codigoTurma);
		}
		return null;
	}

}
