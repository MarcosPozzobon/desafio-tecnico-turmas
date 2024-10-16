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

    @PostMapping
    public ResponseEntity<Void> criarFuncionario(@RequestBody final FuncionarioRequest funcionarioRequest) {
        insertFuncionarioUseCase.salvar(funcionarioRequest);
        LOGGER.info("Criado um novo funcionário em: " + this.getClass().getName());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{codigoFuncionario}")
    public ResponseEntity<Void> atualizarFuncionario(
            @RequestBody final FuncionarioRequest funcionarioRequest,
            @PathVariable("codigoFuncionario") int codigoFuncionario
    ) {
        updateFuncionarioUseCase.atualizar(funcionarioRequest, codigoFuncionario);
        LOGGER.info("Atualizado funcionário com código {} em: {}", codigoFuncionario, this.getClass().getName());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/nome/{nomeFuncionario}")
    public ResponseEntity<List<FuncionarioResponse>> buscarFuncionariosPorNome(@PathVariable("nomeFuncionario") final String nomeFuncionario) {
        LOGGER.info("Buscando funcionários por nome em: " + this.getClass().getName());
        return ResponseEntity.ok(funcionarioMapper.toFuncionarioResponseList(findFuncionarioUseCase.encontrarPorNome(nomeFuncionario)));
    }

    @GetMapping("/ativos/paginacao/{paginacao}")
    public ResponseEntity<List<FuncionarioResponse>> listarFuncionariosAtivos(@PathVariable("paginacao") int paginacao) {
        LOGGER.info("Listando funcionários ativos em: " + this.getClass().getName());
        return ResponseEntity.ok(funcionarioMapper.toFuncionarioResponseList(findFuncionarioUseCase.listarAtivos(paginacao)));
    }

    @GetMapping("/inativos/paginacao/{paginacao}")
    public ResponseEntity<List<FuncionarioResponse>> listarFuncionariosInativos(@PathVariable("paginacao") int paginacao) {
        LOGGER.info("Listando funcionários inativos em: " + this.getClass().getName());
        return ResponseEntity.ok(funcionarioMapper.toFuncionarioResponseList(findFuncionarioUseCase.listarInativos(paginacao)));
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<FuncionarioResponse> obterFuncionarioPorCodigo(@PathVariable("codigo") final int codigo) {
        LOGGER.info("Buscando funcionário por código em: " + this.getClass().getName());
        return ResponseEntity.ok(funcionarioMapper.toFuncionarioResponse(findFuncionarioUseCase.buscarFuncionarioPorCodigo(codigo)));
    }
}
