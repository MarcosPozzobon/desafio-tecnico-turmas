package com.marcos.desenvolvimento.desafio_tecnico.usecases;

import com.marcos.desenvolvimento.desafio_tecnico.entity.Funcionario;
import com.marcos.desenvolvimento.desafio_tecnico.repository.DAOFuncionario;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

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
}
