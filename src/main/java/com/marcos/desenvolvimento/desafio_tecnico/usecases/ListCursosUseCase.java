package com.marcos.desenvolvimento.desafio_tecnico.usecases;

import com.marcos.desenvolvimento.desafio_tecnico.entity.Curso;
import com.marcos.desenvolvimento.desafio_tecnico.repository.DAOCurso;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListCursosUseCase {

    private final DAOCurso daoCurso;

    private ListCursosUseCase(DAOCurso daoCurso){
        this.daoCurso = daoCurso;
    }

    public List<Curso> listarTodos(){
        return daoCurso.listarTodosOsCursos();
    }

}
