package com.marcos.desenvolvimento.desafio_tecnico.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.util.Date;

public class FuncionarioRequest {

    private String nome;

    private String cpf;

    @JsonProperty(value = "dt_nascimento")
    private LocalDate dtNascimento;

    private String cargo;

    @JsonProperty(value = "dt_admissao")
    private LocalDate dtAdmissao;

    @JsonProperty(value = "is_ativo")
    private String isAtivo;

    public FuncionarioRequest(){}

    public FuncionarioRequest(String nome, String cpf, LocalDate dtNascimento, String cargo, LocalDate dtAdmissao, String isAtivo) {
        this.nome = nome;
        this.cpf = cpf;
        this.dtNascimento = dtNascimento;
        this.cargo = cargo;
        this.dtAdmissao = dtAdmissao;
        this.isAtivo = isAtivo;
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

    public LocalDate getDtNascimento() {
        return dtNascimento;
    }

    public void setDtNascimento(LocalDate dtNascimento) {
        this.dtNascimento = dtNascimento;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public LocalDate getDtAdmissao() {
        return dtAdmissao;
    }

    public void setDtAdmissao(LocalDate dtAdmissao) {
        this.dtAdmissao = dtAdmissao;
    }

    public String getIsAtivo() {
        return isAtivo;
    }

    public void setIsAtivo(String isAtivo) {
        this.isAtivo = isAtivo;
    }
}
