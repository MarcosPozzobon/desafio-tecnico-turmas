package com.marcos.desenvolvimento.desafio_tecnico.controller;

import com.marcos.desenvolvimento.desafio_tecnico.mapper.CursoMapper;
import com.marcos.desenvolvimento.desafio_tecnico.response.CursoResponse;
import com.marcos.desenvolvimento.desafio_tecnico.usecases.InsertCursoUseCase;
import com.marcos.desenvolvimento.desafio_tecnico.usecases.ListCursosUseCase;
import com.marcos.desenvolvimento.desafio_tecnico.usecases.UpdateCursoUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.marcos.desenvolvimento.desafio_tecnico.request.CursoRequest;


import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/cursos")
public class CursoController {

    private final InsertCursoUseCase insertCursoUseCase;

    private final UpdateCursoUseCase updateCursoUseCase;

    private final ListCursosUseCase listCursosUseCase;

    private final CursoMapper cursoMapper;

    public CursoController(InsertCursoUseCase insertCursoUseCase,
                           UpdateCursoUseCase updateCursoUseCase,
                           CursoMapper cursoMapper,
                           ListCursosUseCase listCursosUseCase
    )
    {
        this.insertCursoUseCase = insertCursoUseCase;
        this.updateCursoUseCase = updateCursoUseCase;
        this.cursoMapper = cursoMapper;
        this.listCursosUseCase = listCursosUseCase;
    }
    private static final Logger LOGGER = LoggerFactory.getLogger(CursoController.class);

    @GetMapping(value = "/teste")
    public HashMap<String, Object> teste(){
        HashMap<String, Object> jsonResponse = new HashMap<String, Object>();
        jsonResponse.put("Status", HttpStatus.OK);
        jsonResponse.put("Horário", LocalDateTime.now());
        return jsonResponse;
    }

    @GetMapping(value = "/listar-todos-os-cursos")
    public ResponseEntity<List<CursoResponse>> listarTodosOsCursos(){
        LOGGER.info("O serviço de listar todos os cursos foi chamado em: " + this.getClass().getName() + " no seguinte horário: " + LocalDateTime.now());
        return ResponseEntity.ok(cursoMapper.toCursoResponseList(listCursosUseCase.listarTodos()));
    }

    @PostMapping(value = "/salvar-novo-curso")
    public ResponseEntity<Void> salvarNovoCurso(@RequestBody CursoRequest cursoRequest){
        insertCursoUseCase.salvar(cursoRequest);
        LOGGER.info("O serviço de salvar um novo curso foi chamado em: " + this.getClass().getName());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping(value = "/atualizar-curso/{codigo_curso}")
    public ResponseEntity<CursoResponse> atualizarCurso(@PathVariable(value = "codigo_curso") int codigoCurso, @RequestBody CursoRequest cursoRequest) {
        var cursoAtualizado = cursoMapper.toCursoResponse(updateCursoUseCase.atualizarCurso(codigoCurso, cursoRequest));
        LOGGER.info("O serviço de atualização de nome de curso foi chamado em: " + this.getClass().getName() + " no seguinte horário: " + LocalDateTime.now());
        return ResponseEntity.ok(cursoAtualizado);
    }
}
