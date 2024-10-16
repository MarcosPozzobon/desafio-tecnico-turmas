package com.marcos.desenvolvimento.desafio_tecnico.usecases;

import com.marcos.desenvolvimento.desafio_tecnico.repository.DAOUser;
import com.marcos.desenvolvimento.desafio_tecnico.security.service.UserDetailsImpl;
import org.springframework.stereotype.Service;

@Service
public class FindUserUseCase {

    private final DAOUser daoUser;

    public FindUserUseCase(DAOUser daoUser){
        this.daoUser = daoUser;
    }

    public UserDetailsImpl encontrar(String login){
        return daoUser.obterUsuarioPorLogin(login);
    }
}
