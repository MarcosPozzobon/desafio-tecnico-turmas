package com.marcos.desenvolvimento.desafio_tecnico.repository.dto;

import org.springframework.lang.NonNull;

public record LoginRequestDTO(@NonNull String login, @NonNull String senha) {
}
