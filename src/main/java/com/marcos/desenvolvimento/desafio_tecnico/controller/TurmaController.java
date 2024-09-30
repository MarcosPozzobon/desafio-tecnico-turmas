package com.marcos.desenvolvimento.desafio_tecnico.controller;

import com.marcos.desenvolvimento.desafio_tecnico.response.FullResultSetTurmaResponse;
import com.marcos.desenvolvimento.desafio_tecnico.response.TurmaInformacoesBasicasResponse;
import com.marcos.desenvolvimento.desafio_tecnico.usecases.FindTurmasUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/turmas")
public class TurmaController {

    private final FindTurmasUseCase findTurmasUseCase;

    private static final Logger LOGGER = LoggerFactory.getLogger(CursoController.class);

    public TurmaController(FindTurmasUseCase findTurmasUseCase){
        this.findTurmasUseCase = findTurmasUseCase;
    }

    @GetMapping("listar-turmas-informacao-completa/{paginacao}")
    public ResponseEntity<List<FullResultSetTurmaResponse>> listarTodasAsTurmas(@PathVariable(value = "paginacao") int paginacao){
        LOGGER.info("O serviço de listagem de turmas foi chamado em: " + this.getClass().getName());
        return ResponseEntity.ok(findTurmasUseCase.listarTodasAsTurmas(paginacao));
    }
    
    @GetMapping("listar-turmas-informacao-basica/{paginacao}")
    public ResponseEntity<List<TurmaInformacoesBasicasResponse>> listarTurmasInformacaoBasica(@PathVariable(value = "paginacao") int paginacao){
    	LOGGER.info("O serviço de listagem básico de turmas foi chamado em: " + this.getClass().getName());
    	return ResponseEntity.ok(findTurmasUseCase.listarTurmasBasico(paginacao));
    }
   
}
