package com.marcos.desenvolvimento.desafio_tecnico.controller;

import com.marcos.desenvolvimento.desafio_tecnico.request.FuncionarioRequest;
import com.marcos.desenvolvimento.desafio_tecnico.usecases.InsertFuncionarioUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/funcionarios")
public class FuncionarioController {

    private final InsertFuncionarioUseCase insertFuncionarioUseCase;

    private static final Logger LOGGER = LoggerFactory.getLogger(FuncionarioController.class);

    private FuncionarioController(InsertFuncionarioUseCase insertFuncionarioUseCase){
        this.insertFuncionarioUseCase = insertFuncionarioUseCase;
    }

    @PostMapping(value = "/salvar-novo-funcionario")
    public ResponseEntity<Void> salvarNovoFuncionario(@RequestBody final FuncionarioRequest funcionarioRequest){
        insertFuncionarioUseCase.salvar(funcionarioRequest);
        LOGGER.info("O serviço de salvar um novo funcionário foi chamado em: " + this.getClass().getName());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
