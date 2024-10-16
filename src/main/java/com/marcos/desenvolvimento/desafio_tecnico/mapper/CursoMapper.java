package com.marcos.desenvolvimento.desafio_tecnico.mapper;

import com.marcos.desenvolvimento.desafio_tecnico.entity.Curso;
import com.marcos.desenvolvimento.desafio_tecnico.response.CursoResponse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CursoMapper {

    public CursoResponse toCursoResponse(Curso curso) {
        if (curso == null) {
            throw new IllegalArgumentException("O curso não pode ser nulo.");
        }
        CursoResponse cursoResponse = new CursoResponse();

        if(curso.getNome() != null && !curso.getNome().isEmpty()){
            cursoResponse.setNome(curso.getNome());
        }

        if(curso.getDescricao() != null && !curso.getDescricao().isEmpty()){
            cursoResponse.setDescricao(curso.getDescricao());
        }

        if(curso.getDuracao() > 0){
            cursoResponse.setDuracao(curso.getDuracao());
        }

        if(curso.getIsAtivo() != null && !curso.getIsAtivo().isEmpty()){
            cursoResponse.setIsAtivo(curso.getIsAtivo());
        }

        return cursoResponse;
    }

    public List<CursoResponse> toCursoResponseList(List<Curso> listaCursos){
        return listaCursos
                .stream()
                .map(this::toCursoResponse)
                .toList();
    }

}
