package com.marcos.desenvolvimento.desafio_tecnico.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.marcos.desenvolvimento.desafio_tecnico.response.TurmaParticipanteResponse;
import com.marcos.desenvolvimento.desafio_tecnico.usecases.DeleteTurmaParticipanteUseCase;
import com.marcos.desenvolvimento.desafio_tecnico.usecases.FindParticipantesUseCase;
import com.marcos.desenvolvimento.desafio_tecnico.usecases.InsertParticipanteTurmaUseCase;

@RestController
@RequestMapping("api/v1/participantes-turmas")
public class TurmaParticipanteController {
	
	private final DeleteTurmaParticipanteUseCase deleteTurmaParticipanteUseCase;
	
	private final FindParticipantesUseCase findParticipantesUseCase;

	private final InsertParticipanteTurmaUseCase insertParticipanteTurmaUseCase;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TurmaParticipanteController.class);
	
	public TurmaParticipanteController(
			DeleteTurmaParticipanteUseCase deleteTurmaParticipanteUseCase, 
			FindParticipantesUseCase findParticipantesUseCase, 
			InsertParticipanteTurmaUseCase insertParticipanteTurmaUseCase) 
	{
		this.deleteTurmaParticipanteUseCase = deleteTurmaParticipanteUseCase;
		this.findParticipantesUseCase = findParticipantesUseCase;
		this.insertParticipanteTurmaUseCase = insertParticipanteTurmaUseCase;
		
	}
	
	@DeleteMapping("/excluir-participante/{codigo_participante}")
	public ResponseEntity<Void> excluirParticipante(@PathVariable(value = "codigo_participante") int codigoParticipante){
		deleteTurmaParticipanteUseCase.deletarParticipante(codigoParticipante);
		LOGGER.info("O serviço de remoção de participantes foi chamado em: " + this.getClass().getName());
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping("/listar-participantes/{paginacao}")
	public ResponseEntity<List<TurmaParticipanteResponse>> obterListagemParticipantes(@PathVariable(value = "paginacao") int paginacao){
		LOGGER.info("O serviço de listagem de todos os participantes foi chamado em: " + this.getClass().getName());
		return ResponseEntity.ok(findParticipantesUseCase.listarParticipantes(paginacao));
	}
	
	@PostMapping("/cadastrar-novo-participante/participante/{codigo_funcionario}/turma-vinculada/{codigo_turma}")
	public ResponseEntity<Void> cadastrarNovoParticipante(@PathVariable(value = "codigo_funcionario") int codigoFuncionario, @PathVariable(value = "codigo_turma") int codigoTurma) {
		insertParticipanteTurmaUseCase.inserirParticipante(codigoTurma, codigoFuncionario);
		LOGGER.info("O serviço de cadastro de participantes foi chamado em: " + this.getClass().getName());
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}	
}
