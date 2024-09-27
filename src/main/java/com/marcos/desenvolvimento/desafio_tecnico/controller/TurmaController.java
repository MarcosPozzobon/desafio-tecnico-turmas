package com.marcos.desenvolvimento.desafio_tecnico.controller;

import com.marcos.desenvolvimento.desafio_tecnico.repository.DAOTurmas;
import com.marcos.desenvolvimento.desafio_tecnico.response.FullResultSetTurmaResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/turmas")
public class TurmaController {


    @Autowired
    DAOTurmas daoTurmas;

    @GetMapping("listar-turmas")
    public ResponseEntity<List<FullResultSetTurmaResponse>> listarTodasAsTurmas(){
        return ResponseEntity.ok(daoTurmas.buscarTurmas(10));
    }

}
