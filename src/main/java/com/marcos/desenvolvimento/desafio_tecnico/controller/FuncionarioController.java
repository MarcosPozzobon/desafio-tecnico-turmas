package com.marcos.desenvolvimento.desafio_tecnico.controller;

import com.marcos.desenvolvimento.desafio_tecnico.entity.Funcionario;
import com.marcos.desenvolvimento.desafio_tecnico.mapper.FuncionarioMapper;
import com.marcos.desenvolvimento.desafio_tecnico.request.FuncionarioRequest;
import com.marcos.desenvolvimento.desafio_tecnico.response.FuncionarioResponse;
import com.marcos.desenvolvimento.desafio_tecnico.usecases.FindFuncionarioUseCase;
import com.marcos.desenvolvimento.desafio_tecnico.usecases.InsertFuncionarioUseCase;
import com.marcos.desenvolvimento.desafio_tecnico.usecases.UpdateFuncionarioUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/funcionarios")
public class FuncionarioController {

    private final InsertFuncionarioUseCase insertFuncionarioUseCase;

    private final UpdateFuncionarioUseCase updateFuncionarioUseCase;

    private final FindFuncionarioUseCase findFuncionarioUseCase;

    private final FuncionarioMapper funcionarioMapper;

    private static final Logger LOGGER = LoggerFactory.getLogger(FuncionarioController.class);

    public FuncionarioController(
            InsertFuncionarioUseCase insertFuncionarioUseCase,
            UpdateFuncionarioUseCase updateFuncionarioUseCase,
            FindFuncionarioUseCase findFuncionarioUseCase,
            FuncionarioMapper funcionarioMapper
    ){
        this.insertFuncionarioUseCase = insertFuncionarioUseCase;
        this.updateFuncionarioUseCase = updateFuncionarioUseCase;
        this.findFuncionarioUseCase = findFuncionarioUseCase;
        this.funcionarioMapper = funcionarioMapper;
    }

    @PostMapping(value = "/salvar-novo-funcionario")
    public ResponseEntity<Void> salvarNovoFuncionario(@RequestBody final FuncionarioRequest funcionarioRequest){
        insertFuncionarioUseCase.salvar(funcionarioRequest);
        LOGGER.info("O serviço de salvar um novo funcionário foi chamado em: " + this.getClass().getName());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping(value = "/atualizar-funcionario/{codigo_funcionario}")
    public ResponseEntity<Funcionario> atualizarFuncionarioExistente(
            @RequestBody final FuncionarioRequest funcionarioRequest,
            @PathVariable("codigo_funcionario") int codigoFuncionario
    ) {
        updateFuncionarioUseCase.atualizar(funcionarioRequest, codigoFuncionario);
        LOGGER.info("O serviço de atualização de informações de funcionário foi chamado em: " + this.getClass().getName());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/encontrar-funcionario-por-nome/{nome_funcionario}")
    public ResponseEntity<List<FuncionarioResponse>> encontrarFuncionarioPorNome(@PathVariable(value = "nome_funcionario") final String nomeFuncionario){
        LOGGER.info("O serviço de busca de funcionário por nome foi chamado em: " + this.getClass().getName());
        return ResponseEntity.ok(funcionarioMapper.toFuncionarioResponseList(findFuncionarioUseCase.encontrarPorNome(nomeFuncionario)));
    }

    @GetMapping("/listar-funcionarios-ativos/paginacao/{paginacao}")
    public ResponseEntity<List<FuncionarioResponse>> listarFuncionariosAtivos(@PathVariable(value = "paginacao") int paginacao){
        LOGGER.info("O serviço de listagem por funcionários ativos foi chamado em: " + this.getClass().getName());
        return ResponseEntity.ok(funcionarioMapper.toFuncionarioResponseList(findFuncionarioUseCase.listarAtivos(paginacao)));
    }

    @GetMapping("/listar-funcionarios-inativos/paginacao/{paginacao}")
    public ResponseEntity<List<FuncionarioResponse>> listarFuncionariosInativos(@PathVariable(value = "paginacao") int paginacao){
        LOGGER.info("O serviço de listagem por funcionários inativos foi chamado em: " + this.getClass().getName());
        return ResponseEntity.ok(funcionarioMapper.toFuncionarioResponseList(findFuncionarioUseCase.listarInativos(paginacao)));
    }
    
    @GetMapping("/listar-funcionario/{codigo}")
    public ResponseEntity<FuncionarioResponse> obterFuncionarioPorCodigo(@PathVariable(value = "codigo") final int codigo){
    	LOGGER.info("O serviço de busca de funcionário por código foi chamado em: " + this.getClass().getName());
    	return ResponseEntity.ok(funcionarioMapper.toFuncionarioResponse(findFuncionarioUseCase.buscarFuncionarioPorCodigo(codigo)));
    }
}
