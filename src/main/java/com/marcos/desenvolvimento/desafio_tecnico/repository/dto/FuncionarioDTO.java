package com.marcos.desenvolvimento.desafio_tecnico.repository.dto;

import com.marcos.desenvolvimento.desafio_tecnico.entity.Funcionario;

public class FuncionarioDTO {

    private String nomeFuncionario;

    public FuncionarioDTO(){}

    public String getNomeFuncionario() {
        return nomeFuncionario;
    }

    public void setNomeFuncionario(String nomeFuncionario) {
        this.nomeFuncionario = nomeFuncionario;
    }
}
