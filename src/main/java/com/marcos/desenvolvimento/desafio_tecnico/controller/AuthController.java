package com.marcos.desenvolvimento.desafio_tecnico.controller;

import com.marcos.desenvolvimento.desafio_tecnico.repository.dto.LoginRequestDTO;
import com.marcos.desenvolvimento.desafio_tecnico.repository.dto.TokenGeradoDTO;
import com.marcos.desenvolvimento.desafio_tecnico.security.service.LoginService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpHeaders.SET_COOKIE;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("api/v1/auth")
public class AuthController {

    private final LoginService loginService;

    public AuthController(LoginService loginService){
        this.loginService = loginService;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> authenticateUser(@RequestBody LoginRequestDTO loginRequest) {
        final var tokenResponse = loginService.validateLogin(loginRequest);
        return ResponseEntity.ok().body(tokenResponse);
    }
}
