package com.marcos.desenvolvimento.desafio_tecnico.mapper;

import com.marcos.desenvolvimento.desafio_tecnico.entity.Funcionario;
import com.marcos.desenvolvimento.desafio_tecnico.request.FuncionarioRequest;
import com.marcos.desenvolvimento.desafio_tecnico.response.FuncionarioResponse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class FuncionarioMapper {

    public FuncionarioRequest toFuncionarioRequest(Funcionario funcionario) {

        if (funcionario == null) {
            throw new IllegalArgumentException("Funcionário não pode ser nulo.");
        }
        if (funcionario.getNome() == null || funcionario.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do funcionário não pode ser vazio.");
        }

        if (funcionario.getCargo() == null || funcionario.getCargo().trim().isEmpty()) {
            throw new IllegalArgumentException("Cargo do funcionário não pode ser vazio.");
        }

        if (funcionario.getCpf() == null || !funcionario.getCpf().matches("\\d{11}")) {
            throw new IllegalArgumentException("CPF do funcionário deve ter 11 dígitos numéricos.");
        }

        if (funcionario.getDtAdmissao() == null) {
            throw new IllegalArgumentException("Data de admissão não pode ser nula.");
        }

        if (funcionario.getDtNascimento() == null) {
            throw new IllegalArgumentException("Data de nascimento não pode ser nula.");
        }

        FuncionarioRequest funcionarioRequest = new FuncionarioRequest();
        funcionarioRequest.setNome(funcionario.getNome());
        funcionarioRequest.setCargo(funcionario.getCargo());
        funcionarioRequest.setCpf(funcionario.getCpf());
        funcionarioRequest.setDtAdmissao(funcionario.getDtAdmissao());
        funcionarioRequest.setDtNascimento(funcionario.getDtNascimento());
        return funcionarioRequest;
    }

    public FuncionarioResponse toFuncionarioResponse(final Funcionario funcionario){
        FuncionarioResponse funcionarioResponse = new FuncionarioResponse();

        if(funcionario.getNome() != null && !funcionario.getNome().isEmpty()){
            funcionarioResponse.setNome(funcionario.getNome());
        }

        if(funcionario.getCpf() != null && !funcionario.getCpf().isEmpty()){
            funcionarioResponse.setCpf(funcionario.getCpf());
        }

        if(funcionario.getCargo() != null && !funcionario.getCargo().isEmpty()){
            funcionarioResponse.setCargo(funcionario.getCargo());
        }

        return funcionarioResponse;
    }

    public List<FuncionarioResponse> toFuncionarioResponseList(List<Funcionario> list){
        return list.stream()
                .map(this::toFuncionarioResponse)
                .toList();
    }

}
