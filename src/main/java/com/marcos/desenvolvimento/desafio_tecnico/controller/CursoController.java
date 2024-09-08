package com.marcos.desenvolvimento.desafio_tecnico.controller;

import com.marcos.desenvolvimento.desafio_tecnico.repository.DAOCurso;
import com.marcos.desenvolvimento.desafio_tecnico.usecases.InsertCursoUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.marcos.desenvolvimento.desafio_tecnico.request.CursoRequest;


import java.util.HashMap;

@RestController
@RequestMapping(value = "/api/v1/cursos")
public class CursoController {

    private final InsertCursoUseCase insertCursoUseCase;
    public CursoController(InsertCursoUseCase insertCursoUseCase){
        this.insertCursoUseCase = insertCursoUseCase;
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
        insertCursoUseCase.salvarNovoCurso(cursoRequest);
        LOGGER.info("O serviço de salvar um novo curso foi chamado em: " + this.getClass().getName());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
