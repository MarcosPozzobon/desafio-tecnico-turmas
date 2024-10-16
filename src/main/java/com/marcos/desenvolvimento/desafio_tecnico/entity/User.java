package com.marcos.desenvolvimento.desafio_tecnico.entity;

import com.marcos.desenvolvimento.desafio_tecnico.repository.dto.FuncionarioDTO;

public class User {

    private int id;

    private String login;

    private String senha;

    private FuncionarioDTO funcionario;

    public User(){}

    public User(int id, String login, String senha, FuncionarioDTO funcionario) {
        this.id = id;
        this.login = login;
        this.senha = senha;
        this.funcionario = funcionario;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public FuncionarioDTO getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(FuncionarioDTO funcionario) {
        this.funcionario = funcionario;
    }
}
