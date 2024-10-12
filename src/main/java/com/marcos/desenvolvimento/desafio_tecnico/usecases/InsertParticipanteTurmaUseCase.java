package com.marcos.desenvolvimento.desafio_tecnico.usecases;

import org.springframework.stereotype.Service;

import com.marcos.desenvolvimento.desafio_tecnico.repository.DAOTurmaParticipante;

@Service
public class InsertParticipanteTurmaUseCase {
	
	private final DAOTurmaParticipante daoTurmaParticipante;
	
	public InsertParticipanteTurmaUseCase(DAOTurmaParticipante daoTurmaParticipante) {
		this.daoTurmaParticipante = daoTurmaParticipante;
	}
	
	public void inserirParticipante(final int codigoTurma, final int codigoFuncionario) {
		if(codigoFuncionario > 0 && codigoTurma > 0) {
			if(daoTurmaParticipante.existeRelacaoNaTabelaFuncionario(codigoFuncionario)) {
				daoTurmaParticipante.cadastrarParticipante(codigoTurma, codigoFuncionario);
			}
		}
	}
}
