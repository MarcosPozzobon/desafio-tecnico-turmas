package com.marcos.desenvolvimento.desafio_tecnico.usecases;

import com.marcos.desenvolvimento.desafio_tecnico.controller.FuncionarioController;
import com.marcos.desenvolvimento.desafio_tecnico.entity.Funcionario;
import com.marcos.desenvolvimento.desafio_tecnico.mapper.FuncionarioMapper;
import com.marcos.desenvolvimento.desafio_tecnico.repository.DAOFuncionario;
import com.marcos.desenvolvimento.desafio_tecnico.request.FuncionarioRequest;
import com.marcos.desenvolvimento.desafio_tecnico.response.FuncionarioResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class UpdateFuncionarioUseCase {

    private final DAOFuncionario daoFuncionario;

    private final FuncionarioMapper mapper;

    private static final Logger LOGGER = LoggerFactory.getLogger(UpdateFuncionarioUseCase.class);

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public UpdateFuncionarioUseCase(DAOFuncionario daoFuncionario, FuncionarioMapper mapper){
        this.daoFuncionario = daoFuncionario;
        this.mapper = mapper;
    }

    @Transactional
    public void atualizar(final FuncionarioRequest funcionarioRequest, int codigoFuncionario){
        daoFuncionario.atualizarFuncionario(funcionarioRequest, codigoFuncionario);
    }

}
