package com.marcos.desenvolvimento.desafio_tecnico.usecases;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.marcos.desenvolvimento.desafio_tecnico.repository.DAOTurmaParticipante;

@Service
public class DeleteTurmaParticipanteUseCase {
	
	private final DAOTurmaParticipante daoTurmaParticipante;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DeleteTurmaParticipanteUseCase.class);
	
	public DeleteTurmaParticipanteUseCase(DAOTurmaParticipante daoTurmaParticipante) {
		this.daoTurmaParticipante = daoTurmaParticipante;
	}
	
	public void deletarParticipante(final int codigoTurmaParticipante) {
		if(codigoTurmaParticipante > 0) {
			daoTurmaParticipante.removerParticipanteDaTurma(codigoTurmaParticipante);
		} else {
			LOGGER.warn("O código fornecido é inválido! Valor fornecido: " + codigoTurmaParticipante);
		}
		
	}

}
