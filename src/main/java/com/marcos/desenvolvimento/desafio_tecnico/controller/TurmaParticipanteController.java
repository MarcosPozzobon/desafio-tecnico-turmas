package com.marcos.desenvolvimento.desafio_tecnico.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.marcos.desenvolvimento.desafio_tecnico.usecases.DeleteTurmaParticipanteUseCase;

@RestController
@RequestMapping("api/v1/participantes-turmas")
public class TurmaParticipanteController {
	
	private final DeleteTurmaParticipanteUseCase deleteTurmaParticipanteUseCase;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TurmaParticipanteController.class);
	
	public TurmaParticipanteController(DeleteTurmaParticipanteUseCase deleteTurmaParticipanteUseCase) {
		this.deleteTurmaParticipanteUseCase = deleteTurmaParticipanteUseCase;
	}
	
	@DeleteMapping("/excluir-participante/{codigo_participante}")
	public ResponseEntity<Void> excluirParticipante(@PathVariable(value = "codigo_participante") int codigoParticipante){
		deleteTurmaParticipanteUseCase.deletarParticipante(codigoParticipante);
		LOGGER.info("O serviço de remoção de participantes foi chamado em: " + this.getClass().getName());
		return ResponseEntity.noContent().build();
	}
}
