package com.marcos.desenvolvimento.desafio_tecnico.response;

import com.marcos.desenvolvimento.desafio_tecnico.entity.Funcionario;

import java.util.Date;

public class FuncionarioResponse {

    private String nome;

    private String cpf;

    private String cargo;

    public FuncionarioResponse(){}

    public FuncionarioResponse(String nome, String cpf, String cargo) {
        this.nome = nome;
        this.cpf = cpf;
        this.cargo = cargo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }
}
