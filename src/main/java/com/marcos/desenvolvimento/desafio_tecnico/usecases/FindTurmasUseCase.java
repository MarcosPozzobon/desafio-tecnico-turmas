package com.marcos.desenvolvimento.desafio_tecnico.usecases;

import com.marcos.desenvolvimento.desafio_tecnico.repository.DAOTurmas;
import com.marcos.desenvolvimento.desafio_tecnico.response.FullResultSetTurmaResponse;
import com.marcos.desenvolvimento.desafio_tecnico.response.TurmaInformacoesBasicasResponse;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class FindTurmasUseCase {

    private final DAOTurmas daoTurmas;

    public FindTurmasUseCase(DAOTurmas daoTurmas){
        this.daoTurmas = daoTurmas;
    }

    @Cacheable(value = "listagem-completa-turmas")
    public List<FullResultSetTurmaResponse> listarTodasAsTurmas(int paginacao, String dataInicial, String dataFinal){
        return daoTurmas.buscarTurmas(dataInicial, dataFinal, paginacao);
    }

    @Cacheable(value = "listagem-basica-turmas")
    public List<TurmaInformacoesBasicasResponse> listarTurmasBasico(int paginacao){
    	return daoTurmas.listarTurmasInformacaoBasica(paginacao);
    }

    @Cacheable(value = "listagem-curso-vinculo-turmas")
    public List<HashMap<String, Object>> listarTurmasVinculadasCurso(int codigoCurso, int paginacao){
    	if(codigoCurso > 0) {
    		return daoTurmas.listarTurmasVinculadas(codigoCurso, paginacao);
    	}
    	return null;
    }

}
