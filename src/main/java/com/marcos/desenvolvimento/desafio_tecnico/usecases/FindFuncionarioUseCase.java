package com.marcos.desenvolvimento.desafio_tecnico.usecases;

import com.marcos.desenvolvimento.desafio_tecnico.entity.Funcionario;
import com.marcos.desenvolvimento.desafio_tecnico.repository.DAOFuncionario;
import com.marcos.desenvolvimento.desafio_tecnico.response.FuncionarioResponse;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Service
public class FindFuncionarioUseCase {

    private final DAOFuncionario daoFuncionario;

    public FindFuncionarioUseCase(DAOFuncionario daoFuncionario){
        this.daoFuncionario = daoFuncionario;
    }

    @Cacheable(value = "funcionarios")
    public List<Funcionario> encontrarPorNome(final String nomeFuncionario){
        if(nomeFuncionario != null && !nomeFuncionario.isBlank()){
            return daoFuncionario.buscarFuncionarioPorNome(nomeFuncionario);
        }
        throw new IllegalArgumentException("Erro ao processar o método encontrarPorNome em: " + this.getClass().getName());
    }

    @Cacheable(value = "funcionarios_ativos")
    public List<Funcionario> listarAtivos(final int paginacao){
        return daoFuncionario.buscarFuncionariosAtivos(paginacao);
    }

    @Cacheable(value = "funcionarios-inativos")
    public List<Funcionario> listarInativos(final int paginacao){
        return daoFuncionario.buscarFuncionariosInativos(paginacao);
    }
    
    public Funcionario buscarFuncionarioPorCodigo(final int codigo) {
    	if(codigo > 0) {
    		return daoFuncionario.buscarFuncionarioPorCodigo(codigo);
    	}
    	return null;
    }

}
