package com.marcos.desenvolvimento.desafio_tecnico.usecases;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.marcos.desenvolvimento.desafio_tecnico.repository.DAOTurmaParticipante;
import com.marcos.desenvolvimento.desafio_tecnico.response.TurmaParticipanteResponse;

@Service
public class FindParticipantesUseCase {
	
	private final DAOTurmaParticipante daoTurmaParticipante;
	
	public FindParticipantesUseCase(DAOTurmaParticipante daoTurmaParticipante) {
		this.daoTurmaParticipante = daoTurmaParticipante;
	}
	
	@Cacheable(value = "listagem-completa-participantes")
	public List<TurmaParticipanteResponse> listarParticipantes(int paginacao){
		return daoTurmaParticipante.listarTodosOsAlunos(paginacao);
	}
	
}
