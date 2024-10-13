package com.marcos.desenvolvimento.desafio_tecnico.usecases;

import com.marcos.desenvolvimento.desafio_tecnico.repository.DAOCurso;
import org.springframework.stereotype.Service;

@Service
public class DeleteCursoUseCase {

    private final DAOCurso daoCurso;

    public DeleteCursoUseCase(DAOCurso daoCurso){
        this.daoCurso = daoCurso;
    }

    public void deletarCursoSemForce(final int codigoCurso){
        if(codigoCurso > 0){
            daoCurso.deletarCurso(codigoCurso);
        }
    }

    public void deletarCursoComForce(final int codigoCurso, final String force){
        if(codigoCurso > 0 && force != null && !force.isEmpty()){
            daoCurso.deletarCursoComForce(codigoCurso, force);
        }
    }
}
