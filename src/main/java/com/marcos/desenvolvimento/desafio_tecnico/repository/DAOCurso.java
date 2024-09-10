package com.marcos.desenvolvimento.desafio_tecnico.repository;

import com.marcos.desenvolvimento.desafio_tecnico.entity.Curso;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
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
    public void salvarCurso(final Curso curso) {
        String queryInclusaoCurso = "INSERT INTO curso (nome, descricao, duracao) VALUES (?, ?, ?)";
        try {
            jdbcTemplate.update(queryInclusaoCurso, curso.getNome(), curso.getDescricao(), curso.getDuracao());
            LOGGER.info("Realizado um INSERT na tabela curso com os seguintes valores: nome={}, descricao={}, duracao={}",
                    curso.getNome(), curso.getDescricao(), curso.getDuracao());
        } catch (Exception e) {
            LOGGER.error("Erro ao realizar um insert na tabela curso! Classe de erro: " + this.getClass().getName(), e);
        }
    }

    @Transactional
    public void atualizarNomeCurso(final Curso curso, int codigoCurso) {
        String queryAtualizacaoCurso = "UPDATE curso SET nome = ?, descricao = ?, duracao = ? WHERE codigo_curso = ?";
        try {
            jdbcTemplate.update(queryAtualizacaoCurso, curso.getNome(), curso.getDescricao(), curso.getDuracao(), codigoCurso);
            LOGGER.info("Realizado um UPDATE na tabela curso no campo nome com o seguinte valor: {} , " +
                    "passando o seguinte código: {}", curso.getNome(), codigoCurso);
        } catch (Exception e) {
            LOGGER.error("Erro ao realizar um update na tabela curso! Classe de erro: " + this.getClass().getName(), e);
            throw e;
        }
    }

    public List<Curso> listarTodosOsCursos() {
        String queryListaCursos = "SELECT * FROM curso;";
        try {
            List<Curso> cursos = jdbcTemplate.query(queryListaCursos, new BeanPropertyRowMapper<>(Curso.class));
            LOGGER.info("Realizado um SELECT * FROM na tabela curso no seguinte horário: " + LocalDateTime.now());
            return cursos;
        } catch (Exception e) {
            LOGGER.error("Ocorreu um erro ao realizar um SELECT * FROM na tabela cursos. Detalhes da exception: " + e.getMessage());
            throw e;
        }
    }

}
