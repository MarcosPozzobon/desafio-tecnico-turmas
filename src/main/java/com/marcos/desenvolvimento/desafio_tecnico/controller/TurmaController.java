package com.marcos.desenvolvimento.desafio_tecnico.controller;

import com.marcos.desenvolvimento.desafio_tecnico.response.FullResultSetTurmaResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/turmas")
public class TurmaController {


    @GetMapping("listar-turmas")
    public ResponseEntity<FullResultSetTurmaResponse> listarTodasAsTurmas(){
        return null;
    }



}
