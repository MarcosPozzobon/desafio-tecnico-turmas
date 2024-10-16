package com.marcos.desenvolvimento.desafio_tecnico.entity;

import java.time.LocalDate;
import java.util.Date;

public class Funcionario {

    private int codigoFuncionario;

    private String nome;

    private String cpf;

    private LocalDate dtNascimento;

    private String cargo;

    private LocalDate dtAdmissao;

    private String isAtivo;

    public Funcionario() {

    }

    public Funcionario(int codigoFuncionario, String nome, String cpf, LocalDate dtNascimento, String cargo, LocalDate dtAdmissao, String isAtivo) {
        this.codigoFuncionario = codigoFuncionario;
        this.nome = nome;
        this.cpf = cpf;
        this.dtNascimento = dtNascimento;
        this.cargo = cargo;
        this.dtAdmissao = dtAdmissao;
        this.isAtivo = isAtivo;
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
