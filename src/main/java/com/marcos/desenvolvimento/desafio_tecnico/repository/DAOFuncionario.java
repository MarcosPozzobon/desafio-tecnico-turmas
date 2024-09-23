package com.marcos.desenvolvimento.desafio_tecnico.repository;

import com.marcos.desenvolvimento.desafio_tecnico.entity.Curso;
import com.marcos.desenvolvimento.desafio_tecnico.entity.Funcionario;
import com.marcos.desenvolvimento.desafio_tecnico.request.FuncionarioRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class DAOFuncionario {

    private static final Logger LOGGER = LoggerFactory.getLogger(DAOCurso.class);

    private final JdbcTemplate jdbcTemplate;

    public DAOFuncionario(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional
    public void salvarFuncionario(final Funcionario funcionario) {
        String queryInclusaoFuncionario = "INSERT INTO funcionario (nome, cpf, dt_nascimento, cargo, dt_admissao, is_ativo) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            jdbcTemplate.update(queryInclusaoFuncionario,
                    funcionario.getNome(),
                    funcionario.getCpf(),
                    funcionario.getDtNascimento(),
                    funcionario.getCargo(),
                    funcionario.getDtAdmissao(),
                    funcionario.isAtivo());

            LOGGER.info("Realizado um INSERT na tabela funcionario com os seguinte valores: nome={}, cpf={}, data de nascimento={}, cargo={}, data de admissão={} e is_ativo={}",
                    funcionario.getNome(),
                    funcionario.getCpf(),
                    funcionario.getDtNascimento(),
                    funcionario.getCargo(),
                    funcionario.getDtAdmissao(),
                    funcionario.isAtivo());
        } catch (Exception e) {
            LOGGER.error("Erro ao realizar um insert na tabela funcionario! Classe de erro: " + this.getClass().getName(), e);
        }
    }

    public boolean isFuncionarioAtualAtivo(int codigoFuncionario) {
        try {
            String queryVerificaFuncionarioAtivo = "SELECT COUNT(1) FROM funcionario WHERE codigo_funcionario = ? AND is_ativo = 'true'";
            Integer count = jdbcTemplate.queryForObject(queryVerificaFuncionarioAtivo, new Object[]{codigoFuncionario}, Integer.class);
            return count != null && count > 0;
        } catch (Exception e) {
            LOGGER.error("Erro ao verificar se o funcionário está ativo. Possível erro: " + e.getMessage());
            return false;
        }
    }

    @Transactional
    public void atualizarFuncionario(FuncionarioRequest funcionarioRequest, int codigoFuncionario){
        try{
            String atualizarFuncionario = "UPDATE funcionario SET nome = ?, cpf = ?, dt_nascimento = ?, cargo = ?, dt_admissao = ? WHERE codigo_funcionario = ?";
            jdbcTemplate.update(
                    atualizarFuncionario,
                    funcionarioRequest.getNome(),
                    funcionarioRequest.getCpf(),
                    funcionarioRequest.getDtNascimento(),
                    funcionarioRequest.getCargo(),
                    funcionarioRequest.getDtAdmissao(),
                    codigoFuncionario
            );
        }catch (Exception e){
            throw new RuntimeException("Alguma coisa deu errado em " + this.getClass().getName(), e);
        }
    }

    public List<Funcionario> buscarFuncionarioPorNome(String nomeFuncionario) {
        try {
            String buscaFuncionarioPorNome = "" +
                    "SELECT nome, cpf, dt_nascimento, cargo, dt_admissao FROM funcionario " +
                    "WHERE nome ILIKE ? AND is_ativo = 'true'";

            if (nomeFuncionario != null && !nomeFuncionario.isBlank()) {
                String nomeParam = "%" + nomeFuncionario + "%";

                List<Funcionario> funcionarioExistente = jdbcTemplate.query(
                        buscaFuncionarioPorNome,
                        new Object[]{nomeParam},
                        new BeanPropertyRowMapper<>(Funcionario.class)
                );
                return funcionarioExistente;
            }
        } catch (Exception e) {
            throw new RuntimeException("Alguma coisa deu errado em " + this.getClass().getName(), e);
        }
        throw new IllegalArgumentException("Argumento inválido: " + nomeFuncionario + " passado em :" + this.getClass().getName());
    }
}
