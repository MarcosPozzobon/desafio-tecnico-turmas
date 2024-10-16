package com.marcos.desenvolvimento.desafio_tecnico.usecases;

import com.marcos.desenvolvimento.desafio_tecnico.entity.Curso;
import com.marcos.desenvolvimento.desafio_tecnico.repository.DAOCurso;
import com.marcos.desenvolvimento.desafio_tecnico.request.CursoRequest;
import org.springframework.stereotype.Service;

@Service
public class UpdateCursoUseCase {

    private final DAOCurso daoCurso;

    public UpdateCursoUseCase(DAOCurso daoCurso){
        this.daoCurso = daoCurso;
    }

    public Curso atualizarCurso(int codigoCurso, CursoRequest cursoRequest){
        Curso cursoNomeAtualizado = new Curso();

        if(cursoRequest.getNome() != null || !cursoRequest.getNome().isEmpty()){
            cursoNomeAtualizado.setNome(cursoRequest.getNome());
        }
        if(cursoRequest.getDescricao() != null || !cursoRequest.getDescricao().isEmpty()){
            cursoNomeAtualizado.setDescricao(cursoRequest.getDescricao());
        }
        if(cursoRequest.getDuracao() > 0){
            cursoNomeAtualizado.setDuracao(cursoRequest.getDuracao());
        }
        daoCurso.atualizarNomeCurso(cursoNomeAtualizado, codigoCurso);
        return cursoNomeAtualizado;
    }

}
