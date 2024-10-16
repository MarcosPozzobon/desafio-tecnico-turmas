package com.marcos.desenvolvimento.desafio_tecnico.usecases;

import com.marcos.desenvolvimento.desafio_tecnico.entity.Curso;
import com.marcos.desenvolvimento.desafio_tecnico.repository.DAOCurso;
import com.marcos.desenvolvimento.desafio_tecnico.request.CursoRequest;
import org.springframework.stereotype.Service;

@Service
public class InsertCursoUseCase {

    private final DAOCurso daoCurso;

    public InsertCursoUseCase(DAOCurso daoCurso) {
        this.daoCurso = daoCurso;
    }

    public void salvar(final CursoRequest cursoRequest) {
        Curso curso = new Curso();
        if(cursoRequest.getNome() != null || !cursoRequest.getNome().isEmpty()){
            curso.setNome(cursoRequest.getNome());
        }
        if(cursoRequest.getDuracao() > 0) {
            curso.setDuracao(cursoRequest.getDuracao());
        }
        if(cursoRequest.getDescricao() != null || !cursoRequest.getDescricao().isEmpty()){
            curso.setDescricao(cursoRequest.getDescricao());
        }
        curso.setIsAtivo("true");
        daoCurso.salvarCurso(curso);
    }

}
