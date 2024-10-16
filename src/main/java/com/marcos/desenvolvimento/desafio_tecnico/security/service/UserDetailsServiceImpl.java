package com.marcos.desenvolvimento.desafio_tecnico.security.service;

import com.marcos.desenvolvimento.desafio_tecnico.usecases.FindUserUseCase;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final FindUserUseCase findUserUseCase;

    public UserDetailsServiceImpl(FindUserUseCase findUserUseCase){
        this.findUserUseCase = findUserUseCase;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return findUserUseCase.encontrar(username);
    }
}
