package com.marcos.desenvolvimento.desafio_tecnico.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
			InsertParticipanteTurmaUseCase insertParticipanteTurmaUseCase) {
		this.deleteTurmaParticipanteUseCase = deleteTurmaParticipanteUseCase;
		this.findParticipantesUseCase = findParticipantesUseCase;
		this.insertParticipanteTurmaUseCase = insertParticipanteTurmaUseCase;
	}

	@DeleteMapping("/{codigoParticipante}")
	public ResponseEntity<Void> excluirParticipante(@PathVariable(value = "codigoParticipante") int codigoParticipante) {
		LOGGER.info("O serviço de remoção de participantes foi chamado em: " + this.getClass().getName() + " - Participante: " + codigoParticipante);
		deleteTurmaParticipanteUseCase.deletarParticipante(codigoParticipante);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/listar/paginacao/{paginacao}")
	public ResponseEntity<List<TurmaParticipanteResponse>> obterListagemParticipantes(@PathVariable(value = "paginacao") int paginacao) {
		LOGGER.info("O serviço de listagem de todos os participantes foi chamado em: " + this.getClass().getName());
		return ResponseEntity.ok(findParticipantesUseCase.listarParticipantes(paginacao));
	}

	@PostMapping("/cadastrar/participante/{codigoFuncionario}/turma/{codigoTurma}")
	public ResponseEntity<Void> cadastrarNovoParticipante(
			@PathVariable(value = "codigoFuncionario") int codigoFuncionario,
			@PathVariable(value = "codigoTurma") int codigoTurma) {
		LOGGER.info("O serviço de cadastro de participantes foi chamado em: " + this.getClass().getName() + " - Funcionário: " + codigoFuncionario + ", Turma: " + codigoTurma);
		insertParticipanteTurmaUseCase.inserirParticipante(codigoTurma, codigoFuncionario);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
}
