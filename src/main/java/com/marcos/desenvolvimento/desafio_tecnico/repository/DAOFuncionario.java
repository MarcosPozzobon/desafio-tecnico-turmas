package com.marcos.desenvolvimento.desafio_tecnico.repository;

import com.marcos.desenvolvimento.desafio_tecnico.entity.Curso;
import com.marcos.desenvolvimento.desafio_tecnico.entity.Funcionario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class DAOFuncionario {

    private static final Logger LOGGER = LoggerFactory.getLogger(DAOCurso.class);

    private final JdbcTemplate jdbcTemplate;

    public DAOFuncionario(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional
    public void salvarFuncionario(final Funcionario funcionario) {
        String queryInclusaoFuncionario = "INSERT INTO funcionario (nome, cpf, dt_nascimento, cargo, dt_admissao, status) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            jdbcTemplate.update(queryInclusaoFuncionario,
                    funcionario.getNome(),
                    funcionario.getCpf(),
                    funcionario.getDtNascimento(),
                    funcionario.getCargo(),
                    funcionario.getDtAdmissao(),
                    funcionario.getStatus());

            LOGGER.info("Realizado um INSERT na tabela funcionario com os seguinte valores: nome={}, cpf={}, data de nascimento={}, cargo={}, data de admissão={} e status={}",
                    funcionario.getNome(),
                    funcionario.getCpf(),
                    funcionario.getDtNascimento(),
                    funcionario.getCargo(),
                    funcionario.getDtAdmissao(),
                    funcionario.getStatus());
        } catch (Exception e) {
            LOGGER.error("Erro ao realizar um insert na tabela funcionario! Classe de erro: " + this.getClass().getName(), e);
        }
    }
}
