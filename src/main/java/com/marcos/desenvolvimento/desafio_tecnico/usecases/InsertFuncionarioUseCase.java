package com.marcos.desenvolvimento.desafio_tecnico.usecases;

import com.marcos.desenvolvimento.desafio_tecnico.entity.Funcionario;
import com.marcos.desenvolvimento.desafio_tecnico.repository.DAOFuncionario;
import com.marcos.desenvolvimento.desafio_tecnico.request.FuncionarioRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class InsertFuncionarioUseCase {

    private final DAOFuncionario daoFuncionario;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public InsertFuncionarioUseCase(DAOFuncionario daoFuncionario){
        this.daoFuncionario = daoFuncionario;
    }

    public void salvar(final FuncionarioRequest funcionarioRequest) {
        if (funcionarioRequest == null) {
            throw new IllegalArgumentException("O funcionário não pode ser nulo!");
        }
        Funcionario funcionario = new Funcionario();

        if (funcionarioRequest.getNome() != null && !funcionarioRequest.getNome().isEmpty()) {
            funcionario.setNome(funcionarioRequest.getNome());
        }
        if (funcionarioRequest.getCpf() != null && funcionarioRequest.getCpf().length() == 11) {
            funcionario.setCpf(funcionarioRequest.getCpf());
        }
        if (funcionarioRequest.getDtNascimento() != null) {
            funcionario.setDtNascimento(funcionarioRequest.getDtNascimento());
        }
        if (funcionarioRequest.getDtAdmissao() != null) {
            funcionario.setDtAdmissao(funcionarioRequest.getDtAdmissao());
        }
        if (funcionarioRequest.getCargo() != null && !funcionarioRequest.getCargo().isEmpty()) {
            funcionario.setCargo(funcionarioRequest.getCargo());
        }

        funcionario.setIsAtivo("true");
        daoFuncionario.salvarFuncionario(funcionario);
    }
}
