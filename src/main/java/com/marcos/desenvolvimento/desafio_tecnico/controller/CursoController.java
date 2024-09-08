package com.marcos.desenvolvimento.desafio_tecnico.controller;

import com.marcos.desenvolvimento.desafio_tecnico.repository.DAOCurso;
import com.marcos.desenvolvimento.desafio_tecnico.response.CursoResponse;
import com.marcos.desenvolvimento.desafio_tecnico.usecases.InsertCursoUseCase;
import com.marcos.desenvolvimento.desafio_tecnico.usecases.UpdateCursoUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.marcos.desenvolvimento.desafio_tecnico.request.CursoRequest;


import java.time.LocalDateTime;
import java.util.HashMap;

@RestController
@RequestMapping(value = "/api/v1/cursos")
public class CursoController {

    private final InsertCursoUseCase insertCursoUseCase;

    private final UpdateCursoUseCase updateCursoUseCase;
    public CursoController(InsertCursoUseCase insertCursoUseCase, UpdateCursoUseCase updateCursoUseCase){
        this.insertCursoUseCase = insertCursoUseCase;
        this.updateCursoUseCase = updateCursoUseCase;
    }
    private static final Logger LOGGER = LoggerFactory.getLogger(CursoController.class);

    @GetMapping(value = "/teste")
    public HashMap<String, Object> teste(){
        HashMap<String, Object> jsonResponse = new HashMap<String, Object>();
        jsonResponse.put("Status", HttpStatus.OK);
        return jsonResponse;
    }

    @PostMapping(value = "/salvar-novo-curso")
    public ResponseEntity<Void> salvarNovoCurso(@RequestBody CursoRequest cursoRequest){
        insertCursoUseCase.salvar(cursoRequest);
        LOGGER.info("O serviço de salvar um novo curso foi chamado em: " + this.getClass().getName());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping(value = "/atualizar-curso/{codigo_curso}")
    public ResponseEntity<CursoResponse> atualizarCurso(@PathVariable(value = "codigo_curso") int codigoCurso, @RequestBody CursoRequest cursoRequest) {
        updateCursoUseCase.atualizarCurso(codigoCurso, cursoRequest);
        LOGGER.info("O serviço de atualização de nome de curso foi chamado em: " + this.getClass().getName() + " no seguinte horário: " + LocalDateTime.now());
        return ResponseEntity.ok().build();
    }


}
