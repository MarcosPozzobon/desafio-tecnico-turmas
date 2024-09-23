package com.marcos.desenvolvimento.desafio_tecnico.usecases;

import com.marcos.desenvolvimento.desafio_tecnico.controller.FuncionarioController;
import com.marcos.desenvolvimento.desafio_tecnico.entity.Funcionario;
import com.marcos.desenvolvimento.desafio_tecnico.mapper.FuncionarioMapper;
import com.marcos.desenvolvimento.desafio_tecnico.repository.DAOFuncionario;
import com.marcos.desenvolvimento.desafio_tecnico.request.FuncionarioRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UpdateFuncionarioUseCase {

    private final DAOFuncionario daoFuncionario;

    private final FuncionarioMapper mapper;

    private static final Logger LOGGER = LoggerFactory.getLogger(UpdateFuncionarioUseCase.class);

    public UpdateFuncionarioUseCase(DAOFuncionario daoFuncionario, FuncionarioMapper mapper){
        this.daoFuncionario = daoFuncionario;
        this.mapper = mapper;
    }

    @Transactional
    public Funcionario atualizar(FuncionarioRequest funcionarioRequest, int codigoFuncionario){
        if(daoFuncionario.isFuncionarioAtualAtivo(codigoFuncionario)){
            Funcionario funcionario = new Funcionario();
            if(funcionarioRequest.getNome() != null && !funcionarioRequest.getNome().isEmpty()){
                funcionario.setNome(funcionarioRequest.getNome());
            }
            if(funcionarioRequest.getDtNascimento() != null){
                funcionario.setDtNascimento(funcionarioRequest.getDtNascimento());
            }
            if(funcionarioRequest.getDtAdmissao() != null){
                funcionario.setDtAdmissao(funcionarioRequest.getDtAdmissao());
            }
            if(funcionarioRequest.getCpf().length() == 11){
                funcionario.setCpf(funcionarioRequest.getCpf());
            }
            if(funcionarioRequest.getCargo() != null && !funcionarioRequest.getCargo().isEmpty()){
                funcionario.setCargo(funcionarioRequest.getCargo());
            }
            if(codigoFuncionario > 0){
                funcionario.setCodigoFuncionario(codigoFuncionario);
            }
            daoFuncionario.atualizarFuncionario(mapper.toFuncionarioRequest(funcionario), codigoFuncionario);
            return funcionario;
        }
        LOGGER.error("Argumentos recebidos para atualização de funcionário: ", funcionarioRequest.getNome(), funcionarioRequest.getCargo(),
                funcionarioRequest.getCpf(), funcionarioRequest.getDtNascimento(), funcionarioRequest.getDtAdmissao());
        return null;
    }

}
