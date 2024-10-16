package com.marcos.desenvolvimento.desafio_tecnico.entity;

import org.springframework.security.core.GrantedAuthority;

public class Role implements GrantedAuthority {

    private int id;

    private String roleDesc;

    public Role(){}

    public Role(int id, String roleDesc) {
        this.id = id;
        this.roleDesc = roleDesc;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRoleDesc() {
        return roleDesc;
    }

    public void setRoleDesc(String roleDesc) {
        this.roleDesc = roleDesc;
    }

    @Override
    public String getAuthority() {
        return roleDesc;
    }
}
