package com.marcos.desenvolvimento.desafio_tecnico.usecases;

import com.marcos.desenvolvimento.desafio_tecnico.repository.DAOTurmas;
import com.marcos.desenvolvimento.desafio_tecnico.response.FullResultSetTurmaResponse;
import com.marcos.desenvolvimento.desafio_tecnico.response.TurmaInformacoesBasicasResponse;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FindTurmasUseCase {

    private final DAOTurmas daoTurmas;

    public FindTurmasUseCase(DAOTurmas daoTurmas){
        this.daoTurmas = daoTurmas;
    }

    @Cacheable(value = "turmas_sem_filtro_result_set")
    public List<FullResultSetTurmaResponse> listarTodasAsTurmas(int paginacao){
        return daoTurmas.buscarTurmas(paginacao);
    }
    
    @Cacheable(value = "turmas_info_basica_result_set")
    public List<TurmaInformacoesBasicasResponse> listarTurmasBasico(int paginacao){
    	return daoTurmas.listarTurmasInformacaoBasica(paginacao);
    }

}
