package com.marcos.desenvolvimento.desafio_tecnico.repository;

import com.marcos.desenvolvimento.desafio_tecnico.entity.Curso;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class DAOCurso {

    private static final Logger LOGGER = LoggerFactory.getLogger(DAOCurso.class);

    private final JdbcTemplate jdbcTemplate;

    public DAOCurso(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional
    public void salvarCurso(Curso curso) {
        String queryInclusaoCurso = "INSERT INTO curso (nome, descricao, duracao) VALUES (?, ?, ?)";
        try {
            jdbcTemplate.update(queryInclusaoCurso, curso.getNome(), curso.getDescricao(), curso.getDuracao());
            LOGGER.info("Realizado um INSERT na tabela curso com os seguintes valores: nome={}, descricao={}, duracao={}",
                    curso.getNome(), curso.getDescricao(), curso.getDuracao());
        } catch (Exception e) {
            LOGGER.error("Erro ao realizar um insert na tabela curso! Classe de erro: " + this.getClass().getName(), e);
        }
    }
}
