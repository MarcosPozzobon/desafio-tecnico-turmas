package com.marcos.desenvolvimento.desafio_tecnico.usecases;

import com.marcos.desenvolvimento.desafio_tecnico.repository.DAOCurso;
import org.springframework.stereotype.Service;

@Service
public class ChangeCourseStatusUseCase {

    private final DAOCurso daoCurso;

    public ChangeCourseStatusUseCase(DAOCurso daoCurso){
        this.daoCurso = daoCurso;
    }

    public void inativarCurso(int codigoCurso){
        daoCurso.inativarCurso(codigoCurso);
    }

    public void ativarCurso(int codigoCurso){
        daoCurso.ativarCurso(codigoCurso);
    }

}
