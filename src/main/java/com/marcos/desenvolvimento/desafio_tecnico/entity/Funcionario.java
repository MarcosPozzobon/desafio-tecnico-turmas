package com.marcos.desenvolvimento.desafio_tecnico.entity;

import java.util.Date;

public class Funcionario {

    private int codigoFuncionario;

    private String nome;

    private String cpf;

    private Date dtNascimento;

    private String cargo;

    private Date dtAdmissao;

    private int status;

    public Funcionario() {

    }

    public Funcionario(int codigoFuncionario, String nome, String cpf, Date dtNascimento, String cargo, Date dtAdmissao, int status) {
        this.codigoFuncionario = codigoFuncionario;
        this.nome = nome;
        this.cpf = cpf;
        this.dtNascimento = dtNascimento;
        this.cargo = cargo;
        this.dtAdmissao = dtAdmissao;
        this.status = codigoFuncionario;
    }


    public int getCodigoFuncionario() {
        return codigoFuncionario;
    }

    public void setCodigoFuncionario(int codigoFuncionario) {
        this.codigoFuncionario = codigoFuncionario;
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

    public Date getDtNascimento() {
        return dtNascimento;
    }

    public void setDtNascimento(Date dtNascimento) {
        this.dtNascimento = dtNascimento;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public Date getDtAdmissao() {
        return dtAdmissao;
    }

    public void setDtAdmissao(Date dtAdmissao) {
        this.dtAdmissao = dtAdmissao;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }



}
