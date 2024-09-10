package com.marcos.desenvolvimento.desafio_tecnico.request;

import java.util.Date;

public class FuncionarioRequest {

    private String nome;

    private String cpf;

    private Date dtNascimento;

    private String cargo;

    private Date dtAdmissao;

    private boolean status;

    public FuncionarioRequest() {

    }

    public FuncionarioRequest(String nome, String cpf, Date dtNascimento, String cargo, Date dtAdmissao, boolean status) {
        this.nome = nome;
        this.cpf = cpf;
        this.dtNascimento = dtNascimento;
        this.cargo = cargo;
        this.dtAdmissao = dtAdmissao;
        this.status = status;
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

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
