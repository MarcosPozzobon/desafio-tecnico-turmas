package com.marcos.desenvolvimento.desafio_tecnico.repository.dto;

public record TokenGeradoDTO (
        String token,
        int expiresIn
){
}
