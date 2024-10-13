package com.marcos.desenvolvimento.desafio_tecnico.controller;

import com.marcos.desenvolvimento.desafio_tecnico.mapper.CursoMapper;
import com.marcos.desenvolvimento.desafio_tecnico.response.CursoResponse;
import com.marcos.desenvolvimento.desafio_tecnico.usecases.*;
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
    private final ChangeCourseStatusUseCase changeCourseStatusUseCase;
    private final DeleteCursoUseCase deleteCursoUseCase;
    private final CursoMapper cursoMapper;

    private static final Logger LOGGER = LoggerFactory.getLogger(CursoController.class);

    public CursoController(
            InsertCursoUseCase insertCursoUseCase,
            UpdateCursoUseCase updateCursoUseCase,
            CursoMapper cursoMapper,
            ListCursosUseCase listCursosUseCase,
            ChangeCourseStatusUseCase changeCourseStatusUseCase,
            DeleteCursoUseCase deleteCursoUseCase
    ) {
        this.insertCursoUseCase = insertCursoUseCase;
        this.updateCursoUseCase = updateCursoUseCase;
        this.cursoMapper = cursoMapper;
        this.listCursosUseCase = listCursosUseCase;
        this.changeCourseStatusUseCase = changeCourseStatusUseCase;
        this.deleteCursoUseCase = deleteCursoUseCase;
    }

    @GetMapping(value = "/teste")
    public HashMap<String, Object> teste() {
        HashMap<String, Object> jsonResponse = new HashMap<>();
        jsonResponse.put("Status", HttpStatus.OK);
        jsonResponse.put("Horário", LocalDateTime.now());
        return jsonResponse;
    }

    @GetMapping
    public ResponseEntity<List<CursoResponse>> listarTodosOsCursos() {
        LOGGER.info("O serviço de listar todos os cursos foi chamado em: " + this.getClass().getName() + " no seguinte horário: " + LocalDateTime.now());
        return ResponseEntity.ok(cursoMapper.toCursoResponseList(listCursosUseCase.listarTodos()));
    }

    @PostMapping
    public ResponseEntity<Void> criarCurso(@RequestBody CursoRequest cursoRequest) {
        insertCursoUseCase.salvar(cursoRequest);
        LOGGER.info("O serviço de criar um novo curso foi chamado em: " + this.getClass().getName());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{codigoCurso}")
    public ResponseEntity<CursoResponse> atualizarCurso(@PathVariable(value = "codigoCurso") int codigoCurso, @RequestBody CursoRequest cursoRequest) {
        var cursoAtualizado = cursoMapper.toCursoResponse(updateCursoUseCase.atualizarCurso(codigoCurso, cursoRequest));
        LOGGER.info("O serviço de atualização de curso foi chamado em: " + this.getClass().getName() + " no seguinte horário: " + LocalDateTime.now());
        return ResponseEntity.ok(cursoAtualizado);
    }

    @PatchMapping("/{codigoCurso}/status")
    public ResponseEntity<Void> alternarStatusCurso(@PathVariable(value = "codigoCurso") final int codigoCurso, @RequestParam(value = "ativo") boolean ativo) {
        if (ativo) {
            changeCourseStatusUseCase.ativarCurso(codigoCurso);
            LOGGER.info("O serviço de ativação de status de curso foi chamado em: " + this.getClass().getName());
        } else {
            changeCourseStatusUseCase.inativarCurso(codigoCurso);
            LOGGER.info("O serviço de inativação de status de curso foi chamado em: " + this.getClass().getName());
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/{codigoCurso}")
    public ResponseEntity<Void> deletarCurso(@PathVariable(value = "codigoCurso") final int codigoCurso) {
        deleteCursoUseCase.deletarCursoSemForce(codigoCurso);
        LOGGER.warn("O serviço de exclusão total de curso foi chamado em: " + this.getClass().getName());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/{codigoCurso}/force")
    public ResponseEntity<Void> deletarCursoComForce(@PathVariable(value = "codigoCurso") final int codigoCurso, @RequestParam(value = "force") String force) {
        deleteCursoUseCase.deletarCursoComForce(codigoCurso, force);
        LOGGER.warn("O serviço de exclusão total de curso, participantes e turmas foi chamado em: " + this.getClass().getName());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
