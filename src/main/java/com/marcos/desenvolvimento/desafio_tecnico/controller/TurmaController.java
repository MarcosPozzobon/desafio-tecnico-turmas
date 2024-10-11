package com.marcos.desenvolvimento.desafio_tecnico.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.marcos.desenvolvimento.desafio_tecnico.request.TurmaAtualizacaoRequest;
import com.marcos.desenvolvimento.desafio_tecnico.request.TurmaRequest;
import com.marcos.desenvolvimento.desafio_tecnico.response.FullResultSetTurmaResponse;
import com.marcos.desenvolvimento.desafio_tecnico.response.TurmaInformacoesBasicasResponse;
import com.marcos.desenvolvimento.desafio_tecnico.usecases.DeleteTurmaUseCase;
import com.marcos.desenvolvimento.desafio_tecnico.usecases.FindTurmasUseCase;
import com.marcos.desenvolvimento.desafio_tecnico.usecases.InsertTurmaUseCase;
import com.marcos.desenvolvimento.desafio_tecnico.usecases.UpdateTurmaUseCase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("api/v1/turmas")
public class TurmaController {

    private final FindTurmasUseCase findTurmasUseCase;
    
    private final DeleteTurmaUseCase deleteTurmaUseCase;
    
    private final InsertTurmaUseCase insertTurmaUseCase;
    
    private final UpdateTurmaUseCase updateTurmaUseCase;
    
    private final ObjectMapper objectMapper;

    private static final Logger LOGGER = LoggerFactory.getLogger(CursoController.class);

    public TurmaController(
    		FindTurmasUseCase findTurmasUseCase, 
    		DeleteTurmaUseCase deleteTurmaUseCase, 
    		InsertTurmaUseCase insertTurmaUseCase, 
    		UpdateTurmaUseCase updateTurmaUseCase,
    		ObjectMapper objectMapper)
    {
        this.findTurmasUseCase = findTurmasUseCase;
        this.deleteTurmaUseCase = deleteTurmaUseCase;
        this.insertTurmaUseCase = insertTurmaUseCase;
        this.updateTurmaUseCase = updateTurmaUseCase;
        this.objectMapper = objectMapper;
    }

    @GetMapping("/listar-turmas-informacao-completa/paginacao/{paginacao}")
    public ResponseEntity<List<FullResultSetTurmaResponse>> listarTodasAsTurmas(
    		@PathVariable(value = "paginacao") int paginacao, 
    		@RequestBody HashMap<String, Object> jsonIntervaloDataTurmas
    		)
    {
        LOGGER.info("O serviço de listagem de turmas foi chamado em: " + this.getClass().getName());
        return ResponseEntity.ok(findTurmasUseCase.listarTodasAsTurmas(paginacao, jsonIntervaloDataTurmas.get("dt_inicio").toString(), jsonIntervaloDataTurmas.get("dt_fim").toString()));
    }
    
    @GetMapping("/listar-turmas-informacao-basica/paginacao/{paginacao}")
    public ResponseEntity<List<TurmaInformacoesBasicasResponse>> listarTurmasInformacaoBasica(@PathVariable(value = "paginacao") int paginacao){
    	LOGGER.info("O serviço de listagem básico de turmas foi chamado em: " + this.getClass().getName());
    	return ResponseEntity.ok(findTurmasUseCase.listarTurmasBasico(paginacao));
    }
    
    @GetMapping("/listar-turmas-vinculadas/codigo-curso/{codigo_curso}/paginacao/{paginacao}")
    public ResponseEntity<List<HashMap<String, Object>>> listarTurmasVinculadasCurso(
    		@PathVariable(value = "codigo_curso") int codigoCurso, 
    		@PathVariable(value = "paginacao") int paginacao
    		){
    	LOGGER.info("O serviço de listagem de turmas vinculadas foi chamado em: " + this.getClass().getName());
    	return ResponseEntity.ok(findTurmasUseCase.listarTurmasVinculadasCurso(codigoCurso, paginacao));
    }
    
    @DeleteMapping("/deletar-turma/{codigo_turma}")
    public ResponseEntity<HashMap<String, Object>> deletarTurmaDefinitivo(@PathVariable(value = "codigo_turma") int codigoTurma){
    	LOGGER.info("O serviço de remoção definitiva de turmas foi chamado em: " + this.getClass().getName() + " deletando a turma: " + codigoTurma);
    	return ResponseEntity.ok(deleteTurmaUseCase.deletarTurma(codigoTurma));
    }
    
    @PostMapping("/salvar-turma")
    public ResponseEntity<Void> salvarTurma(@RequestBody final TurmaRequest turmaJsonRequest) throws JsonProcessingException{
    	LOGGER.info("O serviço de cadastro de turmas foi chamado em: " + this.getClass().getName() + " passando a turma: " + objectMapper.writeValueAsString(turmaJsonRequest));
    	insertTurmaUseCase.inserir(turmaJsonRequest);
    	return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    
    @PutMapping("/atualizar-informacoes-turma/{codigo_turma}")
    public ResponseEntity<TurmaInformacoesBasicasResponse> atualizarTurma(
    		@RequestBody final TurmaAtualizacaoRequest turmaAtualizacaoJsonRequest, 
    		@PathVariable(value = "codigo_turma") int codigoTurma)
    {
    	LOGGER.info("O serviço de atualização de turmas foi chamado em: " + this.getClass().getName());
    	return ResponseEntity.ok(updateTurmaUseCase.atualizarTurma(codigoTurma, turmaAtualizacaoJsonRequest));
    }
}
