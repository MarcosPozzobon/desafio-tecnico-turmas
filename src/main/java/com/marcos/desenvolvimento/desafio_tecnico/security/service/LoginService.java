package com.marcos.desenvolvimento.desafio_tecnico.security.service;

import com.marcos.desenvolvimento.desafio_tecnico.repository.dto.LoginRequestDTO;
import com.marcos.desenvolvimento.desafio_tecnico.security.jwt.TokenUtils;
import com.marcos.desenvolvimento.desafio_tecnico.usecases.FindUserUseCase;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class LoginService {

    private final AuthenticationManager authenticationManager;
    private final TokenUtils tokenUtils;
    private final FindUserUseCase findUserUseCase;

    public LoginService(AuthenticationManager authenticationManager, TokenUtils tokenUtils, FindUserUseCase findUserUseCase){
        this.authenticationManager = authenticationManager;
        this.tokenUtils = tokenUtils;
        this.findUserUseCase = findUserUseCase;
    }

    public Map<String, String> validateLogin(final LoginRequestDTO loginRequest) {

        if (loginRequest.login() == null || loginRequest.login().isEmpty()) {
            throw new BadCredentialsException("O login não pode ser nulo!");
        }

        if (loginRequest.senha() == null || loginRequest.senha().isEmpty()) {
            throw new BadCredentialsException("A senha não pode ser nula!");
        }

        final var authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.login(), loginRequest.senha()));

        var userDetails = (UserDetailsImpl) authenticate.getPrincipal();
        SecurityContextHolder.getContext().setAuthentication(authenticate);

        String token = tokenUtils.generateTokenFromUserDetails(userDetails);
        Map<String, String> response = new HashMap<>();
        response.put("token", token);

        return response;
    }


}
